package me.isayaksh.oEmbed.entity;

import lombok.Getter;

@Getter
public class SearchHistory {
    private String url;

    public static SearchHistory createSearchHistory(String title, String url){
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.url = url;
        return searchHistory;
    }
}
