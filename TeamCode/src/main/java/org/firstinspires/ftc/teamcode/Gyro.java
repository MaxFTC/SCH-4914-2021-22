package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.abs;

/* class designed to let you use the gyro. */

public class Gyro {

    FTCRobotConfig config;
    Telemetry   telemetry;
    BNO055IMU   imu;

    Orientation mLastAngles = new Orientation();
    double dGlobalAngle =0.0;

    double dCorrectionSensitivity = 0.0;
    double dCorrectionGain = 0.0;
    double dMaxCorrectionGain = 0.0;
    double dDesiredHeading = 0.0;

    public Gyro(FTCRobotConfig mPassedConfig, Telemetry mPassedTelemetry, HardwareMap mHardwareMap){
        config = mPassedConfig;
        telemetry = mPassedTelemetry;
        imu = mHardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = mHardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        loadConfig();
    }

    public void loadConfig(){
        dCorrectionSensitivity  = config.getDouble("gyro.CorrectionSensitivity",.3);
        dCorrectionGain         = config.getDouble("gyro.CorrectionGain",0.05);
        dMaxCorrectionGain      = config.getDouble("gyro.MaxCorrectionGain",0.75);
    }
    /**
     * Resets the cumulative angle tracking to zero.
     */

    public void resetAngle()
    {
        mLastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        dGlobalAngle = 0.0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    public double getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler mAngles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation mAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double dDeltaAngle = mAngles.firstAngle - mLastAngles.firstAngle;

        if (dDeltaAngle < -180)
            dDeltaAngle += 360;
        else if (dDeltaAngle > 180)
            dDeltaAngle -= 360;

        dGlobalAngle += dDeltaAngle;

        mLastAngles = mAngles;

        return dGlobalAngle;
    }


    /**
     * See if we are moving in a straight line and if not return a power correction value.
     * @return Power adjustment, + is adjust left - is adjust right.
     */
    public double checkDirection()
    {
        // The gain value determines how sensitive the correction is to direction changes.
        // You will have to experiment with your robot to get small smooth direction changes
        // to stay on a straight line.
        double correction, angle;

        angle = getAngle();

        if( abs(angle) <= dCorrectionSensitivity)
            correction = 0.0;             // no adjustment.
        else
            correction = -angle * dCorrectionGain; // reverse sign of angle for correction to push in opposite direction

        return correction;
    }

    ///////////////////////////////////////////////////////////////////////
    // Desired heading methods -- some are over loaded
    ///////////////////////////////////////////////////////////////////////

    public void setDesiredHeading(){
        setDesiredHeading(this.getAngle());                    // overloaded -- no heading passed use stored one
    }

    public void setDesiredHeading( double dNewHeading ){
        this.dDesiredHeading = dNewHeading;
    }

    public double steerToDesiredHeading(){
        return steerToDesiredHeading(this.dDesiredHeading);                    // overloaded -- no heading passed use stored one
    }

    public double steerToDesiredHeading( double dNewHeading ){          // use the passed heading but don't change default

        double dAngle = getAngle();
        double dDeltaAngle = dNewHeading - dAngle;
        if( abs(dDeltaAngle) <= dCorrectionSensitivity) // not too far out
            return 0.0;             // no adjustment.

        if( dDeltaAngle < 0.0 )
            return Math.min(abs(dDeltaAngle) * dCorrectionGain, dMaxCorrectionGain); // reverse sign of angle for correction to push in opposite direction
        else
            return -Math.min(abs(dDeltaAngle) * dCorrectionGain, dMaxCorrectionGain); // reverse sign of angle for correction to push in opposite direction
    }

}