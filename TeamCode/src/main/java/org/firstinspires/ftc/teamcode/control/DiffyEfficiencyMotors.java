package org.firstinspires.ftc.teamcode.control;

import android.sax.StartElementListener;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
public class DiffyEfficiencyMotors {
    public DcMotorEx m1,m2;
    public Encoder e;
    private PIDController position_pid;
    public static double p=0.015,i=0,d=0;
    public static double pv=0.00003,iv=0,dv=0.000001;
    private int currentPosition=0;
    private int targetPosition=0;
    private double power=0;
    private double currentVelocity=0;
    private double targetVelocity=0;
    private PIDController velocity_pid;
    public static RUN_MODE runMode;
    public DiffyEfficiencyMotors(HardwareMap hm){
        m1 = new DcMotorImplEx(hm.get(DcMotorControllerEx.class,"Control Hub"),0);
        m2 = new DcMotorImplEx(hm.get(DcMotorControllerEx.class,"Control Hub"),3);

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        e = new Encoder(m2);
        e.setDirection(Encoder.Direction.REVERSE);
        position_pid = new PIDController(p,i,d);
        velocity_pid = new PIDController(pv,iv,dv);
        runMode = RUN_MODE.POSITION_PID;

    }

    public void update(Telemetry tele){
        position_pid.kp = p;
        position_pid.ki = i;
        position_pid.kd = d;
        velocity_pid.kp = pv;
        velocity_pid.ki = iv;
        velocity_pid.kd = dv;

        currentPosition = e.getCurrentPosition();
        currentVelocity = e.getCorrectedVelocity();

        if(runMode == RUN_MODE.POSITION_PID)
            power = position_pid.calculate(currentPosition,targetPosition);
        else if(runMode == RUN_MODE.VELOCITY_PID)
            power+= velocity_pid.calculate(currentVelocity,targetVelocity);


        power = Math.max(Math.min(power,1),-1);
        m1.setPower(power);
        m2.setPower(power);

        tele.addData("power",power);
        tele.addData("CurrentPosition",currentPosition);
        tele.addData("TargetPosition",targetPosition);
        tele.addData("CurrentVelocity",currentVelocity);
        tele.addData("TargetVelocity",targetVelocity);

    }
    public void setTargetPosition(int target){
        targetPosition = target;
    }
    public void setTargetVelocity(double target){
        targetVelocity = target;
    }
    public void setRunMode(RUN_MODE mode){
        runMode = mode;
    }
    public double getCurrentDraw(){
        return m1.getCurrent(CurrentUnit.AMPS)+m2.getCurrent(CurrentUnit.AMPS);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }
    public double getCurrentVelocityRadians() {
        return e.getRawVelocityRadians();
    }
}
