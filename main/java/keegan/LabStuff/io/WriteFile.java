package keegan.labstuff.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteFile 
{
	public void write(File file, String string, boolean noClear)
	{
		BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            // This will output the full path where the file will be written to...
            System.out.println(file.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(file, noClear));
            writer.append(string);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}
}
