package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by haotr on 02/12/2020.
 */
@Data
@NoArgsConstructor
public class YoutubeResponseData {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageInfo {
        public int totalResults;
        public int resultsPerPage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Id {
        public String kind;
        public String videoId;
        public String playlistId;
        public String channelId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Default {
        public String url;
        public int width;
        public int height;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Medium {
        public String url;
        public int width;
        public int height;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class High {
        public String url;
        public int width;
        public int height;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Thumbnails {
        @JsonProperty("default")
        public Default defaultVal;
        public Medium medium;
        public High high;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Snippet {
        public String publishedAt;
        public String channelId;
        public String title;
        public String description;
        public Thumbnails thumbnails;
        public String channelTitle;
        public String liveBroadcastContent;
        public String publishTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        public String kind;
        public String etag;
        public Id id;
        public Snippet snippet;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Root {
        public String kind;
        public String etag;
        public String regionCode;
        public PageInfo pageInfo;
        public List<Item> items;
    }
}
