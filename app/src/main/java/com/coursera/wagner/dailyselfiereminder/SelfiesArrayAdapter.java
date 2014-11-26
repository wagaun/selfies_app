package com.coursera.wagner.dailyselfiereminder;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfiesArrayAdapter extends ArrayAdapter<File> {

	private File[] mFiles;
	
	public SelfiesArrayAdapter(Context context, File[] files) {
		super(context, R.layout.list_item, files);
		mFiles = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Use setPic
		LayoutInflater inflater = (LayoutInflater) getContext()
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_item, parent, false);
	    TextView fileName = (TextView) rowView.findViewById(R.id.fileName);
	    File f = mFiles[position];
	    fileName.setText(f.getName());
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.thumbnail);
	    imageView.setImageURI(Uri.fromFile(f));
	    imageView.setContentDescription(f.getName());
	    return rowView;
	}
}
