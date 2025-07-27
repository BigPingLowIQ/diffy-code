package org.firstinspires.ftc.teamcode.control;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;


@Config
public class DiffyMotors {
    // PTO DISENGAGED POS 0.3
    // PTO ENGAGED POS „
    public DcMotorEx m1,m2;
    public Encoder e1,e2;
    private int o1pos,o2pos;
    private int o1target,o2target;
    private double o1vel,o2vel;
    private double o1targetvel,o2targetvel;
    private PIDPair positionPids;
    private PIDPair velocityPids;
    public static double p=0.08,i=0,d=0;
    public static double pv=0,iv=0,dv=0;
    private RUN_MODE run_mode = RUN_MODE.POSITION_PID;
    private double[] powers = {0,0};
    public DiffyMotors(HardwareMap hm){
        this.m1 = new DcMotorImplEx(hm.get(DcMotorController.class,"Control Hub"),3);
        this.m2 = new DcMotorImplEx(hm.get(DcMotorController.class,"Control Hub"),0); // extendo
        this.e1 = new Encoder(m1);
        this.e2 = new Encoder(m2);
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        positionPids = new PIDPair(p,i,d);
        velocityPids = new PIDPair(pv,iv,dv);
        m2.setDirection(DcMotorSimple.Direction.REVERSE);
        o1pos = o2pos = o1target = o2target = 0;
        o1vel = o2vel = o1targetvel = o2targetvel = 0;
        e2.setDirection(Encoder.Direction.REVERSE);
    }

    public void update(Telemetry telemetry){
        positionPids.updateCoeffs(p,i,d);
        velocityPids.updateCoeffs(pv,iv,dv);

        if(run_mode == RUN_MODE.POSITION_PID) {
            o1pos = m1.getCurrentPosition()/(1<<9);
            o2pos = m2.getCurrentPosition()/(1<<9);

            int[] inputTargets = calculateDiffyInputs(o1target,o2target);
            int[] inputPositions = calculateDiffyInputs(o1pos,o2pos);

            int i1pos = inputPositions[0];
            int i2pos = inputPositions[1];
            int i1target = inputTargets[0];
            int i2target = inputTargets[1];

            powers = positionPids.calculatePIDS(i1target,i1pos,i2target,i2pos);
            m1.setPower(powers[0]);
            m2.setPower(powers[1]);

            telemetry.addData("i1target",i1target);
            telemetry.addData("i2target",i2target);
            telemetry.addData("i1pos",i1pos);
            telemetry.addData("i2pos",i2pos);
            telemetry.addData("power1", powers[0]);
            telemetry.addData("power2", powers[1]);

        }else if(run_mode == RUN_MODE.VELOCITY_PID){
            o1vel = e1.getCorrectedVelocity()/(1<<8);
            o2vel = e2.getCorrectedVelocity()/(1<<8);

            double[] inputTargetVelocities = calculateDiffyInputs(o1targetvel,o2targetvel);
            double[] inputVelocities = calculateDiffyInputs(o1vel,o2vel);

            double i1vel = inputVelocities[0];
            double i2vel = inputVelocities[1];
            double i1targetvel = inputTargetVelocities[0];
            double i2targetvel = inputTargetVelocities[1];

            double[] addPowers = velocityPids.calculatePIDS(i1targetvel,i1vel,i2targetvel,i2vel);
            powers[0]+= addPowers[0];
            powers[0] = Math.min(Math.max(powers[0],-1),1);
            powers[1]+= addPowers[1];
            powers[1] = Math.min(Math.max(powers[1],-1),1);
            m1.setPower(powers[0]);
            m2.setPower(powers[1]);

            telemetry.addData("o1vel",o1vel);
            telemetry.addData("o2vel",o2vel);
            telemetry.addData("i1targetvel",i1targetvel);
            telemetry.addData("i2targetvel",i2targetvel);
            telemetry.addData("i1vel",i1vel);
            telemetry.addData("i2vel",i2vel);
            telemetry.addData("power1", powers[0]);
            telemetry.addData("power2", powers[1]);
        }
    }

    public void setRunMode(RUN_MODE run_mode) {
        this.run_mode = run_mode;
    }

    private int[] calculateDiffyInputs(int o1, int o2){
        int i1 = o1+o2;
        int i2 = o1-o2;
        return new int[]{i1,i2};
    }
    private double[] calculateDiffyInputs(double o1,double o2){
        double i1 = o1+o2;
        double i2 = o1-o2;
        return new double[]{i1,i2};
    }

    public void setOutputTargets(int o1target,int o2target){
        this.o1target = o1target;
        this.o2target = o2target;
    }

    public void setOutputVelocityTargets(int o1target,int o2target){
        this.o1targetvel = o1target;
        this.o2targetvel = o2target;
    }
    public int[] getOutputTargets(){
        return new int[]{o1target, o2target};
    }
    public double[] getVelocities(){
        return new double[]{m1.getVelocity(),m2.getVelocity()};
    }
    public double[] getMotorsCurrents(){
        return new double[]{o1pos+o2pos,o1pos-o2pos};
    }

    public int[] getOutputCurrents(){
        return new int[]{o1pos,o2pos};
    }
}
