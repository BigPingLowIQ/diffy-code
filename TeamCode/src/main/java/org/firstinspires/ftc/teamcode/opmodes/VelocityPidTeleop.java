package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.control.PIDController;

@TeleOp
@Config
public class VelocityPidTeleop extends LinearOpMode {
    public static double p=0.000008,i=0.00000001,d=0;
    public static int targetVel = 2000; // ticks/s
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DcMotorEx m1 = hardwareMap.get(DcMotorEx.class, "m1");
        DcMotorEx m2 = hardwareMap.get(DcMotorEx.class, "m2");
        PIDController pid = new PIDController(p,i,d);

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        m1.setDirection(DcMotorSimple.Direction.REVERSE);

        double sum = 0;
        int count = 0;
        double power=0;

        waitForStart();

        while(opModeIsActive()){
            pid.kp = p;
            pid.ki = i;
            pid.kd = d;

            double currentVel = -m1.getVelocity();
            power+= Math.min(1,Math.max(pid.calculate(currentVel,targetVel),-1));

            sum+=currentVel;
            count++;

            m1.setPower(power);
            telemetry.addData("Power",power);
            telemetry.addData("Target Velocity", targetVel);
            telemetry.addData("Current Velocity", currentVel);
            telemetry.addData("Mean Velocity",sum/count);
            telemetry.update();
        }


    }
}
