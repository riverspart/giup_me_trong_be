package com.riverspart.storage;

public interface RSDBStorable extends RSStorable {
	public Object insert();
	public Object update();
	public Object delete();
	public Object query();
}
