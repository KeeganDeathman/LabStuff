package keegan.labstuff.util;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.relauncher.FMLRelaunchLog;

public class LSLog
{
    public static void info(String message)
    {
        FMLRelaunchLog.log("LabStuff", Level.INFO, message);
    }

    public static void severe(String message)
    {
        FMLRelaunchLog.log("LabStuff", Level.ERROR, message);
    }
    
    public static void debug(String message)
    {
    	FMLRelaunchLog.log("labstuff", Level.DEBUG, "Debug: " + message);
    }


	public static void exception(Exception e)
	{
		FMLRelaunchLog.log("LabStuff", Level.ERROR, e.getMessage());
	}
}