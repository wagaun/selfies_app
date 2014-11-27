package com.coursera.wagner.dailyselfiereminder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class SelfiesArrayAdapter extends ArrayAdapter<File> {

    private final File[] mFiles;

    static class ViewHolder {
        public TextView fileName;
        public ImageView thumbnail;
    }
	
	public SelfiesArrayAdapter(Context context, File[] files) {
		super(context, R.layout.list_item, files);
        mFiles = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.fileName = (TextView) rowView.findViewById(R.id.fileName);
            viewHolder.thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
	    final File f = mFiles[position];
        holder.fileName.setText(f.getName());
        try {
            setPic(holder, f, 150);
        } catch (FileNotFoundException e) {
            Log.e(getClass().getCanonicalName(), "Error loading bitmap " + f.getAbsolutePath(), e);
        }
        return rowView;
	}

    private void setPic(ViewHolder holder, File file, int width) throws FileNotFoundException {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new BufferedInputStream(new FileInputStream(file)), null, bmOptions);
        int photoW = bmOptions.outWidth;

        // Determine how much to scale down the image
        int scaleFactor = photoW / width;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(new FileInputStream(file)), null, bmOptions);
        holder.thumbnail.setImageBitmap(bitmap);
        holder.thumbnail.setContentDescription(file.getName());
    }
}
