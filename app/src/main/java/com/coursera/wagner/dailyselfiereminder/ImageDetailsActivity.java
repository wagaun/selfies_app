package com.coursera.wagner.dailyselfiereminder;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ImageDetailsActivity extends Activity {
	
	public final static String FILENAME_PARAMETER = "coursera.wagner.dailyselfie.ImageDetailsActivity.filename";
	
	public ImageDetailsActivity() {
	}
	
	// TODO show more details
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.details);
		
		Bundle b = getIntent().getExtras();
		String fileName = b.getString(ImageDetailsActivity.FILENAME_PARAMETER);
		File file = new File(fileName);
		
		ImageView imageView = (ImageView) findViewById(R.id.selfie);
		imageView.setContentDescription(file.getName());
		imageView.setImageURI(Uri.fromFile(file));
	}
}

