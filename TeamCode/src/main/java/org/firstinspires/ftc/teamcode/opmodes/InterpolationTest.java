package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.control.TimeInterpolation;

@TeleOp
@Config
public class InterpolationTest extends LinearOpMode {
    TimeInterpolation up;
    public static int target1start=0,target1end=2000,target2=0;
    public static double speed_mm_per_sec = 20; // mm/s

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        up = new TimeInterpolation(target1end, speed_mm_per_sec);
        waitForStart();
        up.start();
        while(opModeIsActive()){
            telemetry.addData("position",up.getPosition());
            telemetry.addData("isInterpolating",up.isInterpolating());
            telemetry.addData("duration",up.getDuration());
            telemetry.addData("speed",up.getSpeed());
            telemetry.update();
        }
    }
}
