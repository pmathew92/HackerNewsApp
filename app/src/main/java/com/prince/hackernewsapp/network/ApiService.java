package com.prince.hackernewsapp.network;

import com.prince.hackernewsapp.model.TopStory;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("topstories.json?print=pretty")
    Single<List<Integer>> getTopStories();

    @GET("item/{id}.json?print=pretty")
    Single<TopStory> getStory(@Path("id") int id);
}
