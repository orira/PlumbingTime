package com.rsd.plumbing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsd.plumbing.R;
import com.rsd.plumbing.domain.Pipe;
import com.rsd.plumbing.util.BitmapUtil;
import com.rsd.plumbing.util.TypefaceUtil;

import java.util.List;

/**
 * Created by wadereweti on 27/11/13.
 */
public class PipeAdapter extends BaseAdapter {

    private final String LABEL_SUFFIX = "mls";
    private final String CUSTOM_LABEL = "Custom Size";

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Pipe> mPipes;

    public PipeAdapter(Context context) {
        mPipes = Pipe.findAll();
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPipes.size();
    }

    @Override
    public Object getItem(int position) {
        return mPipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pipe pipe = mPipes.get(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.gridview_pipe_item, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview_pipe);
        imageView.setImageBitmap(BitmapUtil.getBitMap(position, mContext));

        TextView textView = (TextView) convertView.findViewById(R.id.label_pipe_size);
        textView.setText(getLabel(pipe.size));
        textView.setTypeface(TypefaceUtil.getRobotoThin(mContext));

        return convertView;
    }

    private String getLabel(int size) {
        return size == 0 ? CUSTOM_LABEL : size + LABEL_SUFFIX;
    }
}
