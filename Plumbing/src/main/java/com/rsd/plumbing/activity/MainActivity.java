package com.rsd.plumbing.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.rsd.plumbing.R;
import com.rsd.plumbing.adapter.PipeAdapter;
import com.rsd.plumbing.async.GetPipesTask;
import com.rsd.plumbing.callback.PipesCallBack;
import com.rsd.plumbing.util.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity implements PipesCallBack {

    private final String TAG = "MainActivity";
    private static final String TITLE = "Select a pipe size";
    private MainActivity mCurrentContext;

    @InjectView(R.id.gridview_pipesize) GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mCurrentContext = this;

        setupActionBar();
        fetchPipeDetails();
        setupGridView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchPipeDetails() {
        new GetPipesTask(this).execute();
    }

    private void setupActionBar() {
        getActionBar().setTitle(TITLE);
        getActionBar().setBackgroundDrawable(null);
    }

    private void setupGridView() {
        mGridView.setAdapter(new PipeAdapter(this));
        mGridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = createBundle(view, position);
                Intent intent = new Intent(mCurrentContext, LengthActivity.class);
                intent.putExtra(Constants.BUNDLE_PIPE, bundle);
                startActivity(intent);
            }

            private Bundle createBundle(View view, int position) {
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);
                String pipeSize = ((TextView) view.findViewById(R.id.label_pipe_size)).getText().toString();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.VIEW_HEIGHT, view.getHeight());
                bundle.putInt(Constants.VIEW_WIDTH, view.getHeight());
                bundle.putInt(Constants.VIEW_LEFT, screenLocation[0]);
                bundle.putInt(Constants.VIEW_TOP, screenLocation[1]);
                bundle.putInt(Constants.PIPE_POSTION, position);
                bundle.putString(Constants.PIPE_SIZE_LABEL, pipeSize);

                String pipeSizeStringValue = view.findViewById(R.id.label_pipe_size).getContentDescription().toString();
                bundle.putInt(Constants.PIPE_SIZE, Integer.parseInt(pipeSizeStringValue));

                return bundle;
            }
        });
    }

    @Override
    public void pipesRetrieved() {
        setupGridView();
    }
}
