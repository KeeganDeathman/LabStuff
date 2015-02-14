package keegan.labstuff.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile 
{
	public String[] readLines(File file) throws IOException 
	 {
	        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
	        {
	        	List<String> lines = new ArrayList<String>();
	        	String line = null;
	        	while ((line = bufferedReader.readLine()) != null) {
	        		lines.add(line);
	        	}
	        	bufferedReader.close();
	        	return lines.toArray(new String[lines.size()]);
	        }
	    }
}
