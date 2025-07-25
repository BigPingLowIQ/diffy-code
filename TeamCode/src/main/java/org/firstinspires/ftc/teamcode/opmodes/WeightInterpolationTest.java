package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.DataCollection;
import org.firstinspires.ftc.teamcode.control.DiffyMotors;
import org.firstinspires.ftc.teamcode.control.TimeInterpolation;

import java.util.concurrent.TimeUnit;

@Config
@TeleOp
public class WeightInterpolationTest extends LinearOpMode {
    DiffyMotors motors;
    TimeInterpolation up;
    TimeInterpolation down;
    DataCollection data;
    public static int target1start=0,target1end=2000,target2=0;
    public static double speed_mm_per_sec = 200; // mm/s
    public static int wait_time = 1000;
    enum State{
        START_GOING_UP,
        GOING_UP,
        START_GOING_DOWN,
        GOING_DOWN

    }
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        motors = new DiffyMotors(hardwareMap);
        up = new TimeInterpolation(target1end-target1start, speed_mm_per_sec);
        down = new TimeInterpolation(target1end-target1start, speed_mm_per_sec);
        data = new DataCollection(telemetry,2);
        LynxModule ch = hardwareMap.getAll(LynxModule.class).get(0);
        State state = State.START_GOING_UP;
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        double sum = 0;
        double n = 0;

        waitForStart();

        timer.startTime();
        timer.reset();
        motors.setOutputTargets(0,0);
        while(opModeIsActive()){
            switch (state){
                case START_GOING_DOWN:{
                    if(timer.time()>wait_time){
                        down.start();
                        state = State.GOING_DOWN;
                    }
                    break;
                }
                case GOING_DOWN:{
                    motors.setOutputTargets(target2,target1start+down.getPosition());
                    if(!down.isInterpolating()){
                        state = State.START_GOING_UP;
                        timer.reset();
                        timer.startTime();
                    }
                    n++;
                    sum += Math.abs(motors.getVelocities()[1]);
                    break;
                }
                case START_GOING_UP:{
                    if(timer.time(TimeUnit.MILLISECONDS)>wait_time){
                        up.start();
                        state = State.GOING_UP;
                    }
                    break;
                }
                case GOING_UP:{
                    motors.setOutputTargets(target2,target1end-up.getPosition());
                    if(!up.isInterpolating()){
                        state = State.START_GOING_DOWN;
                        timer.reset();
                        timer.startTime();
                    }
                    n++;
                    sum += Math.abs(motors.getVelocities()[1]);
                    break;
                }
            }



            //telemetry.addData("Velocity",motors.getVelocities()[0]);
            telemetry.addData("Velocity",motors.getVelocities()[1]);
            telemetry.addData("Mean Velocity",sum/n);
            telemetry.addData("State",state);
            telemetry.addData("Target",motors.getOutputTargets()[1]);
            telemetry.addData("Current",motors.getOutputCurrents()[1]);
            telemetry.update();
            data.update(motors.getVelocities()[0],motors.getMotorsCurrents()[0],ch.getInputVoltage(VoltageUnit.VOLTS));
            motors.update(telemetry);
        }
    }
}
