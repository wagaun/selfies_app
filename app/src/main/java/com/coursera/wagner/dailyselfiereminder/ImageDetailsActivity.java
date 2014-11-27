package com.coursera.wagner.dailyselfiereminder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageDetailsActivity extends Activity {
	
	public final static String FILENAME_PARAMETER = "coursera.wagner.dailyselfie.ImageDetailsActivity.filename";

    private final LruCache<String, Bitmap> mBitmapCache;

    private String mFileName;

	public ImageDetailsActivity() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mBitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.details);

		Bundle b = getIntent().getExtras();
		mFileName = b.getString(ImageDetailsActivity.FILENAME_PARAMETER);

		final ImageView imageView = (ImageView) findViewById(R.id.selfie);
		imageView.setContentDescription(mFileName);
        final String imageKey = "Full" + mFileName;
        Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap == null) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(mFileName);
            addBitmapToMemoryCache(imageKey, bitmap);
        }
        imageView.setImageBitmap(bitmap);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mBitmapCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mBitmapCache.get(key);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create action menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete:
                if(mFileName != null)
                {
                    new File(mFileName).delete();
                    Intent intent = new Intent(getApplicationContext(), DailySelfieActivity.class);
                    Toast.makeText(this, "File successfully deleted", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }
                return true;
            default:
                return false;
        }
    }
}

