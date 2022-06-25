package com.shashi.bol.mancala.web.game.v1.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;


class ClientHttpRequestFactoryTest {

    @Test
     void createRequestTest() throws IOException {
        ClientHttpRequestFactory factory = new ClientHttpRequestFactory();

        ClientHttpRequest request = factory.createRequest(URI.create("http://localhost:6666"), HttpMethod.GET);
        assertEquals("mancala-web", request.getHeaders().get("application").get(0));
    }
}
