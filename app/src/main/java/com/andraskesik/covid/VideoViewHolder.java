package com.andraskesik.covid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by andra on 2016-07-23.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public VideoViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(android.R.id.text1);
        field.setText(name);
    }

    public void setText(String text) {
        TextView field = (TextView) mView.findViewById(android.R.id.text2);
        field.setText(text);
    }



}
