package me.isayaksh.oEmbed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.isayaksh.oEmbed.entity.Provider;
import me.isayaksh.oEmbed.entity.ResponseDto;
import me.isayaksh.oEmbed.repository.ProviderRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OembedService {

    private final ProviderRepository providerRepository;

    /** oEmbed의 Provider를 초기화 **/
    @Transactional
    public void createProvider(){
        log.info("OembedService.createProvider");

        // "https://oembed.com/providers.json" API 요청 
        WebClient client = WebClient.create("https://oembed.com");
        Mono<String> response = client.get()
                .uri("/providers.json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
        
        // JSON Data Parsing 후 DB에 저장
        saveProvider(response);
    }

    /** 사용자가 제공한 URL을 이용한 oEmbed 결과 요청 **/
    public ResponseDto search(String url) throws URISyntaxException {
        log.info("OembedService.search");
        // url 에서 domainName 추출
        String domainName = getDomainName(url);

        // 유효성 검사: 해당 url 의 domain 을 찾을 수 없을 때
        if(domainName == null){
            return ResponseDto.builder()
                    .valid("해당 url에 해당하는 domain을 찾을 수 없습니다.")
                    .build();
        }
        // 유효성 검사: url 의 domain 이 instagram 일 때
        if(domainName.contains("instagram")){
            return ResponseDto.builder()
                    .valid("현재 Meta(facebook, instagram)는 oEmbed 개방형 API 정책을 포기하고 인증 받은 사용자만 oEmbed API를 이용할 수 있도록 정책을 변경하였습니다." +
                            "Meta(facebook, instagram)의 oEmbed를 사용하려면 앱이 oEmbed 읽기 기능에 대한 앱 검수를 거쳐야 합니다." +
                            "해당하는 자세한 정보는 url 정보에서 확인할 수 있습니다.")
                    .url("https://developers.facebook.com/docs/instagram/oembed?locale=ko_KR")
                    .build();
        }

        // 추출한 domainName 으로 provider 추출
        Provider provider = providerRepository.findByProviderUrlContaining(domainName).orElseThrow(() -> new IllegalStateException("해당하는 Provider가 존재하지 않습니다."));
        // oEmbed api 를 요청하기 위한 url 생성
        String requiredUrl = getRequiredUrl(provider.getUrl());

        // (oEmbed JSON) → (ResponseDto) 변환 후 반환
        WebClient client = WebClient.create(requiredUrl);
        return client.get()
                .uri("?format=json&url=" + url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ResponseDto.class)
                .block();
    }

    /** parsing 결과 DB에 저장하기 **/
    private void saveProvider(Mono<String> response) {
        log.info("OembedService.createProvider.saveProvider");
        JSONParser parser = new JSONParser();
        response.subscribe(res -> {
            try {
                for (Object object : (JSONArray) parser.parse(res)) {
                    JSONObject provider = (JSONObject) object;
                    String providerUrl = (String) provider.get("provider_url");
                    for (Object endpoints : (JSONArray) provider.get("endpoints")) {
                        JSONObject endpoint = (JSONObject) endpoints;
                        String url = (String) endpoint.get("url");
                        Provider entity = Provider.createProvider(providerUrl, url);
                        providerRepository.save(entity);
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /** oEmbed api 호출을 위한 url 생성 **/
    private String getRequiredUrl(String providerUrl) {
        log.info("OembedService.search.getRequiredUrl");
        return providerUrl.contains("{format}") ? providerUrl.replace("{format}", "json") : providerUrl;
    }

    /** 사용자로부터 전달받은 url 의 DomainName 을 추출 **/
    public static String getDomainName(String url) throws URISyntaxException {
        log.info("OembedService.search.getRequiredUrl");
        String domain = new URI(url).getHost();
        // url 에서 domain 을 추출할 수 없는 경우
        if(domain == null){return null;}
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
