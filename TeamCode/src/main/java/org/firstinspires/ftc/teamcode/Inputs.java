package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Math.abs;

/* class to manage all your human an other inputs. */

public class Inputs {


    // Define all your variables here.
    public boolean bPadA_a = false;
    public boolean bPadA_b = false;
    public boolean bPadA_x = false;
    public boolean bPadA_y = false;

    public double dLeftDriverPower = 0.0;
    public double dLeftDriverTurn = 0.0;
    public double dElevatorPower = 0.0;
    public double dExtenderPower = 0.0;
    //public double dDriverCrabPower = 0.0;

    private Telemetry telemetry = null;
    private HardwareMap mLocalHwMap  = null;
    public Gamepad driverGamepad = null;
    public Gamepad operatorGamepad = null;

    public  boolean bReverseControls = false;

    public  boolean bFineControl = false;


    public Inputs(Telemetry mPassedTelemetry,Gamepad mPassedDriver, Gamepad mPassedOperator ) {

        telemetry       = mPassedTelemetry;
        driverGamepad   = mPassedDriver;
        operatorGamepad = mPassedOperator;

        loadConfig();   // load the input configuration values

    }

    private double getOverrideValue(double dInputValue, double dInputValueOverride, double dLimit){

        if( abs(dInputValueOverride) > dLimit)   // operator o
            return dInputValueOverride;
        else
            return dInputValue;

    }

    public void loadConfig(){
        //dPowerReductionFactor = config.getDouble("input.dPowerReductionFactor",1.0);

    }

    public void readValues() {

        /////////////////////////////////////////////////////////////////
        // Driver controls
        /////////////////////////////////////////////////////////////////

        dLeftDriverPower = -this.driverGamepad.left_stick_y;  // - makes forward 1.0, reverse -1.0
        dLeftDriverTurn = this.driverGamepad.left_stick_x;      // left will be - right will be +

        dLeftDriverPower= getQuadPower(dLeftDriverPower);       // desensiti`ze the power
        dLeftDriverTurn= getQuadPower(dLeftDriverTurn);       // desensiti`ze the power

        if(driverGamepad.left_bumper == true){
            dLeftDriverPower = -dLeftDriverPower;
            dLeftDriverTurn = -dLeftDriverTurn;
        }

        /////////////////////////////////////////////////////////////////
        // Operator controls
        /////////////////////////////////////////////////////////////////

        dElevatorPower = -this.operatorGamepad.left_stick_y;    // - makes forward 1.0, reverse -1.0
        dExtenderPower = -this.driverGamepad.right_stick_y;
        //bFire = operatorGamepad.a;


        telemetry.addLine( String.format("dElevatorPower: %-1.2f ", dElevatorPower));
        telemetry.addLine( String.format("dExtenderPower: %-1.2f ", dExtenderPower));

        dElevatorPower = getCubePower(dElevatorPower);       // desensiti`ze the power
        dExtenderPower = getCubePower(dExtenderPower);       // desensiti`ze the power


        //telemetry.addLine( String.format("IDR: %- 4.2f | %- 4.2f --- %- 4.2f | %- 4.2f | OR: %s ",
        //        dDriverLeftPower, dDriverTurnPower,dDriverRightPower, dDriverCrabPower, sOrientation));

    }

    // This is used in autonomous to zero out all input variables
    public void zeroValues() {

    };

    public double getCubePower(double dInPower){
        return dInPower * abs(dInPower * dInPower);
    }

    public double getSquarePower(double dInPower){
        return dInPower * abs( dInPower ) ;
    }

    public double getQuadPower(double dInPower){
        return dInPower * abs(dInPower * dInPower * dInPower);
    }

}


