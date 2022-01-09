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
import static java.time.Clock.system;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Clients {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    
    public Clients(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch(IOException ex){
            closeEverything(socket, bufferedReader,bufferedWriter);
        }
    }
    
    public void SendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(username + " : "+message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException ex){
            closeEverything(socket, bufferedReader,bufferedWriter);
        }
    }
    
    public void ShowMessage(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String messageFromOthers;
                while(socket.isConnected()){
                    try{
                        messageFromOthers = bufferedReader.readLine();
                        System.out.println(messageFromOthers);
                    }catch(IOException ex){
                        closeEverything(socket, bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();
    }
    
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter ){
        try{
            if(socket != null){
                socket.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username for the chat");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1111);
        Clients client = new Clients(socket, username);
        client.ShowMessage();
        client.SendMessage();
    }
    
}
