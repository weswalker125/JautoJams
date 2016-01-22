package wes.auto.jams.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import wes.auto.jams.models.SimpleSong;
import wes.auto.jams.utils.SongPrinter;

public class ConsoleMain {
	private static String _root = "/Users/wes/Music/Amazon Music/";
	private static String DELIMITER = "/";
	private static boolean PRINT = true;
	
	public static void main(String... strings) {
		menu();
	}
	
	private static void menu() {
		/* TODO: build menu using Spring tool */
		
		List<SimpleSong> songs = getAllSongs(_root);
		for(Iterator<SimpleSong> it = songs.iterator(); it.hasNext();) {
			SimpleSong song = it.next();
			if(!song.filepath.equalsIgnoreCase(song.getFormattedFilePath())) {
				System.out.println(String.format("current: %s\r\nproposed:%s\r\n", song.filepath, song.getFormattedFilePath()));
			}
		}
		
		
	}
	
	private static List<SimpleSong> getAllSongs(String scanDirectory) {
		List<SimpleSong> all = new ArrayList<SimpleSong>();
		Queue<String> scanQueue = new PriorityQueue<String>();
		scanQueue.add(scanDirectory);
		
		while(!scanQueue.isEmpty()) {
			//Pop directory from queue
			File dir = new File(scanQueue.remove());
			File[] files = dir.listFiles();
			
			//Iterate through all items under directory
			for(int i=0; i<files.length; ++i) {
				String fullPath = files[i].getPath();
				
				//Add unseen directory to queue
				if(files[i].isDirectory()) {
					if(!scanQueue.contains(fullPath))
						scanQueue.add(fullPath);
				}
				else if(fullPath.endsWith(".mp3")) {
					//Otherwise process the file
					all.add(new SimpleSong(fullPath));
				}
			}
		}
		
		if(PRINT) {
			SongPrinter.Print(all);
		}
		return all;
	}
}
