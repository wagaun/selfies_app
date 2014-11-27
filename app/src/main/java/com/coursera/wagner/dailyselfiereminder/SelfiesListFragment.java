package com.coursera.wagner.dailyselfiereminder;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Wagner on 25-Nov-14.
 */
public class SelfiesListFragment extends ListFragment {

    private ImageDataManager mImageDataManager;

    public SelfiesListFragment() {
        try {
            mImageDataManager = new ImageDataManager();
        } catch(IOException e) {
            Log.e(DailySelfieActivity.class.getCanonicalName(), "Error initializing image file parameters", e);
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new SelfiesArrayAdapter(getActivity().getBaseContext(),
                mImageDataManager.getFileList()));
//        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "Long press listener!", Toast.LENGTH_LONG).show();
//                // TODO implement context long click logic
//                return true;
//            }
//        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        File item = (File) getListAdapter().getItem(position);
        Intent intent = new Intent(getActivity().getBaseContext(), ImageDetailsActivity.class);
        Bundle b = new Bundle();
        b.putString(ImageDetailsActivity.FILENAME_PARAMETER, item.getAbsolutePath());
        intent.putExtras(b);
        startActivity(intent);
    }
}
