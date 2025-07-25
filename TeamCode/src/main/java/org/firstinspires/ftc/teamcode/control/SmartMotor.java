package org.firstinspires.ftc.teamcode.control;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
public class SmartMotor  {
    private DcMotorEx motor;
    private PIDController pid;
    private int target;
    public static double kp=0.03,ki=0.000008,kd=0.0001;

    enum RUN_MODE{
        POSITION_PID,
        VELOCITY_PID
    }

    protected SmartMotor(DcMotorEx motor, boolean reverse){
        this.motor = motor;
        this.motor.setDirection(reverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.pid = new PIDController(kp,ki,kd);
        this.target = 0;
    }

    protected void update(){
        pid.kp = kp;
        pid.ki = ki;
        pid.kd = kd;
        double power = pid.calculate(motor.getCurrentPosition(),target);
        motor.setPower(-power);
    }

    public void setTarget(int target){
        this.target = target;
    }

    public int getTarget(){
        return target;
    }
    public double getVelocity(){
        return motor.getVelocity(); // ticks/s
    }

    public double getCurrent(){
        return motor.getCurrent(CurrentUnit.AMPS);
    }
    public int getPosition(){
        return motor.getCurrentPosition();
    }


}
