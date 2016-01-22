package wes.auto.jams.models;

import java.io.File;

public class SimpleSong {
	private final static String[] uglyPhrases = {"[Explicit]", "[+Digital Booklet]", "(Album Version)"};
			
	public String title;
	public String artist;
	public String album;
	public String filepath;
	public String getFileExt() {
		return filepath.substring(filepath.lastIndexOf(".") + 1).trim();
	}
	public int trackNumber;
	public String getFormattedTrackNumber() {
		return trackNumber < 10 ? "0" + trackNumber : "" + trackNumber;
	}
	
	public String getFormattedFilePath() {
		String[] tokens = this.filepath.split(File.separator);
		tokens[tokens.length - 1] = String.format("%s %s.%s", getFormattedTrackNumber(), this.title,getFileExt());
		tokens[tokens.length - 2] = this.album;
		tokens[tokens.length - 3] = this.artist;
		return String.join(File.separator, tokens);
	}
	
	public SimpleSong(String fullFilePath) {
		
		String[] tokens = fullFilePath.split(File.separator);
		this.album = cleanPhrase(tokens[tokens.length - 2]);
		this.artist = cleanPhrase(tokens[tokens.length - 3]);
		this.filepath = cleanPhrase(fullFilePath);
		
		//determine title and track number
		String filename = tokens[tokens.length - 1].trim();
		
		//remove extension
		filename = filename.substring(0, filename.lastIndexOf("."));
		
		//separate disc/track information from filename
		int firstSpace = filename.indexOf(" ");
		this.title = cleanPhrase(filename.substring(firstSpace + 1));
		
		String trackInfo = filename.substring(0, firstSpace).trim();
		if(trackInfo.contains("-")) {
			String[] indexNumbers = trackInfo.split("-");
			this.trackNumber = Integer.parseInt(indexNumbers[indexNumbers.length - 1]);
		} else if(trackInfo.contains(" ")){
			String[] indexNumbers = trackInfo.split(" ");
			this.trackNumber = Integer.parseInt(indexNumbers[indexNumbers.length - 1]);
		} else {
			this.trackNumber = Integer.parseInt(trackInfo);
		}
	}
	
	private static String cleanPhrase(String phrase) {
		for(int i=0; i<uglyPhrases.length; ++i) {
			phrase = phrase.replace(uglyPhrases[i], "");
		}
		
		//replace multi-spaces
		phrase = phrase.replace("  ", "");
		return phrase.trim();
	}
}
