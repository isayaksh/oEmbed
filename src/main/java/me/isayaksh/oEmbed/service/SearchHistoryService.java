package me.isayaksh.oEmbed.service;

import lombok.RequiredArgsConstructor;
import me.isayaksh.oEmbed.entity.SearchHistory;
import me.isayaksh.oEmbed.repository.SearchHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public List<SearchHistory> findAll(){
        return searchHistoryRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        searchHistoryRepository.delete(id);
    }

    @Transactional
    public void clear(){
        searchHistoryRepository.clear();
    }
}
