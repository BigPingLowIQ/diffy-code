package org.firstinspires.ftc.teamcode.control;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.TCPServer.DataCollection;

import java.util.concurrent.TimeUnit;

public class TimeInterpolation {
    private int distance;
    private double duration;
    private ElapsedTime timer;
    private double targetSpeed;
    private boolean isInterpolating;
    public TimeInterpolation(int distance, double speed_mm_per_sec){
        this.distance = distance;
        this.duration = ((distance)*2*Math.PI*DataCollection.r /(140*speed_mm_per_sec))*1000; // millis
        this.targetSpeed = speed_mm_per_sec;
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        this.isInterpolating = false;
    }

    public void start(){
        timer.reset();
        timer.startTime();
        this.isInterpolating = true;
    }

    public int getPosition(){
        if (timer.time(TimeUnit.MILLISECONDS) > duration)return distance;
        return (int)(((double)(distance)*(double)(timer.time(TimeUnit.MILLISECONDS)))/duration);
    }
    public boolean isInterpolating(){
        return !(timer.time()>duration);
    }
    public double getSpeed(){
        return targetSpeed;
    }
    public double getDuration(){
        return duration; // seconds
    }
}
