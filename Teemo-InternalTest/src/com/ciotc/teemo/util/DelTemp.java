package com.ciotc.teemo.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class DelTemp {

private static File[] getFiles(String filePath) {
	String filterStart = "libsqlitejdbc";
	File file = new File(filePath);
	FileFilter fileFilter = new FileFilter(filterStart);
	File files[] = file.listFiles(fileFilter);
	return files;
}

public static void delTemp() {
	String filePath = System.getProperty("java.io.tmpdir");
	File temp[] = getFiles(filePath);
	for (File files : temp) {
//		System.out.println(files.getName());
		files.delete();
	}
}

private static class FileFilter implements FilenameFilter {
private String filter;

public FileFilter(String filter) {
	this.filter = filter;
}

@Override
public boolean accept(File dir, String name) {
	// TODO Auto-generated method stub
	return name.startsWith(filter);
}

}

}
