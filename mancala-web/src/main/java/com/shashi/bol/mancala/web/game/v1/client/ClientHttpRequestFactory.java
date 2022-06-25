package com.shashi.bol.mancala.web.game.v1.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;

/**
 * The type Client http request factory.
 */
public class ClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        ClientHttpRequest clientHttpRequest = super.createRequest(uri, httpMethod);
        clientHttpRequest.getHeaders().set("application", "mancala-web");
        return clientHttpRequest;
    }
}
