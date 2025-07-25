package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
@Config
public class EncoderTest extends LinearOpMode {
    public static double power1=0,power2=0;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DcMotorEx m1 = hardwareMap.get(DcMotorEx.class,"m1");
        DcMotorEx m2 = hardwareMap.get(DcMotorEx.class,"m2");
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();
        while(opModeIsActive()){
            m1.setPower(power1);
            m2.setPower(power2);
            telemetry.addData("m1",m1.getCurrentPosition());
            telemetry.addData("m2",m2.getCurrentPosition());
            telemetry.addData("m1-vel",m1.getVelocity());
            telemetry.addData("m2-vel",m2.getVelocity());
            telemetry.update();
        }
    }
}
