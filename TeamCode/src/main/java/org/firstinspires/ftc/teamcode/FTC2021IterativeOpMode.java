/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;

/** This is the main robot task.
 *
 */

@TeleOp(name="FTC2021: SCH 4191", group="Iterative Teleop")
//@Autonomous(name="FTC2019: Autonomous", group="Iterative Autonomous")
//@Disabled
public class FTC2021IterativeOpMode extends OpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Inputs inputs = null;
    private RobotDriveBase base = null;
    private RobotArm arm = null;

    private Gyro gyro = null;
    private Sensors sensors = null;
    private Auton auton = null;

    public String sCodeVersion = "2021/10/29 7:46PM";

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Robot_Iterative", "Start Initialize");

        // Make sure the config folder exists on the Robot Controller phone
        //String directoryName = "/storage/emulated/0/FIRST/MyStuff";
        //File directory = new File(String.valueOf(directoryName));

        //if(!directory.exists()){        // test to see if it exists.
        // directory.mkdir();          // if it does not exist, create it
        //}

        // Create all out custom classes we will use in this robot.
        // Doign this causes it to call the class constructor. We have to make sure we pass a  the right stuff for it to work.
        inputs          = new Inputs(telemetry, gamepad1, gamepad2);
        gyro            = new Gyro (MySettings.config, telemetry,hardwareMap);
        sensors         = new Sensors(telemetry, hardwareMap, base, gyro);
        auton           = new Auton(telemetry, inputs, sensors, gyro, base);
        base            = new RobotDriveBase(telemetry, hardwareMap);
        arm             = new RobotArm(telemetry, hardwareMap);

        gyro.loadConfig();

        telemetry.update();

    }


    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     * We can use this to test out joysticks and check other stuff
     */
    @Override
    public void init_loop() {
        //telemetry.addData("Status", "Run Time: " + runtime.toString() + " (" + sCodeVersion + ")");

        telemetry.addData("Config", MySettings.config.sLoadMesssage);

        inputs.readValues();
        //base.readValues();
        telemetry.update();
    }


    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();            // reset the elapsed time
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     * THis is the meat of the robot while running.
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Run Time: " + runtime.toString() + " (" + sCodeVersion + ")");

        inputs.readValues();            // read out inputs into variables
        sensors.readValues();           // read our sensors into variables. (other may want to use them)
        base.updateValues(inputs);      // send out inputs to the base for it to use them.
        arm.updateValues(inputs);      // send out inputs to the base for it to use them.
        telemetry.update();             // print any telemetry to the screen

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        inputs.zeroValues();
        base.updateValues(inputs);

        telemetry.addData("Program Stopped", runtime.toString());
        telemetry.update();

    }

}
