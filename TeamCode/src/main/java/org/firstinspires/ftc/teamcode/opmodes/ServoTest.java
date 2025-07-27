package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoImpl;

//@Config
@TeleOp(group="zz")
@Config
public class ServoTest extends OpMode {
    Servo servo;
    DcMotorEx motor;

    public static double position = 0.5;
    public static double power = 0;

    @Override
    public void init() {
        servo = new ServoImpl(hardwareMap.get(ServoController.class,"Control Hub"),0);
        motor = new DcMotorImplEx(hardwareMap.get(DcMotorControllerEx.class, "Control Hub"),0);
    }

    @Override
    public void loop() {
        servo.setPosition(position);
        motor.setPower(power);
    }
}