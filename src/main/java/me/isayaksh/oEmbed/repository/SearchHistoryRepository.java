package me.isayaksh.oEmbed.repository;

import me.isayaksh.oEmbed.entity.SearchHistory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SearchHistoryRepository {

    private static Map<Long, SearchHistory> repo = new HashMap<>();
    private static Long sequence = 0L;

    public void save(SearchHistory searchHistory){
        String newUrl = searchHistory.getUrl();
        // 이전에 검색한 기록이 있다면 추가하지 않는다.
        for (SearchHistory value : repo.values()) {
            if(value.getUrl().equals(newUrl)){return;}
        }
        repo.put(sequence++, searchHistory);
    }

    public List<SearchHistory> findAll(){
        return new ArrayList<>(repo.values());
    }

    public void delete(Long id){
        repo.remove(id);
    }

    public void clear(){
        repo.clear();
    }
}
