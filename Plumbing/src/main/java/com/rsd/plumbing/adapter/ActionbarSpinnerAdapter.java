package com.rsd.plumbing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rsd.plumbing.R;

import java.util.List;

/**
 * Created by wadereweti on 27/11/13.
 */
public class ActionbarSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mOptions;

    public ActionbarSpinnerAdapter(Context context, List<String> options) {
        mContext = context;
        mOptions = options;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View actionBarView = mLayoutInflater.inflate(R.layout.actionbar_spinner, null);

        TextView title = (TextView) actionBarView.findViewById(R.id.actionbar_spinner_title);
        TextView subTitle = (TextView) actionBarView.findViewById(R.id.actionbar_spinner_subtitle);

        title.setText(mContext.getResources().getString(R.string.actionbar_spinner_title));
        subTitle.setText(mOptions.get(position));

        return actionBarView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View actionBarDropDownView = mLayoutInflater.inflate(R.layout.item_actionbar_spinner, null);

        TextView subtitle = (TextView) actionBarDropDownView.findViewById(R.id.actionbar_spinner_subtitle);
        subtitle.setText(mOptions.get(position ));


        return actionBarDropDownView;
    }
}
