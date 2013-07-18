/*
 * project 		WhereTo
 * 
 * package 		libs
 * 
 * @author 		Rueben Anderson
 * 
 * date			Jul 11, 2013
 * 
 */
package libs;

import android.content.Context;

import com.randerson.classes.FileSystem;

public final class ApplicationDefaults {
	
	private static final String FILENAME = "path";
	
	public ApplicationDefaults() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean saveDefaults(UniArray data, Context context)
	{
		Boolean success = false;
		
		if (data.hasString(FILENAME))
		{
			// set the path string from the array object
			//String path = data.getString(FILENAME);
			
			// save the file
			FileSystem.writeObjectFile(context, data, "defaults", true);
		}
		
		return success;
	}
	
	public static Boolean loadDefaults()
	{
		Boolean success = false;
		
		return success;
	}
}
