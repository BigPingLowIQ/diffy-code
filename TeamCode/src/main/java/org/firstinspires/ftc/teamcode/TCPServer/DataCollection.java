package org.firstinspires.ftc.teamcode.TCPServer;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.WeightInterpolationTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DataCollection extends Thread{
    public static double mass = 2; //kg
    public static double g=9.81; // m/s^2
    public static double r=16; // mm
    private Telemetry telemetry;
    public FileWriter writer;
    private Queue<String> lines;
    public DataCollection(Telemetry telemetry){
        this.telemetry = telemetry;
        WeightInterpolationTest.logger.writeLine("Time, Velocity, Motor_current, Voltage");
        lines = new ConcurrentLinkedDeque<>();
    }

    public void update(double velocity_tick_per_sec,double motor_current, double voltage, long time){
        @SuppressLint("DefaultLocale") String line = String.format("\n %d , %.2f , %.2f , %.2f",time ,velocity_tick_per_sec,motor_current,voltage);
        lines.add(line);
//        telemetry.addData("Efficiency",calculate_efficiency(velocity_tick_per_sec,voltage,motor_current));
    }
    private double calculate_efficiency(double w, double V, double I){
        w = w * 2 * Math.PI * r / 140;
        return (mass * g * w * r * Math.PI)/(30*V*I);
    }
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
    private void handleClient(Socket socket){
        try{
            if(!lines.isEmpty()) {
                String line = lines.poll();
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(line);
            }

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
