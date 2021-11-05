package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/* various routines used to apply power to different motors */

public class ApplyPower {
    

    private Telemetry telemetry;
    private int iDriveType = k_iTank;               // default is a tank drive but this can change.

    public static final int k_iTank = 0;
    public static final int k_iArchade = 1;
    public static final int k_iMecannum = 2;
    public static final int k_iZDrive = 3;

    public static final int k_iLeftFrontDrive = 1;
    public static final int k_iRightFrontDrive = 2;
    public static final int k_iLeftRearDrive = 3;
    public static final int k_iRightRearDrive = 4;

    public static final int k_iFrontZDrive = 1;
    public static final int k_iRearZDrive = 2;
    public static final int k_iLeftZDrive = 3;
    public static final int k_iRightZDrive = 4;

    public static final int k_iNormalEncoder = 0;
    public static final int k_iReverseEncoder = 1;


    private boolean  bTravelDistanceMotorReset = false;
    public boolean  bTravelDistanceComplete = false;

    public ApplyPower(Telemetry mPassedTelemetry){
        telemetry = mPassedTelemetry;
    }

    public double calcZWheelPower(int iWheel, double dPower, double dTurn, double dCrab ) {

        double dWheelPower = 0.0;

        // Calculate the the zdrive power and crab

        //        Front(1)
        //          ===
        //     ||          ||
        //Left ||          ||  Right
        // (3) ||          ||   (4)
        //          ===
        //        Rear (2)

        // Apply the drive power (front/back) and crab power (left/right)
        switch(iWheel){
            case k_iFrontZDrive:     // these make the robot go left and right so they get crab power
            case k_iRearZDrive:
                dWheelPower = dCrab; // in inputs decide with direct positive is vs. negative.
                break;

            case k_iLeftZDrive:      // these make the robot go forward and back so they get driver power
            case k_iRightZDrive:
                dWheelPower = dPower;
                break;

            default:
                // Tell user that the wheel is not valid.
                //telemetry.addData("ERROR: ApplyWheelPower: getWheelPower", "iWheel is not known.");
                //telemetry.update();
                return 0.0;
        }

        switch(iWheel){              // now add in the turn power
            case k_iFrontZDrive:
                dWheelPower += dTurn;
                break;

            case k_iRearZDrive:
                dWheelPower -= dTurn;
                break;

            case k_iLeftZDrive:      // these make the robot go forward and back so they get driver power
                dWheelPower += dTurn;
                break;

            case k_iRightZDrive:
                dWheelPower -= dTurn;
                break;

            default:
                // Tell user that the wheel is not valid.
                //telemetry.addData("ERROR: ApplyWheelPower: getWheelPower", "iWheel is not known.");
                //telemetry.update();
                return 0.0;
        }


        return dWheelPower;

    }


    public double calcTankWheelPower(int iDriveType, int iWheel, double dLeftPower, double dRightPower ) {

        // Calculate the archade power
        switch (iWheel) {
            case k_iLeftFrontDrive:
                return dLeftPower;
            case k_iRightFrontDrive:
                return dRightPower;
            case k_iLeftRearDrive:
                return dLeftPower;
            case k_iRightRearDrive:
                return dRightPower;

            default:
                // Tell user that the wheel is not valid.
                //telemetry.addData("ERROR: ApplyWheelPower: getWheelPower", "iWheel is not known.");
                //telemetry.update();
                return 0.0;
        }
    }


    public double calcWheelPower(int iDriveType, int iWheel, double dPower, double dTurn, double dCrab ) {

        double dWheelPower = 0.0;

        if (iDriveType != k_iMecannum && iDriveType != k_iArchade){
            // Tell user that the wheel is not valid.
            telemetry.addData("ERROR: ApplyWheelPower: getWheelPower", "iType is not mecannum or arcade.");
            //telemetry.update();
            return 0.0;
        }

        // Calculate the archade power
        switch(iWheel){
            case k_iLeftFrontDrive:
                    dWheelPower = dPower + dTurn;
                    break;
            case k_iRightFrontDrive:
                    dWheelPower = dPower - dTurn;
                    break;
            case k_iLeftRearDrive:
                    dWheelPower = dPower + dTurn;
                    break;
            case k_iRightRearDrive:
                    dWheelPower = dPower - dTurn;
                    break;

            default:
                    // Tell user that the wheel is not valid.
                    //telemetry.addData("ERROR: ApplyWheelPower: getWheelPower", "iWheel is not known.");
                    //telemetry.update();
                    return 0.0;

        }

        if( iDriveType == k_iArchade)
            return dWheelPower;


        // We are Mecannum so now apply the crab power
        switch( iWheel){
            case k_iLeftFrontDrive:
                dWheelPower += dCrab;
                break;
            case k_iRightFrontDrive:
                dWheelPower -= dCrab;
                break;
            case k_iLeftRearDrive:
                dWheelPower -= dCrab;
                break;
            case k_iRightRearDrive:
                dWheelPower += dCrab;
                break;
        }

        return dWheelPower;

    }

    public double getMecannumWheelPower(int iWheel, double dPower, double dTurn, double dCrab ) {
        return calcWheelPower(k_iMecannum, iWheel, dPower, dTurn, dCrab );
    }

