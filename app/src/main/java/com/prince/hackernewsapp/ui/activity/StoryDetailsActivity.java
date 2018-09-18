package com.prince.hackernewsapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.model.TopStory;
import com.prince.hackernewsapp.ui.fragment.ArticleFragment;
import com.prince.hackernewsapp.ui.fragment.CommentsFragment;
import com.prince.hackernewsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class StoryDetailsActivity extends AppCompatActivity {


    private boolean isUrlEmpty = false;
    private TopStory mTopStory;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
        ButterKnife.bind(this);

        Realm mRealm = Realm.getDefaultInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        int storyId = getIntent().getIntExtra("storyId", 0);

        mTopStory = mRealm.where(TopStory.class).equalTo("id", storyId).findFirst();

        populateBasicInfo();

        isUrlEmpty = checkIfUrlEmpty(mTopStory.getUrl());

        setUpViewPager();

    }

    /**
     * Method to populate the basic info about the article loaded
     */
    private void populateBasicInfo() {
        TextView articleTitle = findViewById(R.id.tv_article_title);
        TextView articleUrl = findViewById(R.id.tv_article_url);
        TextView articleOwner = findViewById(R.id.tv_user);
        String url = mTopStory.getUrl();
        if (checkIfUrlEmpty(url)) {
            url = "No url present";
        }
        if (mTopStory != null) {
            articleTitle.setText(mTopStory.getTitle());
            articleUrl.setText(url);
            long time = System.currentTimeMillis() / 1000 - mTopStory.getTime();

            articleOwner.setText(String.format("%s ago%s%s", TimeUtils.timeConverter(time), " . ", mTopStory.getBy()));
        }
    }


    /**
     * Method to check if the given url is empty or not
     *
     * @param url
     * @return
     */
    private boolean checkIfUrlEmpty(String url) {
        return url == null || url.isEmpty();
    }


    /**
     * Method to setup viewpager with adapter
     */
    private void setUpViewPager() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        String commentsTitle = mTopStory.getKids().size() + " COMMENTS";
        ArrayList<Integer> commentIdList = new ArrayList<>(mTopStory.getKids());
        mSectionsPagerAdapter.addFragment(CommentsFragment.newInstance(commentIdList), commentsTitle);
        if (!isUrlEmpty) {
            mSectionsPagerAdapter.addFragment(ArticleFragment.newInstance(mTopStory.getUrl()), "ARTICLE");
        }
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitle = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }
    }
}
