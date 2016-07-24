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

    public void setUserName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.videocard_userid);
        field.setText(name);
    }

    public void setDescription(String description) {
        TextView field = (TextView) mView.findViewById(R.id.videocard_description);
        field.setText(description);
    }

    public void setDownloadLink(String downloadLink){
        TextView field = (TextView) mView.findViewById(R.id.videocard_downloadlink);
        field.setText(downloadLink);
    }



}
