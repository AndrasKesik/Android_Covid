package com.andraskesik.covid;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andra on 2016-07-23.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    View mView;
    public ICatgoryViewHolderClicks mListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setLabelText(String name) {
        TextView field = (TextView) mView.findViewById(R.id.listitem_textview);
        field.setText(name);
    }

    public void setBackgroundImage(Drawable drawable) {
        ImageView field = (ImageView) mView.findViewById(R.id.list_item_background_img);
        field.setImageDrawable(drawable);
    }

    public void setListener(ICatgoryViewHolderClicks listener) {
        mListener = listener;
        mView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        mListener.openCategory();
    }

    public static interface ICatgoryViewHolderClicks {
        public void openCategory();
    }


}