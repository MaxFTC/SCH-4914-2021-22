package org.firstinspires.ftc.teamcode;

import java.io.File;

public class MySettings {

    static final String sDirectoryName = "/storage/emulated/0/FIRST/MyStuff";
    public static FTCRobotConfig config = new FTCRobotConfig(sDirectoryName , "SCH_4914_Config.txt");

    public static final double input_dDriverPowerPct = config.getDouble("input_dDriverPowerPct",1.0);

    public static final double arm_iElevatorLevel0 = config.getDouble("arm_iElevatorLevel0", 0);
    public static final double arm_iElevatorLevel1 = config.getDouble("arm_iElevatorLevel1",200);
    public static final double arm_iElevatorLevel2 = config.getDouble("arm_iElevatorLevel2",400);
    public static final double arm_iElevatorLevel3 = config.getDouble("arm_iElevatorLevel3",600);
    public static final double arm_iElevatorLevel4 = config.getDouble("arm_iElevatorLevel4",800);



}
