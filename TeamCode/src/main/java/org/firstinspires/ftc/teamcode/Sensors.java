package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

//import java.lang.Math.abs;

public class Sensors {

    Telemetry telemetry;
    RobotDriveBase base;
    Gyro gyro;


    public Sensors(Telemetry mPassedTelemetry, HardwareMap mHardwareMap, RobotDriveBase mPassedRobotDriveBase, Gyro mPassedGyro) {
        base = mPassedRobotDriveBase;
        telemetry = mPassedTelemetry;
        gyro = mPassedGyro;

    }

    //public void enableColorSensorLED( boolean bState ){
    //    i2cFrontColor.enableLed(bState);
    //}

    public void readValues() {

       //this.base.readValues();
    }

}