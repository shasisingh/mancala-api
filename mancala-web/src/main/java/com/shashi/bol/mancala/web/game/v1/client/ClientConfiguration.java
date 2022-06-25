package com.shashi.bol.mancala.web.game.v1.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class ClientConfiguration {

    @Value("${client.ssl.keystore.filename}")
    private String clientSslKeystoreFilename;

    @Value("${client.ssl.keystore.password}")
    private String clientSslKeystorePassword;

    @Value("${client.ssl.keystore.type}")
    private String clientSslKeystoreType;

    @Value("${client.ssl.truststore.filename}")
    private String clientSslTruststoreFilename;

    @Value("${client.ssl.truststore.password}")
    private String clientSslTruststorePassword;

    @Value("${game.mancala.api.base.url}")
    private URI mancalaServiceUri;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory rdcClientHttpRequestFactory(HttpClient httpClient) {
        ClientHttpRequestFactory rdcClientHttpRequestFactory = new ClientHttpRequestFactory();
        rdcClientHttpRequestFactory.setHttpClient(httpClient);
        return rdcClientHttpRequestFactory;
    }

    @Bean
    public HttpClient httpClient(SSLContext sslContext) {
        RequestConfig config = RequestConfig.custom()
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .disableConnectionState()
                .setDefaultRequestConfig(config)
                .build();
    }

    @Bean
    public MancalaUriBuilder mancalaUriBuilder() {
        return new MancalaUriBuilder(mancalaServiceUri);
    }

    @Bean
    public SSLContextBuilder sslContextBuilder() {
        return SSLContexts.custom();
    }

    @Bean
    public SSLContext sslContext(SSLContextBuilder sslContextBuilder, ApplicationContext applicationContext)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {

        if (clientSslKeystoreFilename != null && !clientSslKeystoreFilename.isEmpty()) {
            Resource keyStoreResource = applicationContext.getResource(clientSslKeystoreFilename);
            KeyStore keyStore = KeyStore.getInstance(clientSslKeystoreType);
            keyStore.load(keyStoreResource.getInputStream(), clientSslKeystorePassword.toCharArray());
            sslContextBuilder.loadKeyMaterial(keyStore, clientSslKeystorePassword.toCharArray());

        }

        if (clientSslTruststoreFilename != null && !clientSslTruststoreFilename.isEmpty()) {
            Resource trustStoreResource = applicationContext.getResource(clientSslTruststoreFilename);
            sslContextBuilder.loadTrustMaterial(trustStoreResource.getFile(), clientSslTruststorePassword.toCharArray());
        }
        return sslContextBuilder.build();
    }

}

