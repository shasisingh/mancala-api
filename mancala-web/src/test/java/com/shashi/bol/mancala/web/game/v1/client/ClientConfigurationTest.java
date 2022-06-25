package com.shashi.bol.mancala.web.game.v1.client;

import org.apache.http.client.HttpClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static com.shashi.bol.mancala.web.game.v1.ReflectionUtil.set;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientConfigurationTest {

    @Test
    void restTemplate() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        ClientHttpRequestFactory factory = mock(ClientHttpRequestFactory.class);
        assertNotNull(clientConfiguration.restTemplate(factory));
    }

    @Test
    void rdcClientHttpRequestFactory() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        HttpClient https = mock(HttpClient.class);
        ClientHttpRequestFactory conf = clientConfiguration.rdcClientHttpRequestFactory(https);
        assertNotNull(conf);
        assertNotNull(conf.getHttpClient());
    }

    @Test
    void httpClient() throws NoSuchAlgorithmException {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        SSLContext sl = SSLContext.getDefault();
        assertNotNull(clientConfiguration.httpClient(sl));
    }

    @Test
    void mancalaUriBuilder() throws NoSuchFieldException, IllegalAccessException {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        set(clientConfiguration, "mancalaServiceUri", URI.create("url"));
        assertNotNull(clientConfiguration.mancalaUriBuilder().getUrlForCreateGame());
    }

    @Test
    void sslContextBuilder() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        assertNotNull(clientConfiguration.sslContextBuilder());
    }

    @Test
    void sslContext()
            throws NoSuchFieldException, IllegalAccessException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
            NoSuchAlgorithmException, KeyManagementException {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        Resource trustStore = new ClassPathResource("mancala-web-truststore.p12");
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getResource("truststore")).thenReturn(trustStore);

        set(clientConfiguration, "clientSslKeystoreFilename", "truststore");
        set(clientConfiguration, "clientSslKeystorePassword", "vt56@612");
        set(clientConfiguration, "clientSslKeystoreType", "PKCS12");
        set(clientConfiguration, "clientSslTruststoreFilename", "truststore");
        set(clientConfiguration, "clientSslTruststorePassword", "vt56@612");
        SSLContext ssl = clientConfiguration.sslContext(SSLContextBuilder.create(), applicationContext);
        assertNotNull(ssl);
        assertNotNull(ssl.getDefaultSSLParameters());
    }
}
