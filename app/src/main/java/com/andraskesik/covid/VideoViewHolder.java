package com.andraskesik.covid;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by andra on 2016-07-23.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = VideoViewHolder.class.getSimpleName();
    View mView;
    private IViedoViewHolderClicks mListener;

    public VideoViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDescription(String description) {
        TextView field = (TextView) mView.findViewById(R.id.videocard_description);
        field.setText(description);
    }


    public void setUploaderName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.videocard_uploadername);
        field.setText(name);

    }

    public void setListener(VideoViewHolder.IViedoViewHolderClicks listener) {
        mListener = listener;
        mView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "__Card clicked__");
//        switch (mView.getId()) {
//            case R.id.videocard_button_watch:
//                Log.d(TAG, "__Watch clicked__");
//                mListener.onWatchVideoClick();
//                break;
//            case R.id.videocard_button_comment:
//                Log.d(TAG, "__Comment clicked__");
//                mListener.onCommentClick();
//                break;
//            case R.id.videocard_button_save:
//                Log.d(TAG, "__Save clicked__");
//                mListener.onSaveClick();
//                break;
//        }
        mListener.openVideoPage();

    }


    public interface IViedoViewHolderClicks {
        void openVideoPage();
//        void onWatchVideoClick();
//        void onCommentClick();
//        void onSaveClick();
    }
}
