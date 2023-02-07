package me.isayaksh.oEmbed.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {

    // 유효성 검사의 결과를 저장할 필드
    private String valid;

    private String type;
    private String version;
    private String title;
    private String author_name;
    private String author_url;
    private String provider_name;
    private String provider_url;
    private String cache_age;
    private String thumbnail_url;
    private Long thumbnail_width;
    private Long thumbnail_height;
    private String html;
    private Long width;
    private Long height;
    private String url;

    @Builder
    public ResponseDto(String valid, String type, String version, String title, String author_name, String author_url, String provider_name, String provider_url, String cache_age, String thumbnail_url, Long thumbnail_width, Long thumbnail_height, String html, Long width, Long height, String url) {
        this.valid = valid;
        this.type = type;
        this.version = version;
        this.title = title;
        this.author_name = author_name;
        this.author_url = author_url;
        this.provider_name = provider_name;
        this.provider_url = provider_url;
        this.cache_age = cache_age;
        this.thumbnail_url = thumbnail_url;
        this.thumbnail_width = thumbnail_width;
        this.thumbnail_height = thumbnail_height;
        this.html = html;
        this.width = width;
        this.height = height;
        this.url = url;
    }
}
