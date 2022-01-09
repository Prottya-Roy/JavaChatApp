/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author User
 */
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlerList = new ArrayList<>();
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    
    public ClientHandler(Socket socket) throws IOException{
         try{
             this.socket = socket;
             this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             this.clientUsername = bufferedReader.readLine();
             clientHandlerList.add(this);
             broadcastMessage("Server: "+clientUsername+" has entered the chat group.");
             
         }catch(IOException ex){
             closeEverything(socket, bufferedReader, bufferedWriter);
         }
    }
    


    @Override
    public void run() {
        String message;
        while(socket.isConnected()){
                try {
                    message= bufferedReader.readLine();
                    broadcastMessage(message);
                } catch (IOException ex) {
                    closeEverything(socket,bufferedReader,bufferedWriter);
                    break;
                }
        }
    }
    
    public void broadcastMessage(String message){
        for(ClientHandler clientHandler : clientHandlerList){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch(IOException ex){
                 closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }
    
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter ){
        removeClient();
        
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public void removeClient(){
        clientHandlerList.remove(this);
        broadcastMessage("Server: "+clientUsername+" has left the chat"); 
    }
   
}