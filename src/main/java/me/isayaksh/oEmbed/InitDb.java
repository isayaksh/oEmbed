package me.isayaksh.oEmbed;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.isayaksh.oEmbed.service.OembedService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InitDb {

    private final OembedService oembedService;

    @PostConstruct
    public void init(){
        log.info("InitDb.init");
        oembedService.createProvider();
    }

}
