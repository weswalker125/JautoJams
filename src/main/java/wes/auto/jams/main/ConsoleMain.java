package wes.auto.jams.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
//import org.apache.logging.log4j.Logger;

import org.apache.commons.io.FileUtils;

import wes.auto.jams.models.SimpleSong;
import wes.auto.jams.utils.JamminUtils;

public class ConsoleMain {
	//private static Logger log = Logger.getLogger(ConsoleMain.class);
	//private static String _root = "/Users/wes/Music/";
	private static String _root = "C:\\Users\\WesWa\\Music\\";
	private static boolean PRINT = !true;
	
	public static void main(String... strings) {
		System.out.println("JautoJams console has started");
		menu();
		System.out.println("JautoJams console has finished");
	}
	
	private static void menu() {
		/* TODO: build menu using Spring tool */
		
		System.out.println("1. Correct song files");
		System.out.println("2. Find empty directories");
		//correctSongFiles();
		findEmptyDirs(_root);
		
	}
	
	private static void correctSongFiles() {
		List<SimpleSong> songs = getAllSongs(_root);
		for(Iterator<SimpleSong> it = songs.iterator(); it.hasNext();) {
			SimpleSong song = it.next();
			if(!song.filepath.equalsIgnoreCase(song.getFormattedFilePath())) {
				System.out.println(String.format("current: %s\r\nproposed:%s\r\n", song.filepath, song.getFormattedFilePath()));
				
				if(!JamminUtils.moveFile(song.filepath, song.getFormattedFilePath())) {
					System.out.println(String.format("Failed to move file.\r\n\tcurrent: %s\r\n\tproposed:%s\r\n", song.filepath, song.getFormattedFilePath()));
				}
			}
		}
	}
	
	private static List<String> getFiles(String scanDirectory) {
		List<String> all = new ArrayList<String>();
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
					if(!scanQueue.contains(fullPath)) {
						scanQueue.add(fullPath);
					}
				} else {
					all.add(fullPath);
				}
			}
		}
		
		return all;
	}
	
	private static List<SimpleSong> getAllSongs(String scanDirectory) {
		List<SimpleSong> all = new ArrayList<SimpleSong>();
		
		List<String> allFiles = getFiles(scanDirectory);
		for(Iterator<String> it = allFiles.iterator(); it.hasNext();) {
			String filePath = it.next();
			if(filePath.contains(".git")) {
				continue;
			} else if(filePath.endsWith(".mp3")) {
				//Otherwise process the file
				all.add(new SimpleSong(filePath));
			} else {
				System.out.println("Ignored file: " + filePath);
			}
		}
		
		if(PRINT) {
			JamminUtils.Print(all);
		}
		return all;
	}
	
	private static List<String> findEmptyDirs(String scanDirectory) {
		List<String> all = new ArrayList<String>();
		Queue<String> scanQueue = new PriorityQueue<String>();
		scanQueue.add(scanDirectory);
		
		while(!scanQueue.isEmpty()) {
			//Pop directory from queue
			File dir = new File(scanQueue.remove());
			File[] files = dir.listFiles();
//			if(files.length == 1) {
//				System.out.println("empty dir: " + dir.getPath());
//			}
			boolean hasMusic = false;
			//Iterate through all items under directory
			for(int i=0; i<files.length; ++i) {
				String fullPath = files[i].getPath();
				//Add unseen directory to queue
				if(files[i].isDirectory()) {
					if(!scanQueue.contains(fullPath)) {
						scanQueue.add(fullPath);
						hasMusic = true;
					}
				} else {
					if(fullPath.endsWith(".mp3")) {
						hasMusic = true;
					}
					all.add(fullPath);
				}
			}
			
			if(!hasMusic) {
				System.out.println("HEY - doesn't have music: " + dir.getPath());
			}
		}
		
		return all;
	}
}
