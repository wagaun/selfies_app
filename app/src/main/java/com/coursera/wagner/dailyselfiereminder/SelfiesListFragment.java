package com.coursera.wagner.dailyselfiereminder;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_current:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Add the buttons
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Deleted selfie " + mImageDataManager.getFileList()[info.position].getName(), Toast.LENGTH_LONG).show();
                        mImageDataManager.getFileList()[info.position].delete();
                        SelfiesArrayAdapter adapter = new SelfiesArrayAdapter(getActivity(), mImageDataManager.getFileList());
                        SelfiesListFragment.this.setListAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setTitle("Do you really want to delete this selfie?").create().show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        File item = (File) getListAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        Bundle b = new Bundle();
        b.putString(ImageDetailsActivity.FILENAME_PARAMETER, item.getAbsolutePath());
        intent.putExtras(b);
        startActivity(intent);
    }


}
