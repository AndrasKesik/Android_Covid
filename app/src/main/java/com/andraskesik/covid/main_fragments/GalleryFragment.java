package com.andraskesik.covid.main_fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andraskesik.covid.CategoryViewHolder;
import com.andraskesik.covid.R;
import com.andraskesik.covid.VideoViewHolder;
import com.andraskesik.covid.activities.CategoryActivity;
import com.andraskesik.covid.activities.MainActivity;
import com.andraskesik.covid.model.CovidConstants;
import com.andraskesik.covid.model.Video;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by andra on 2016-07-22.
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = GalleryFragment.class.getSimpleName();
    private MainActivity mAct;
    private RecyclerView.Adapter<CategoryViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_gallery, container, false);
        mAct = (MainActivity) getActivity();
        mAct.setTitle("Browse");
//        Intent categoryIntent = new Intent(getActivity(), CategoryActivity.class);
//        categoryIntent.putExtra(CategoryActivity.CATEGORY, "Personal");
//        startActivity(categoryIntent);
//        CardView cardView = (CardView) view.findViewById(R.id.gallery_welcome);
//        cardView.setElevation(50f);

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recyclerView_gallery);
//        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(mAct));
        mAdapter = new RecyclerView.Adapter<CategoryViewHolder>() {

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_categories, parent, false);


                return new CategoryViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(CategoryViewHolder holder, final int position) {
                holder.setListener(new CategoryViewHolder.ICatgoryViewHolderClicks() {
                    @Override
                    public void openCategory() {
                        Intent openCategoryIntent = new Intent(mAct, CategoryActivity.class);
                        openCategoryIntent.putExtra(CategoryActivity.CATEGORY, CovidConstants.CATEGORIES[position]);
                        openCategoryIntent.putExtra(MainActivity.USERNAME, mAct.getmUser().getName());
                        startActivity(openCategoryIntent);
                    }
                });
                holder.setLabelText(CovidConstants.CATEGORIES[position]);
                Drawable picture = getResources().getDrawable(CovidConstants.PICTUREIDS[position]);
                holder.setBackgroundImage(picture);
                Log.d(TAG, "populateviewHolder___________________");
            }

            @Override
            public int getItemCount() {
                return CovidConstants.CATEGORIES.length;
            }




        };
        recycler.setAdapter(mAdapter);


        return view;
    }

}
