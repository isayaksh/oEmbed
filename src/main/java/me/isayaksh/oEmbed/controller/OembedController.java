package me.isayaksh.oEmbed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.isayaksh.oEmbed.entity.ResponseDto;
import me.isayaksh.oEmbed.service.OembedService;
import me.isayaksh.oEmbed.service.SearchHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OembedController {

    private final OembedService oembedService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/")
    public String home(Model model){
        log.info("OembedController.home");
        model.addAttribute("response", new ResponseDto());
        model.addAttribute("searchHistory",searchHistoryService.findAll());
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "url") String url,
                         Model model) throws URISyntaxException {
        log.info("OembedController.search");
        model.addAttribute("response", oembedService.search(url));
        model.addAttribute("searchHistory",searchHistoryService.findAll());
        return "home";
    }

}
