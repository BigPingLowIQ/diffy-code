package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.control.DiffyMotors;
import org.firstinspires.ftc.teamcode.control.SmartMotor;

@TeleOp(name="motor position test")
@Config
public class MotorPositionTest extends LinearOpMode {
    DiffyMotors motors;
    public static int target1=0,target2=0;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motors = new DiffyMotors(hardwareMap.get(DcMotorEx.class, "m2"),hardwareMap.get(DcMotorEx.class,"m1"));
        waitForStart();

        while(opModeIsActive()){
            motors.setOutputTargets(target1,target2);
            telemetry.addData("O1Target",motors.getOutputTargets()[0]);
            telemetry.addData("O1Current",motors.getOutputCurrents()[0]);
            telemetry.addData("O2Target",motors.getOutputTargets()[1]);
            telemetry.addData("O2Current",motors.getOutputCurrents()[1]);
            telemetry.addData("I1Target",motors.m1.getTarget());
            telemetry.addData("I1Current",motors.m1.getPosition());
            telemetry.addData("I2Target",motors.m2.getTarget());
            telemetry.addData("I2Current",motors.m2.getPosition());
            motors.update();
            telemetry.update();
        }
    }
}
