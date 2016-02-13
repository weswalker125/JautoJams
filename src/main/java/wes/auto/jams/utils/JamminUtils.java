package wes.auto.jams.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import wes.auto.jams.models.SimpleSong;

public class JamminUtils {
	private static Gson gson = new Gson();
	public static void Print(List<SimpleSong> songs) {
		System.out.println(gson.toJson(songs));
	}
	
	public static boolean moveFile(String sourceStr, String destinationStr) {
		boolean ret = false;
		
		File source, destination;
		source = new File(sourceStr);
		destination = new File(destinationStr);
		
		try { 
			FileUtils.copyFile(source, destination, true); 
			try { FileUtils.forceDelete(source); }
			catch (IOException ex) { System.out.println("Failed to delete file.\r\n\tError: " + ex.getMessage()); }
			ret = true;
		} 
		catch (IOException ex) { System.out.println("Failed to copy file.\r\n\tError: " + ex.getMessage()); }
		
		
		
		return ret;
	}
}
