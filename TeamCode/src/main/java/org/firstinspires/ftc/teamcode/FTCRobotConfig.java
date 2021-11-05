
/****************************************************************************************************
 * General purpose tool to allow FTC users have variable configs saved in a file on the robot.
 * This you can have all the fields you want in the file set as comments uing a # symbol.
 * Then when you need to use them you just uncomment.
 * This is a derivative of the same code for FRC.
 *
 *<p>
 *<pre>
 * Changes ********************************************************************************************
 * Date          Ver.    BY      Description
 * Oct. 15, 2018 1.00    FWL     Initial FTC submission
 *</pre>
 *<p>			 
 * @author Frank Larkin, FRC272 - Lansdale Catholic Robotics  pafwl@aol.com
 * @version 1.00
 *
 */

/************************
 * Class Constructor. You pass in the directory and file name.
 * <p>
 * The config file uses a # symbol in the first position to indicate comment. The actual data lines have 2 required parts.
 * The key is how you refer to your setting in code and the value is a text representation of the data. 
 * These are pipe separated. You can have as much white space as you like for readability.
 * The last pipe separated field is any comments. This is ignored when read.
 *
 * Below is an example...
 * <pre>
 * inputs.dp_driverPowerReduction      | 1.0       | if set to .5 driver can only run at .5 power.
 * shooter.ip_ClosePosition 	       | 100       | extra stuff
 * shooter.ip_OnTargetPosition         | 
 * shooter.dp_FastUpPower 		       | 30        | more extra stuff
 * shooter.dp_FastDownPower 	       | .5
 * shooter.dp_SlowUpPower		       | .2
 * shooter.dp_SlowDownPower 	       | .2
 * shooter.bp_AllowArmToMove           | false
 *
 * #Camera stuff
 * #D_PickupPanPosition                |
 * #D_PickupTiltPosition               |
 * #D_LoweredPanPosition               |
 * #D_LoweredTiltPosition              |
 * #D_DrivingPanPosition               |
 *
 * #Target
 * #I_FovPixelsX                       |
 * #I_FovPixelsY                       |
 *
 * </pre>
 *
 * Run your code once so the folder is created. Suggested folder: /storage/emulated/0/FIRST/MyStuff
 * The folder is as sub folder of the FIRST folder. I do not recommend you put it in any of their folders.
 *
 * You should get a copy of an Android File Manager and load it on your robot controller phone.
 * Then is is very easy to make and save changes on the robot and in your code.
 * You can use the phone connected to you PC to copy in the initial file from you PC.
 * To do that you have to go to USB control on the phone and enable loading Media files.
 * Then copy the file to the phone in the folder indicated.   Google It.
 * <p>
 * This code will automatically try to read the config file when it is constructed. (initialized)
 * To make changes, stop the code, change the config file, save the changes and restart the code.
 *
 * See examples below how to use it ...
 * <pre>
 * public class TestOPMode extends OpMode {
 *
 *   LCFTCConfig config = null;
 *   Inputs inputs = null;
 *
 *     public void init() {   // or initloop when using Iterative Robot
 *         telemetry.addData("Robot_Iterative", "Start Initialize");
 *
 *         String directoryName = "/storage/emulated/0/FIRST/MyStuff";
 *         File directory = new File(String.valueOf(directoryName));
 *
 *        if(!directory.exists()){
 *              directory.mkdir();
 *        }
 *
 *        config = new LCFTCConfig(String.valueOf(directoryName) + "/LCConfig.txt"); // name yours what you want.
 *        inputs = new Inputs(config, telemetry, hardwareMap, gamepad1, gamepad2);   //note passing config to other classes
 *        robotdrivebase = new RobotDriveBase(config, telemetry, hardwareMap);
 *     }

 *
 *    // In other classes like inputs
 *    public Inputs(LCFTCConfig mPassedConfig, Telemetry mPassedTelemetry,  HardwareMap mPassedHwMap, Gamepad mPassedDriver, Gamepad mPassedOperator ) {
 *        config = mPassedConfig;
 *        telemetry = mPassedTelemetry;
 *        mLocalHwMap  = mPassedHwMap;
 *        driverGamepad = mPassedDriver;
 *        operatorGamepad = mPassedOperator;
 *
 *       dpDriverPowerReduction = config.getInt("driver.reductionPower", 1.0);  // default not reduction
 *
 *    }
 *
 * Other usage examples
 *      parameters and key name as is in file, default if not found.           // when commented out default is returned
 *      ip_WallDistance = config.getInt("auton.ip_WallDistance", 523);         //ip= int parameter (not required)
 *      dp_FastDownPower = config.getDouble("shooter.dp_FastDownPower", 0.5);  //dp= double parameter (not required)
 *      bp_AllowArmUp = config.getBoolean("shooter.dp_FastDownPower", false);  //bp = boolean parameter (not required)
 *
 * Logging
 * Messages are written out the robot log file called robotControllerLog.txt. Download the file to your PC. Google it.
 * The scan for the string "Config." As the day goes on you will have a large file. Your latest stuff will be at the bottom.
 *
 * Examples:
 * 11-02 23:03:23.558 17412 17547 I System.out: Config.load(): INFO: Loading data from file [/storage/emulated/0/FIRST/MyStuff/LCConfig.txt]
 * 11-02 23:03:23.562 17412 17547 I System.out: Config.load(): INFO: Line=18  Key=[tower.dpGrabberHuntPos  Value=[.25]
 * 11-02 23:03:23.563 17412 17547 I System.out: Config.load(): INFO: Line=23  Key=[Tele_FileName  Value=[FRC272_HH_Telemerty]
 * 11-02 23:03:23.563 17412 17547 I System.out: Config.load(): INFO: Line=25  Key=[Tele_TimestampFile  Value=[true]
 * 11-02 23:03:23.676 17412 17547 V RobotCore: addr=false data=true arb=false clock=false
 * 11-02 23:03:23.677 17412 17547 E LynxI2cDeviceSynch: readStatusQuery: cbExpected=1 cbRead=0
 * 11-02 23:03:23.677 17412 17547 E LynxI2cDeviceSynch: placeholder: readStatusQuery
 * 11-02 23:03:24.570 17412 17547 I System.out: Config.getDouble(): INFO tower.dpGrabberHuntPos set to .25
 * 11-02 23:03:24.737 17412 17547 V RobotCore: addr=false data=true arb=false clock=false
 * 11-02 23:03:24.738 17412 17547 E LynxI2cDeviceSynch: readStatusQuery: cbExpected=1 cbRead=0

 */


