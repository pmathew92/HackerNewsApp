package com.prince.hackernewsapp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.model.TopStory;
import com.prince.hackernewsapp.network.RetrofitClient;
import com.prince.hackernewsapp.ui.adapter.StoryAdapter;
import com.prince.hackernewsapp.utils.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class StoriesActivity extends AppCompatActivity {

    private Realm mRealm;
    private Handler mHandler = new Handler();
    private long mLastUpdated;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.toolbar_stories)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_top_Stories);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mRealm = Realm.getDefaultInstance();

        RealmResults<TopStory> storiesList = mRealm.where(TopStory.class).findAll();
        StoryAdapter adapter = new StoryAdapter(this, storiesList, true);

        recyclerView.setAdapter(adapter);

        if(ConnectionManager.isNetworkAvailable(this)) {
            fetchData();
        }else{
            Toast.makeText(this,"No connection",Toast.LENGTH_SHORT).show();
        }

        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeLayout.setOnRefreshListener(() -> {
            if( ConnectionManager.isNetworkAvailable(this)) {
                fetchData();
            } else {
                mSwipeLayout.setRefreshing(false);
            }
        });

    }


    /**
     * Runnable to keep track of last updated time
     */
    private final Runnable lastUpdatedTime=new Runnable() {
        @Override
        public void run() {
            if(mLastUpdated == 0) return;
            mToolbar.setSubtitle("Updated "+ DateUtils.getRelativeTimeSpanString(mLastUpdated,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL));
            mHandler.postDelayed(this,DateUtils.MINUTE_IN_MILLIS);
        }
    };


    /**
     * Method to make API calls and add to local DB
     */
    private void fetchData() {
        Disposable disposable = RetrofitClient.getApiService().getTopStories()
                .flatMapIterable( integers -> {
                    mLastUpdated=System.currentTimeMillis();
                    mHandler.post(lastUpdatedTime);
                    runOnUiThread(() -> mSwipeLayout.setRefreshing(false));

                    List<Integer> newList=new ArrayList<>();
                    Realm realm=Realm.getDefaultInstance();
                        for(Integer integer:integers){
                            if(realm.where(TopStory.class).equalTo("id", integer).findFirst() == null)
                                newList.add(integer);
                        }
                    realm.close();
                    return newList;
                })
                .flatMap(integer -> RetrofitClient.getApiService().getStory(integer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topStory -> mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(topStory)),
                        Throwable::getLocalizedMessage);

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(lastUpdatedTime);
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(lastUpdatedTime);
        mSwipeLayout.setRefreshing(false);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) compositeDisposable.dispose();
        mHandler.removeCallbacks(lastUpdatedTime);
        mHandler=null;
        super.onDestroy();
    }
}
