package org.firstinspires.ftc.teamcode.control;

public class PIDPair {
    private PIDController pid1;
    private PIDController pid2;
    public PIDPair(double p,double i,double d){
        pid1 = new PIDController(p,i,d);
        pid2 = new PIDController(p,i,d);
        pid1.kp = p;
        pid2.kp = p;
        pid1.ki = i;
        pid2.ki = i;
        pid1.kd = d;
        pid2.kd = d;
    }

    public void updateCoeffs(double p, double i,double d){
        pid1.kp = p;
        pid2.kp = p;
        pid1.ki = i;
        pid2.ki = i;
        pid1.kd = d;
        pid2.kd = d;
    }

    public double[] calculatePIDS(int target1,int pos1,int target2,int pos2){
        return new double[]{pid1.calculate(pos1,target1),pid2.calculate(pos2,target2)};
    }
    public double[] calculatePIDS(double target1,double pos1,double target2,double pos2){
        return new double[]{pid1.calculate(pos1,target1),pid2.calculate(pos2,target2)};
    }

}
