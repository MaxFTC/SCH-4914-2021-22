package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/* Auton Class
	This is custom version of autonomous operation. As you can see it extends the StateMachine class so it will can take
    on all the StateMachine's charactistics. Usign this you can design up to 10 autonomous state machines to do run
    during the autonomous period for the robot.

 */


public class Auton extends MyStateMachine {


    private Telemetry telemetry = null;
    private Inputs inputs = null;
    private Sensors sensors = null;
    private RobotDriveBase base = null;
    private Gyro gyro = null;

    private String sDesc = "";

    // Auton class constructor. We are passing several other classes that we need access to get use their FBW values.
    // We are passing references for these classes.
    public Auton(Telemetry mPassedTelemetry,
                 Inputs mPassedInputs, Sensors mPassedSensors,
                 Gyro mPassedGyro, RobotDriveBase mPassedBase) {
        telemetry = mPassedTelemetry;
        inputs = mPassedInputs;            // notice we do not use new Inputs here because we are referencing the external Inputs class
        sensors = mPassedSensors;
        gyro = mPassedGyro;
        base = mPassedBase;
    }

    public void auto1() { // This overrides the auto1 method defined in the state machine class.
        String sAuton = "Auton1";
        switch (iStep) {                            // switch statement.
            // it will look at iStep and find the case where it should run code.
            // if iStep not found, it will go to default section at bottom.

            case 0:                                    // case 0 is where we can set up a bunch of stuff for the state machine.
                if (bStepFirstPass) {
                    sStepDescription = "Init";
                    sDesc = String.format("Step %d: %s...\n", iStep, sStepDescription);
                    telemetry.addData(sAuton, sDesc);
                    //System.out.printf("Step %d: %s...\n", iStep, sStepDescription  );
                }

                iStep++;                            // We are done with step, increment iStep

                break;                            // setup complete, jump out and go around again.

        } // end switch program block

    }

    public void auto2() {
        String sAuton = "Auton2";
        switch (iStep) {                            // switch statement.
            // it will look at iStep and find the case where it should run code.
            // if iStep not found, it will go to default section at bottom.

            case 0:                                    // case 0 is where we can set up a bunch of stuff for the state machine.
                if (bStepFirstPass) {
                    sStepDescription = "Init";
                    sDesc = String.format("Step %d: %s...\n", iStep, sStepDescription);
                    telemetry.addData(sAuton, sDesc);
                }

                iStep++;                                // We are done with step, increment iStep

                break;                                    // setup complete, jump out and go around again.

        }

    }
}
