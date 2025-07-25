package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DataCollection {
    public static double mass =2; //kg
    public static double g=9.81; // m/s^2
    public static double r=16; // mm
    private Telemetry telemetry;
    public DataCollection(Telemetry telemetry){
        this.telemetry = telemetry;
    }

    public DataCollection(Telemetry telemetry, double mass){
        this.telemetry = telemetry;
        this.mass = mass;
    }

    public void update(double velocity_tick_per_sec,double motor_current, double voltage){
        telemetry.addData("Efficiency",calculate_efficiency(velocity_tick_per_sec,voltage,motor_current));
    }

    public double calculate_efficiency(double w, double V, double I){
        w = w * 2 * Math.PI * r / 140;
        return (mass * g * w * r * Math.PI)/(30*V*I);
    }


}
