package org.firstinspires.ftc.teamcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLogger {
    private BufferedWriter writer;

    public void open() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
        String file_name = (formatter.format(now)+".csv");
        // Get the directory where you can safely store file
        try {
            File dir = new File("/sdcard/FIRST/");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, file_name);
            writer = new BufferedWriter(new FileWriter(file, true)); // append = true
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String line) {
        if (writer == null) return;
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (writer == null) return;
        try {
            writer.flush();  // flush to ensure all is written
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
