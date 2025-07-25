package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataCollection {
    public static double mass = 2; //kg
    public static double g=9.81; // m/s^2
    public static double r=16; // mm
    private Telemetry telemetry;
    private Path path;
    public DataCollection(Telemetry telemetry){
        this.telemetry = telemetry;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = now.format(formatter);
        path = Paths.get(formatted);
    }

    public void update(double velocity_tick_per_sec,double motor_current, double voltage, long time){
        @SuppressLint("DefaultLocale") String line = String.format("Time: %d, Velocity: %.2f, Motor_current: %.2f, Voltage: %.2f,",time ,velocity_tick_per_sec,motor_current,voltage);
        try {
            Files.write(path, List.of(line));
        } catch (IOException e) {System.err.println("Error writing to file: " + e.getMessage());}

        telemetry.addData("Efficiency",calculate_efficiency(velocity_tick_per_sec,voltage,motor_current));
    }
    private double calculate_efficiency(double w, double V, double I){
        w = w * 2 * Math.PI * r / 140;
        return (mass * g * w * r * Math.PI)/(30*V*I);
    }


}
