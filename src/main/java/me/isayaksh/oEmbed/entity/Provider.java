package me.isayaksh.oEmbed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Provider {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "provider_id")
    private Long id;

    private String providerUrl;
    private String url;

    public static Provider createProvider(String providerUrl, String url){
        Provider provider = new Provider();
        provider.providerUrl = providerUrl;
        provider.url = url;
        return provider;
    }

}
