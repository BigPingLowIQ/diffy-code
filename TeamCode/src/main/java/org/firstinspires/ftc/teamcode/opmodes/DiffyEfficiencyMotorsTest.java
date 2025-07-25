package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.control.DiffyEfficiencyMotors;
import org.firstinspires.ftc.teamcode.control.DiffyMotors;


@Config
@TeleOp
public class DiffyEfficiencyMotorsTest extends LinearOpMode {
    public DiffyEfficiencyMotors motors;
    public static int targetPos=0;
    public static double targetVelocity=0;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motors = new DiffyEfficiencyMotors(hardwareMap);
        waitForStart();

        while(opModeIsActive()){
            motors.setTargetPosition(targetPos);
            motors.setTargetVelocity(targetVelocity);


            motors.update(telemetry);
            telemetry.update();
        }


    }
}
