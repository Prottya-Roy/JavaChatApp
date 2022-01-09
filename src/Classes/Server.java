/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Server {
     private ServerSocket serverSocket;
     
     public Server(ServerSocket serverSocket){
         this.serverSocket = serverSocket;
     }
     
     public void startingServerSocket(){
         try{
             
             while(!serverSocket.isClosed()){
                 
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected !!!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
             }
         }catch(IOException ex){
             
         }
     }
     
     public void closingServerSocket(){
         try{
             if(serverSocket != null){
                 serverSocket.close();
             }
         }catch(IOException ex){
             ex.printStackTrace();
         }
     }
     
     public static void main(String args[]) throws IOException{
         
         ServerSocket serverSocket = new ServerSocket(1111);
         Server server = new Server(serverSocket);
         server.startingServerSocket();
         
     }
}
