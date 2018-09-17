package com.prince.hackernewsapp.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.model.TopStory;
import com.prince.hackernewsapp.network.RetrofitClient;
import com.prince.hackernewsapp.ui.adapter.StoryAdapter;
import com.prince.hackernewsapp.utils.ConnectionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class StoriesActivity extends AppCompatActivity {

    private Realm mRealm;
    private Disposable disposable;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar_stories);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_top_Stories);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mRealm = Realm.getDefaultInstance();

        RealmResults<TopStory> storiesList = mRealm.where(TopStory.class).findAll();
        StoryAdapter adapter = new StoryAdapter(this, storiesList, true);

        recyclerView.setAdapter(adapter);

        fetchData();

        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeLayout.setOnRefreshListener(() -> {
            if(!mSwipeLayout.isRefreshing()) {
                fetchData();
            }
        });

    }


    /**
     * Method to make API calls and add to local DB
     */
    private void fetchData() {
        disposable = RetrofitClient.getApiService().getTopStories()
                .flatMapIterable(integers -> integers)
//                .filter(integer -> {
//                        Realm realm = Realm.getDefaultInstance();
//                        return realm.where(TopStory.class).equalTo("id", integer).findFirst() != null;
//                    })
                .flatMap(integer -> RetrofitClient.getApiService().getStory(integer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topStory ->
                                mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(topStory)),
                        Throwable::getMessage);
    }


    private void setLastUpdate() {
        if (ConnectionManager.isNetworkAvailable(this)) {

        }
    }

    @Override
    protected void onStop() {
        if(mSwipeLayout.isRefreshing())
            mSwipeLayout.setRefreshing(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        super.onDestroy();

    }
}
