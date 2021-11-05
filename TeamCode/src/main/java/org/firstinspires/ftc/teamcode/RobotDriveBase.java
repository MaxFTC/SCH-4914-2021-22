package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotDriveBase {

    private DcMotor motLeftFrontMotor = null;
    private DcMotor motRightFrontMotor = null;
    //private DcMotor motLeftRearMotor = null;
    // private DcMotor motRightRearMotor = null;


    private FTCRobotConfig config = null;
    private Telemetry telemetry = null;
    private ApplyPower applyPower = null;
    private Gyro       gyro = null;

    public RobotDriveBase(Telemetry mPassedTelemetry, HardwareMap hardwareMap) {

        telemetry =  mPassedTelemetry;
        applyPower = new ApplyPower(mPassedTelemetry);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        motLeftFrontMotor = hardwareMap.get(DcMotor.class, "FLM");
        motRightFrontMotor = hardwareMap.get(DcMotor.class, "FRM");

        motLeftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        motRightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        //motLeftRearMotor.setDirection(DcMotor.Direction.REVERSE);
        //motRightRearMotor.setDirection(DcMotor.Direction.FORWARD);

        motLeftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motRightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Tell the driver that initialization is complete.
        telemetry.addData("RobotBase", "Initialize Complete");
    }

    public void readValues() {
        //iLeftFrontMotorCurrentPosition = this.motLeftFrontMotor.getCurrentPosition();
        //iLeftFrontMotorTargetPosition = this.motLeftFrontMotor.getTargetPosition();
        ///dLeftFrontMotorPower = this.motLeftFrontMotor.getPower();
    }

    public void setBaseDriveEncorerToZero(){
        //this.iLastResetTicks = motLeftFrontMotor.getCurrentPosition();
        //iBaseDriveEncoderCalcPosition = motLeftFrontMotor.getCurrentPosition() - this.iLastResetTicks;

    }

    public void updateValues(Inputs inputs) {

        double dLeftFrontMotorPower = 0.0;
        double dRightFrontMotorPower = 0.0;

        //telemetry.addLine( String.format("INP: %- 4.2f | %- 4.2f | %- 4.2f ",
                        //inputs.dDriverPower,inputs.dDriverCrab,inputs.dDriverTurn));
        telemetry.addLine( String.format("Base:dLeftDriverPower: %-1.2f ", inputs.dLeftDriverPower));
        telemetry.addLine( String.format("Base:dLeftDriverTurn: %-1.2f ", inputs.dLeftDriverTurn));


        dLeftFrontMotorPower = applyPower.getArchadeWheelPower( ApplyPower.k_iLeftFrontDrive, inputs.dLeftDriverPower, inputs.dLeftDriverTurn);
        dRightFrontMotorPower = applyPower.getArchadeWheelPower(ApplyPower.k_iRightFrontDrive, inputs.dLeftDriverPower,inputs.dLeftDriverTurn);

        telemetry.addLine( String.format("Base:dLeftFrontMotorPower: %-1.2f ", dLeftFrontMotorPower));
        telemetry.addLine( String.format("Base:dRightFrontMotorPower: %-1.2f ", dRightFrontMotorPower));

        this.motLeftFrontMotor.setPower( dLeftFrontMotorPower );
        this.motRightFrontMotor.setPower( dRightFrontMotorPower );

    }
}
