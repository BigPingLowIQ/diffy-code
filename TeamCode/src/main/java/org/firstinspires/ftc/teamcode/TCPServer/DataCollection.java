package org.firstinspires.ftc.teamcode.TCPServer;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.WeightInterpolationTest;

import java.io.FileWriter;

public class DataCollection {
    public static double mass = 2; //kg
    public static double g=9.81; // m/s^2
    public static double r=16; // mm
    private Telemetry telemetry;
    public FileWriter writer;
    public DataCollection(Telemetry telemetry){
        this.telemetry = telemetry;
        WeightInterpolationTest.logger.writeLine("Time, Velocity, Motor_current, Voltage");
    }

    public void update(double velocity_tick_per_sec,double motor_current, double voltage, long time){
        @SuppressLint("DefaultLocale") String line = String.format("\n %d , %.2f , %.2f , %.2f",time ,velocity_tick_per_sec,motor_current,voltage);
        WeightInterpolationTest.logger.writeLine(line);

//        telemetry.addData("Efficiency",calculate_efficiency(velocity_tick_per_sec,voltage,motor_current));
    }
    private double calculate_efficiency(double w, double V, double I){
        w = w * 2 * Math.PI * r / 140;
        return (mass * g * w * r * Math.PI)/(30*V*I);
    }
}
