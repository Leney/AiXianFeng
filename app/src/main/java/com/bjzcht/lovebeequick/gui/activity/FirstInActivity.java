package com.bjzcht.lovebeequick.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.List;

import com.bjzcht.lovebeequick.R;
import com.bjzcht.lovebeequick.gui.adapter.FirstInAdapter;

/**
 * 第一次进入时的Activity
 * Created by llj on 2017/7/31.
 */

public class FirstInActivity extends AppCompatActivity {

    private RollPagerView mRollPagerView;

    private FirstInAdapter mAdapter;

    private List<Integer> headerBannerList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_first_in);

        headerBannerList = new ArrayList<>();
        headerBannerList.add(R.drawable.loading_page_1);
        headerBannerList.add(R.drawable.loading_page_2);
        headerBannerList.add(R.drawable.loading_page_3);

        mRollPagerView = (RollPagerView) findViewById(R.id.first_in_page_banner);
        mAdapter = new FirstInAdapter(mRollPagerView, headerBannerList);
        mRollPagerView.setAdapter(mAdapter);

        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position == headerBannerList.size() - 1){
                    MainActivity.startActivity(FirstInActivity.this);
                    finish();
                }
            }
        });
    }

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,FirstInActivity.class));
    }
}
