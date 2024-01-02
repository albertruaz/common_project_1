package com.example.tab_layout;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;



public class FullScreenImageFragment extends Fragment {
    private String imagePath;

    public FullScreenImageFragment(String imagePath) {
        this.imagePath = imagePath;
    }

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // db 설정
        dbHelper = ((MainActivity) getActivity()).getDbHelper();
        db = dbHelper.getWritableDatabase();

        //view 가져오기 및 화면 클릭시 뒤로가기
        View view = inflater.inflate(R.layout.fragment_full_screen_image, container, false);
        ImageView fullScreenImageView = view.findViewById(R.id.fullScreenImageView);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fullScreenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });

        // 이미지 가져오기
        Glide.with(requireContext())
                .load(imagePath)
                .into(fullScreenImageView);

        // 해시태그? 일단 가져오기
        String contactHashTag = dbHelper.onSearchPhotoHashTag(db, imagePath);
        TextView titleView = view.findViewById(R.id.title);
        titleView.setText(contactHashTag);

        TextView hashtagView = view.findViewById(R.id.hashtagView);
        EditText hashtagEdit = view.findViewById(R.id.hashtagEdit);

        // "EDIT" 버튼
        Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hashtagView.setVisibility(View.GONE);
                hashtagEdit.setVisibility(View.VISIBLE);
                hashtagEdit.setText(hashtagView.getText());
            }
        });
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 수정사항 저장될 수 있게

                dbHelper.onEditPhotoHashtag(db, imagePath, hashtagView.getText().toString());

                hashtagEdit.setVisibility(View.GONE);
                hashtagView.setVisibility(View.VISIBLE);
                hashtagView.setText(hashtagEdit.getText());
            }
        });

        return view;
    }
}

