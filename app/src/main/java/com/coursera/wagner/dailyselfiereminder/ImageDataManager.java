package com.coursera.wagner.dailyselfiereminder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ImageDataManager {
	
	private final File mDirectory;
	
	public ImageDataManager() throws IOException {
		mDirectory = new File(Environment.getExternalStorageDirectory() + "/Selfies/");
		if(!mDirectory.exists() && !mDirectory.mkdirs()) {
			throw new IOException();
		}
	}
	
	public File getDirectory() {
		return mDirectory;
	}

	public String createFileName() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    return "selfie_" + timeStamp + ".jpg";
	}
	
	public File[] getFileList() {
		return mDirectory.listFiles();
	}
}
