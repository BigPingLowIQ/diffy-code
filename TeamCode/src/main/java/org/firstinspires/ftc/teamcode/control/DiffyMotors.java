package org.firstinspires.ftc.teamcode.control;

import com.qualcomm.robotcore.hardware.DcMotorEx;


public class DiffyMotors {
    public SmartMotor m1,m2;
    private int o1pos,o2pos;
    private int o1target,o2target;

    public DiffyMotors(DcMotorEx m1, DcMotorEx m2){
        this.m1 = new SmartMotor(m1,true);
        this.m2 = new SmartMotor(m2,false);
        o1pos = o2pos = o1target = o2target = 0;
    }

    public void update(){
        m1.update();
        m2.update();
    }

    public void setOutputTargets(int o1target,int o2target){
        this.o1target = o1target;
        this.o2target = o2target;
        m1.setTarget((o1target+o2target)/2);
        m2.setTarget((o1target-o2target)/2);
    }

    public int[] getOutputTargets(){
        return new int[]{o1target, o2target};
    }
    public double[] getVelocities(){
        return new double[]{m1.getVelocity(),m2.getVelocity()};
    }
    public double[] getMotorsCurrents(){
        return new double[]{m1.getCurrent(),m2.getCurrent()};
    }

    public int[] getOutputCurrents(){
        return new int[]{m1.getPosition()+m2.getPosition(),m1.getPosition()- m2.getPosition()};
    }
}
