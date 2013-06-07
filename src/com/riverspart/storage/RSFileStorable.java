package com.riverspart.storage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public interface RSFileStorable extends RSStorable {
	public void cleanFile(String fileName);
	public void exportFile(FileWriter fileWriter)  throws IOException;
	public void importFile(FileReader fileReader)  throws IOException;
}