package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FTCRobotConfig {

    private Map<String, String> dictColumnData; /** This is a string key, value map. You put in the key and get the value. */
    private String sDirectoryName = null;
    private String sFileName = null;
    public String sLoadMesssage = null;
    public File mDirectory = null;

    public FTCRobotConfig(String s_PassedDirectory, String s_PassedFileName){
        this.sDirectoryName = s_PassedDirectory;
        this.sFileName = s_PassedFileName;
        dictColumnData = new HashMap<String, String>();	// when we load we want to reload this too to clear out the old data
        load();
    }
    //public void setup(Telemetry mPassedTelemetry) {
    //    telemetry = mPassedTelemetry;
    //}
    
    /************************
     * Used to open the config file and read in the variables. Everything is saved in a hash table (key value pair).
     * While reading anything that starts with a # will be ignored as well as blank lines even if filled with whitespace.
     * Message are sent to the console if you have it enabled so you can see problems in reading teh file.    
     */
    public void load() {

        String sPathName = this.sDirectoryName + '/' + this.sFileName;
        File mDirectory = new File(String.valueOf(sDirectoryName));

        if(!mDirectory.exists()){        // test to see if it exists. The leading ! means NOT.
            mDirectory.mkdir();          // if it does not exist, create it
        }

        System.out.println("Config.load(): INFO: Loading data from file [" + sPathName + "]");
        dictColumnData.clear(); // when we load we want to clear out the old data

        try{

            BufferedReader in = new BufferedReader(new FileReader(sPathName));
            String str;
            Integer line = 0;
            while ((str = in.readLine()) != null){

                line++;
                str = str.trim();

                if(str.length() == 0 || str.charAt(0) == '#')       // blank line
                    continue;

                // remove all the double spaces leaving only single spaces.
                str = str.replace("    "," ").replace("    "," ").replace("  "," ");
                str = str.replace("    "," ").replace("    "," ").replace("  "," ");


                // break it up 
                String[] fields = str.split("\\|");  // break up line read on pipes.


                if (fields.length < 2){
                    System.out.println("Config.load(): ****ERROR Line " + line.toString() + " is not a comment (#) but does not have a pipe (|) delimiter!");
                    continue;
                }

                String key = fields[0].trim();
                String value = fields[1].trim();

                if( key.length() == 0){
                    System.out.println("Config.load(): *WARN: Line " + line.toString() + " Key has no data in it, zero length, ignoring!!!");
                    continue;
                }

                if( value.length() == 0){
                    System.out.println("Config.load(): *WARN: Line " + line.toString() + " Value has no data in it, zero length, ignoring!!!");
                    continue;
                }

                System.out.println("Config.load(): INFO: Line=" + line.toString() + "  Key=[" + key + "  Value=[" + value + "]"  );
                dictColumnData.put(key,value);			// save the data 

            }

            in.close();

            sLoadMesssage = "Config.load: Load successful...";

        } catch (IOException e) {
            //System.out.println(
            //sLoadMesssage =  "Config.load(): ****ERROR: Failed to load the file " + this.s_FileName +
            //       "   Exception:" + e + "  Reason:" + e.getMessage() );
            sLoadMesssage =  "Config.load():  Exception:" + e.getMessage();
        }

    }


    private String getValue( String key ){

        if(dictColumnData.containsKey(key))
            return dictColumnData.get(key);
        else
            return null;
    }

    /************************
     * Used to pull an int value from the config file.     
     * You can monitor the log to see if you values was pulled correctly.
     */
    public int getInt( String key, int defaultValue){

        String sValue = getValue(key);

        if( sValue == null )
            return defaultValue;

        try{
            double dValue = Double.parseDouble(sValue);  // read as double first in case there is decimal
            int retValue = (int) dValue;									      // now convert to int
            System.out.println("Config getInt: INFO " + key +  " set to " + String.valueOf(retValue));
            return retValue;
        } catch( Exception e ) {
            System.out.println("Config.getInt(): ***ERROR converting saved string value to int! Key=" + key +  "  Value String=[" + sValue + "]" +
                    "   Exception:" + e + "  Reason:" + e.getMessage() );
            return defaultValue;
        }

    }


    /************************
     * Used to pull a double value from the config file.     
     * You can monitor the console to see if you values was pulled correctly.      
     */
    public double getDouble( String key, double defaultValue){

        String sValue = getValue(key);

        if( sValue == null )
            return defaultValue;

        try{
            double retValue = Double.parseDouble(sValue);
            System.out.println("Config.getDouble(): INFO " + key +  " set to " + sValue);
            return retValue;
        } catch( Exception e ) {
            System.out.println("Config.getDouble(): ***ERROR converting saved string value to double! Key=" + key +  "  Value String=[" + sValue + "]" +
                    "   Exception:" + e + "  Reason:" + e.getMessage() );
            return defaultValue;
        }

    }


    /************************
     * Used to pull a string value from the config file.     
     * You can monitor the console to see if you values was pulled correctly.      
     */
    public String getString( String key, String defaultValue){

        String sValue = getValue(key);

        if( sValue == null )
            return defaultValue;

        System.out.println("Config.getString(): INFO " + key +  " set to " + sValue);
        return sValue;

    }

    /************************
     * Used to pull a boolean (true,false) value from the config file.
     * You can monitor the console to see if you values was pulled correctly.      
     */
    public boolean getBoolean( String key, boolean defaultValue){

        String sReturnValue = getValue(key);
        String sValue = "";

        if( sReturnValue == null)
            sValue = "null";
        else
            sValue = sReturnValue.toLowerCase();

        if( sValue.equalsIgnoreCase("true")){
            System.out.println("Config.getBoolean(): INFO " + key +  " set to true");
            return true;
        } else if( sValue.equalsIgnoreCase("false")) {
            System.out.println("Config.getBoolean(): INFO " + key +  " set to false");
            return false;
        }else {
            System.out.println("Config.getBoolean(): ****ERROR: Key=" + key +  "  Value String=[" + sValue + "]" +
                    "  Not true or false, returning default value." );
            return defaultValue;
        }
    }

}