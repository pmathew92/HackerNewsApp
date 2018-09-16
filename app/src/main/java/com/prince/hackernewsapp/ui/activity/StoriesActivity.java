package com.prince.hackernewsapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.utils.ConnectionManager;

import butterknife.ButterKnife;

public class StoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar_stories);
        setSupportActionBar(toolbar);
    }


    private void setLastUpdate() {
        if (ConnectionManager.isNetworkAvailable(this)) {

        }
    }
}
