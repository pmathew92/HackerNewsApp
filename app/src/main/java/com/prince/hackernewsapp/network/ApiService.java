package com.prince.hackernewsapp.network;

import com.prince.hackernewsapp.model.Comments;
import com.prince.hackernewsapp.model.TopStory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("topstories.json?print=pretty")
    Observable<List<Integer>> getTopStories();

    @GET("item/{id}.json?print=pretty")
    Observable<TopStory> getStory(@Path("id") int id);

    @GET("item/{id}.json?print=pretty")
    Observable<Comments> getComments(@Path("id") int id);
}
