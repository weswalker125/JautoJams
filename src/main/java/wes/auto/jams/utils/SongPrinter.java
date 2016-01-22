package wes.auto.jams.utils;

import java.util.List;

import com.google.gson.Gson;

import wes.auto.jams.models.SimpleSong;

public class SongPrinter {
	private static Gson gson = new Gson();
	public static void Print(List<SimpleSong> songs) {
		System.out.println(gson.toJson(songs));
	}
}
