package wes.auto.jams.models;

import java.io.File;
import java.util.regex.Pattern;

public class SimpleSong {
	private final static String[] uglyPhrases = {"[Explicit]", "[+Digital Booklet]", "[+digital booklet]", "(Album Version)", "(Amazon MP3 Exclusive Version)"};
			
	public String title;
	public String artist;
	public String album;
	public String filepath;
	
	private boolean certainValidity;
	public boolean getCertainValidity() {
		return certainValidity;
	}
	
	/**
	 * Retrieve file extension from file path (excluding the "." (dot))
	 * @return
	 */
	public String getFileExt() {
		return filepath.substring(filepath.lastIndexOf(".") + 1).trim();
	}
	public int trackNumber;
	
	/**
	 * Stringify track number with padded 0 for single-digit numbers
	 * @return
	 */
	public String getFormattedTrackNumber() {
		return trackNumber < 10 ? "0" + trackNumber : "" + trackNumber;
	}
	
	public String getFormattedFilePath() {
		String[] tokens = this.filepath.split(Pattern.quote(File.separator));
		tokens[tokens.length - 1] = String.format("%s %s.%s", getFormattedTrackNumber(), this.title,getFileExt());
		tokens[tokens.length - 2] = this.album;
		tokens[tokens.length - 3] = this.artist;
		return String.join(File.separator, tokens);
	}
	
	public SimpleSong() { }
	
	public SimpleSong(String fullFilePath) {
		
		String[] tokens = fullFilePath.split(Pattern.quote(File.separator));
		this.album = cleanPhrase(tokens[tokens.length - 2]);
		this.artist = cleanPhrase(tokens[tokens.length - 3]);
		this.filepath = fullFilePath.trim();
		
		//determine title and track number
		String filename = tokens[tokens.length - 1].trim();
		
		//remove extension
		filename = filename.substring(0, filename.lastIndexOf("."));
		
		//separate disc/track information from filename
		int firstSpace = filename.indexOf(" ");

		try {
			if(firstSpace < 0) { 
				//no track info?!
				this.title = cleanPhrase(filename);
				this.trackNumber = 0;
				certainValidity = false;
			} else {
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
				
				certainValidity = true;
			}
		} catch(NumberFormatException ex) {
			//System.out.println("Error with file: " + this.filepath + ".\r\n\tError: " + ex.getMessage());
			
			//no track info?!
			this.title = cleanPhrase(filename);
			this.trackNumber = 0;
			certainValidity = false;
		}
	}
	
	private static String cleanPhrase(String phrase) {
		for(int i=0; i<uglyPhrases.length; ++i) {
			phrase = phrase.replace(uglyPhrases[i], "");
		}
		
		//replace multi-spaces
		phrase = phrase.replace("  ", " ").replace("  ", " ");
		return phrase.trim();
	}
}
