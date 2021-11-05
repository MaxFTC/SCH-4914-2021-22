package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotArm {

    private DcMotor motElevator = null;
    private DcMotor motExtender = null;

    private FTCRobotConfig config = null;
    private Telemetry telemetry = null;
    private ApplyPower applyPower = null;
    private Gyro gyro = null;


    public RobotArm(Telemetry mPassedTelemetry, HardwareMap hardwareMap) {

        telemetry = mPassedTelemetry;
        applyPower = new ApplyPower(mPassedTelemetry);

        //loadConfig();   // Load the configs if there are any

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        motElevator = hardwareMap.get(DcMotor.class, "FLM");
        motExtender = hardwareMap.get(DcMotor.class, "FRM");

        motElevator.setDirection(DcMotor.Direction.REVERSE);
        motExtender.setDirection(DcMotor.Direction.REVERSE);
        //motLeftRearMotor.setDirection(DcMotor.Direction.REVERSE);
        //motRightRearMotor.setDirection(DcMotor.Direction.FORWARD);

        motElevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }


    public void updateValues(Inputs inputs) {

        double dElevatorPower = 0.0;
        double dExtenderPower = 0.0;

        dElevatorPower = inputs.dElevatorPower;
        dExtenderPower = inputs.dExtenderPower;

        this.motElevator.setPower(dElevatorPower);
        this.motExtender.setPower(dExtenderPower);

    }



}
