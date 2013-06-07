package com.riverspart.storage;

import android.content.SharedPreferences;

public interface RSLazyStorable extends RSStorable {
	public void saveStorage(SharedPreferences prefs);
	public void loadStorage(SharedPreferences prefs);
	public void clearStorage(SharedPreferences prefs);
}
