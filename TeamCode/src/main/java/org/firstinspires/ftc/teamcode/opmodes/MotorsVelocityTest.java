package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.control.DiffyMotors;

@TeleOp
@Config
public class MotorsVelocityTest extends LinearOpMode {
    DiffyMotors motors;
    public static int target1=0,target2=0;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motors = new DiffyMotors(hardwareMap);
        motors.setRunMode(DiffyMotors.RUN_MODE.VELOCITY_PID);
        waitForStart();

        while (opModeIsActive()) {
            motors.setOutputVelocityTargets(target1, target2);
            motors.update(telemetry);
            telemetry.update();
        }
    }
}
