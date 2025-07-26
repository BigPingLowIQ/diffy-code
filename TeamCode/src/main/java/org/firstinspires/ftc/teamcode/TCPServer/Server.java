package org.firstinspires.ftc.teamcode.TCPServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private ServerSocket serverSocket;
    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(5000);
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(()->handleClient(socket)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket socket){
        try{
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output,true);
            writer.println();

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
