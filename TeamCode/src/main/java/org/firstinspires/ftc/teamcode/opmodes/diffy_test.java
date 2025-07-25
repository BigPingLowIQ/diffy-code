package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoImpl;

@TeleOp
public class diffy_test extends LinearOpMode {
    DcMotor m1;
    DcMotor m2;
    Servo servo;
    int running_mode = 0;
    double servo_pos = 0.62;

    @Override
    public void runOpMode(){
        DcMotorController m = hardwareMap.get(DcMotorController.class, "Control Hub");
        ServoController s = hardwareMap.get(ServoController.class, "Control Hub");
        m1 = new DcMotorImpl(m, 1);
        m2 = new DcMotorImpl(m, 0);
        servo = new ServoImpl(s, 1);
        servo.setPosition(0.62);

        waitForStart();
        while(opModeIsActive()){
            // aici bagi codul
//            if(gamepad1.a) running_mode = 1;
//            if(gamepad1.b) running_mode = 2;
            if(gamepad1.triangle) running_mode = 3;
            if(gamepad1.circle) running_mode = 4;
            if(gamepad1.start) running_mode = 0;
//            if(gamepad1.dpad_down) running_mode = 5;
            if(gamepad1.dpad_left)servo_pos=0.42;
            if(gamepad1.dpad_right)servo_pos=0.62;

            switch(running_mode){
                case 0:
                    set_output_power(0,0,1);
                    break;
                case 3:
                    set_output_power(gamepad1.right_stick_y, gamepad1.left_stick_y,2);
                    break;
                case 4:
                    set_output_power(gamepad1.right_stick_y, gamepad1.left_stick_y,1);
                    break;
            }

            servo.setPosition(servo_pos);
        }
    }

    public void set_output_power(double o1, double o2,double LIMIT){
        o1 = o1/LIMIT;
        o2 = o2/LIMIT;
        double denominator = Math.max(Math.abs(o1)+Math.abs(o2),1);
        m1.setPower((o1+o2)/denominator);
        m2.setPower((o1-o2)/denominator);
    }
}
