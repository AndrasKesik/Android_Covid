package com.andraskesik.covid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by andra on 2016-07-24.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView mText2;
    TextView mText1;

    public CommentViewHolder(View itemView) {
        super(itemView);
        mText1 = (TextView) itemView.findViewById(android.R.id.text1);
        mText2 = (TextView) itemView.findViewById(android.R.id.text2);
    }

    public void setmText1(String text) {
        mText1.setText(text);

    }

    public void setmText2(String text) {
        mText2.setText(text);

    }
}
