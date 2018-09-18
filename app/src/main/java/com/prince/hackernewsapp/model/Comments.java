package com.prince.hackernewsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comments {
    @SerializedName("by")
    @Expose
    private String by;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("kids")
    @Expose
    private List<Integer> kids = null;
    @SerializedName("parent")
    @Expose
    private int parent;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("type")
    @Expose
    private String type;

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getKids() {
        return kids;
    }

    public void setKids(List<Integer> kids) {
        this.kids = kids;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
