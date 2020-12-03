package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */
public class PageInfo {
    @JsonProperty("totalResults")
    public int getTotalResults() {
        return this.totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    int totalResults;

    @JsonProperty("resultsPerPage")
    public int getResultsPerPage() {
        return this.resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    int resultsPerPage;


    public class Id {
        @JsonProperty("kind")
        public String getKind() {
            return this.kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        String kind;

        @JsonProperty("videoId")
        public String getVideoId() {
            return this.videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        String videoId;

        @JsonProperty("playlistId")
        public String getPlaylistId() {
            return this.playlistId;
        }

        public void setPlaylistId(String playlistId) {
            this.playlistId = playlistId;
        }

        String playlistId;

        @JsonProperty("channelId")
        public String getChannelId() {
            return this.channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        String channelId;
    }

    public class Default {
        @JsonProperty("url")
        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        String url;

        @JsonProperty("width")
        public int getWidth() {
            return this.width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        int width;

        @JsonProperty("height")
        public int getHeight() {
            return this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        int height;
    }

    public class Medium {
        @JsonProperty("url")
        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        String url;

        @JsonProperty("width")
        public int getWidth() {
            return this.width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        int width;

        @JsonProperty("height")
        public int getHeight() {
            return this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        int height;
    }

    public class High {
        @JsonProperty("url")
        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        String url;

        @JsonProperty("width")
        public int getWidth() {
            return this.width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        int width;

        @JsonProperty("height")
        public int getHeight() {
            return this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        int height;
    }

    public class Thumbnails {
        @JsonProperty("default")
        public Default getDefault() {
            return this.defaultVal;
        }

        public void setDefault(Default defaultVal) {
            this.defaultVal = defaultVal;
        }

        Default defaultVal;

        @JsonProperty("medium")
        public Medium getMedium() {
            return this.medium;
        }

        public void setMedium(Medium medium) {
            this.medium = medium;
        }

        Medium medium;

        @JsonProperty("high")
        public High getHigh() {
            return this.high;
        }

        public void setHigh(High high) {
            this.high = high;
        }

        High high;
    }

    public class Snippet {
        @JsonProperty("publishedAt")
        public String getPublishedAt() {
            return this.publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        String publishedAt;

        @JsonProperty("channelId")
        public String getChannelId() {
            return this.channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        String channelId;

        @JsonProperty("title")
        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        String title;

        @JsonProperty("description")
        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        String description;

        @JsonProperty("thumbnails")
        public Thumbnails getThumbnails() {
            return this.thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }

        Thumbnails thumbnails;

        @JsonProperty("channelTitle")
        public String getChannelTitle() {
            return this.channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        String channelTitle;

        @JsonProperty("liveBroadcastContent")
        public String getLiveBroadcastContent() {
            return this.liveBroadcastContent;
        }

        public void setLiveBroadcastContent(String liveBroadcastContent) {
            this.liveBroadcastContent = liveBroadcastContent;
        }

        String liveBroadcastContent;

        @JsonProperty("publishTime")
        public String getPublishTime() {
            return this.publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        String publishTime;
    }

    public class Item {
        @JsonProperty("kind")
        public String getKind() {
            return this.kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        String kind;

        @JsonProperty("etag")
        public String getEtag() {
            return this.etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        String etag;

        @JsonProperty("id")
        public Id getId() {
            return this.id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        Id id;

        @JsonProperty("snippet")
        public Snippet getSnippet() {
            return this.snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }

        Snippet snippet;
    }

    public class Root {
        @JsonProperty("kind")
        public String getKind() {
            return this.kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        String kind;

        @JsonProperty("etag")
        public String getEtag() {
            return this.etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        String etag;

        @JsonProperty("regionCode")
        public String getRegionCode() {
            return this.regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        String regionCode;

        @JsonProperty("pageInfo")
        public PageInfo getPageInfo() {
            return this.pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        PageInfo pageInfo;

        @JsonProperty("items")
        public List<Item> getItems() {
            return this.items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        List<Item> items;
    }
}

