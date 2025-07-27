package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.TCPServer.DataCollection;
import org.firstinspires.ftc.teamcode.TCPServer.FileLogger;
import org.firstinspires.ftc.teamcode.control.DiffyEfficiencyMotors;
import org.firstinspires.ftc.teamcode.control.RUN_MODE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Config
@TeleOp
public class WeightInterpolationTest extends LinearOpMode {
    DiffyEfficiencyMotors motors;
    DataCollection data;
    public static int downPosition = 0;
    public static int upPosition = 1000;
    public static int going_up_velocity = 140;
    public static int going_down_velocity = -560;
    public static int wait_time = 1000;
    public static int threshold = 50;
    public static int HZ_LIMIT = 100;
    private int count = 0;
    enum State{
        START_GOING_UP,
        GOING_UP,
        START_GOING_DOWN,
        GOING_DOWN,
        DONE

    }
    @Override
    public void runOpMode() throws InterruptedException {
//        logger = new FileLogger();
//        logger.open();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        motors = new DiffyEfficiencyMotors(hardwareMap);
        data = new DataCollection(telemetry);
        data.start();

        LynxModule ch = hardwareMap.getAll(LynxModule.class).get(0);
        State state = State.START_GOING_UP;
        ElapsedTime run_timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        ElapsedTime hz_timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        motors.setTargetPosition(downPosition);

        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }


        waitForStart();

        timer.reset();
        timer.startTime();
        hz_timer.reset();
        hz_timer.startTime();

        while(opModeIsActive() && state != State.DONE){
            hz_timer.reset();
            hz_timer.startTime();
            for (LynxModule hub : hardwareMap.getAll(LynxModule.class))
                hub.clearBulkCache();

            switch (state){
                case START_GOING_UP:{
                    if(count==4){
                        state = State.DONE;
                        break;
                    }
                    if(!isInThreshold(motors.getCurrentPosition(), motors.getTargetPosition(), threshold)){
                        timer.reset();
                        timer.startTime();
                    }else if(timer.time()>wait_time) {
                        state = State.GOING_UP;
                        data.update("Start Going Up");
                        motors.setTargetVelocity(going_up_velocity);
                        motors.setRunMode(RUN_MODE.VELOCITY_PID);
                    }
                    break;
                }
                case GOING_UP:{
                    data.update(motors.getCurrentVelocity(),ch.getCurrent(CurrentUnit.AMPS),ch.getInputVoltage(VoltageUnit.VOLTS),run_timer.time(TimeUnit.MILLISECONDS));
                    if(motors.getCurrentPosition()>upPosition){
                        state = State.START_GOING_DOWN;
                        motors.setRunMode(RUN_MODE.POSITION_PID);
                        motors.setTargetPosition(upPosition);
                        timer.reset();
                        timer.startTime();
                    }
                    break;
                }
                case START_GOING_DOWN:{
                    if(!isInThreshold(motors.getCurrentPosition(), motors.getTargetPosition(), threshold)){
                        timer.reset();
                        timer.startTime();
                    }else if(timer.time()>wait_time) {
                        state = State.GOING_DOWN;
//                        data.update("Start going down");
                        motors.setTargetVelocity(going_down_velocity);
                        motors.setRunMode(RUN_MODE.VELOCITY_PID);
                    }
                    break;
                }
                case GOING_DOWN:{
//                    data.update(motors.getCurrentVelocityRadians(),ch.getCurrent(CurrentUnit.AMPS),ch.getInputVoltage(VoltageUnit.VOLTS),run_timer.time(TimeUnit.MILLISECONDS));
                    if(motors.getCurrentPosition()<downPosition){
                        state = State.START_GOING_UP;
                        motors.setRunMode(RUN_MODE.POSITION_PID);
                        motors.setTargetPosition(downPosition);
                        timer.reset();
                        timer.startTime();
                        going_up_velocity = going_up_velocity * 2;
                        count++;
                    }
                    break;
                }
            }

            motors.update(telemetry);
            telemetry.addData("time",timer.time());
            telemetry.addData("state",state);
            telemetry.update();

            // loop time throttling
            long sleep = (long)((1000d/HZ_LIMIT)-hz_timer.time());
            telemetry.addData("sleep",sleep);
            if(sleep>0)
              sleep(sleep);
            //while(hz_timer.time(TimeUnit.MILLISECONDS)<(1000d/HZ_LIMIT)){}
            telemetry.addData("hz",1000d/hz_timer.time(TimeUnit.MILLISECONDS));
        }
//        logger.close();
        while (!data.lines.isEmpty()){}
        try {
            data.closeServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isInThreshold(int curr,int target,int threshold){
        return Math.abs(target-curr)<threshold;
    }

}
