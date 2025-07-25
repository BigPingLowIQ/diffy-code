package org.firstinspires.ftc.teamcode;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataCollection {
    public static double mass = 2; //kg
    public static double g=9.81; // m/s^2
    public static double r=16; // mm
    private Telemetry telemetry;
    private final String file_name;
    public FileWriter writer;
    public DataCollection(Telemetry telemetry){
        this.telemetry = telemetry;
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
        file_name = (formatter.format(now)+".csv");
        // Get the directory where you can safely store file
        writeLogFile("Time, Velocity, Motor_current, Voltage");
    }

    public void update(double velocity_tick_per_sec,double motor_current, double voltage, long time){
        @SuppressLint("DefaultLocale") String line = String.format("\n %d , %.2f , %.2f , %.2f",time ,velocity_tick_per_sec,motor_current,voltage);
        boolean yes = writeLogFile(line);

        telemetry.addData("writing",yes);
//        telemetry.addData("Efficiency",calculate_efficiency(velocity_tick_per_sec,voltage,motor_current));
    }
    private double calculate_efficiency(double w, double V, double I){
        w = w * 2 * Math.PI * r / 140;
        return (mass * g * w * r * Math.PI)/(30*V*I);
    }

    private boolean writeLogFile(String content) {
        try {
            // Use /sdcard/FIRST/, which is usually safe for FTC apps
            File dir = new File("/sdcard/FIRST/");
            if (!dir.exists()) {
                dir.mkdirs(); // Create the directory if needed
            }

            // Create or open the file for appending
            File file = new File(dir, file_name);

            // Append mode = true
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Write and force flush
            bw.write(content);
            bw.newLine();  // Optional: adds a newline
            bw.flush();    // 🔧 Very important: force flush to file system
            bw.close();    // 🔧 Always close to release system buffer
            return true;
        } catch (IOException e) {
            e.printStackTrace();return false;
        }
    }


}