    public double getArchadeWheelPower(int iWheel, double dPower, double dTurn ) {
        return calcWheelPower(k_iArchade, iWheel, dPower, dTurn, 0.0 );
    }


    public String runToPosition(DcMotor motor, double dPower, int iTargetPosition, int iMinDistance ) {
        String sStatus = "Full Speed";

        motor.setTargetPosition(iTargetPosition);
        int iDistance = abs(iTargetPosition - motor.getCurrentPosition());

        if( iDistance < 5 ) {
            //dPower = 0.0;
            sStatus = "Stop";
        }
        else if( iDistance < (iMinDistance/2) ){
            dPower *= .25;
            sStatus = "1/4 Speed";
        }
        else if( iDistance < iMinDistance ) {
            dPower *= .5;
            sStatus = "1/2 Speed";
        }

        motor.setPower(dPower);
        return sStatus;
    }

    public void resetTravelDistance(){
        bTravelDistanceMotorReset = false;
    }


    public double travelDistance(DcMotor motor, int iEncoderDirection, double dPower, int iTargetPosition, int iMinDistance ) {
        String sStatus = "Full Speed";

        if( bTravelDistanceMotorReset == false){                    // first time through see if we reset the encoders start to 0.
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  // reset the encoder to 0.
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);     // allow us to control the motor not the robot.
        }


        bTravelDistanceMotorReset = true;                           // set this to true so that we only do this once after it was false
                                                                    // true means it was reset. User must cann resetTravelDistance before using this method.

                                                                    // distance will be set to positive as forward and negative as reverse.
        iMinDistance = abs(iMinDistance);                           // force this to positive.

        int iCurrentPosition = motor.getCurrentPosition();          // get the current position, this will normally start as zero at the beginning of this process.

        if(iEncoderDirection == k_iReverseEncoder)              // allow us to tell the system to reverse the ecocoder value for what we think
            iCurrentPosition = -iCurrentPosition;               // as forward will be plus value from the encoder

        int iDistance = iTargetPosition - iCurrentPosition;     // calculate how far we have to go
                                                                // Problem: This works but assumes we will always be going forward. Must fix that.

        if( iDistance < 0)                                      // if distance is negative then we want reverse the power. Not really used.
            dPower = -abs(dPower);

        if( iDistance < 5 || bTravelDistanceComplete == true ) {  // did we go far enough?  If we did then we set power to 0.0. This indicates we are there.
            dPower = 0.0;
            sStatus = "Stop";
        }
        else if( iDistance < (iMinDistance/2) ){                  // if we are less that  1/4 way there slow down to 1/4 power.
            dPower *= .25;
            sStatus = "1/4 Speed";
        }
        else if( iDistance < iMinDistance ) {                     // if we are half way go to 1/2 power
            dPower *= .5;
            sStatus = "1/2 Speed";
        }

        telemetry.addLine( String.format("Travel STR: %-10s | DIST: %7d | CURP:  %7d ", sStatus, iDistance, iCurrentPosition));

        return dPower;
        //motor.setPower(dPower);
        //return sStatus;
    }


    public double runWithCalcPower(double dPower, int iTargetDistance, int iCurrentDistance, double dMinPower ) {

        int iMinDistance = iTargetDistance /5;
        int iFarDistance = iTargetDistance - iMinDistance;
        int iDistance = abs(iCurrentDistance);

        if( dPower < 0.0 && dMinPower > 0.0)
            dMinPower *= -1.0;

        if( iDistance > iTargetDistance){
            return 0.0;
        }
        else if( iDistance < (iMinDistance/2) ){
            dPower *= .25;
        }
        else if( iDistance < iMinDistance ) {
            dPower *= .5;
        }
        else if( iDistance > iFarDistance + (iMinDistance/2)  ) {
            dPower *= .5;
        }
        else if( iDistance > iFarDistance  ) {
            dPower *= .25;
        }

        if(dPower < dMinPower)
            dPower = dMinPower;

        return dPower;
    }


    public void MotorEncoder_Stop( DcMotor mMotor){
        mMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mMotor.setPower(0.0);
    };

    public void MotorEncoder_StopAndReset( DcMotor mMotor){
        mMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mMotor.setPower(0.0);
    };



    public void MotorEncoder_Run( DcMotor mMotor, double dPower, int iTargetPosition){

        if( dPower == abs(0.1) ) {
            mMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mMotor.setTargetPosition(0);
            mMotor.setPower(0.0);
            return;
        }

        if( mMotor.getTargetPosition() == iTargetPosition &&        // are want got the target position?
            mMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION &&  // are we in the mode to get there
                mMotor.isBusy() == true)                            // are we working now to get there
            return;

        mMotor.setTargetPosition(iTargetPosition);
        mMotor.setPower(dPower);
        mMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    };

    public void MotorEncoder_StationKeep( DcMotor mMotor, double dPower, int iTargetPosition){

        if( mMotor.getTargetPosition() == iTargetPosition &&        // are want got the target position?
                mMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION &&  // are we in the mode to get there
                mMotor.isBusy() == true)                            // are we working now to get there
            return;

        mMotor.setTargetPosition(iTargetPosition);
        mMotor.setPower(dPower);
        mMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    };

};