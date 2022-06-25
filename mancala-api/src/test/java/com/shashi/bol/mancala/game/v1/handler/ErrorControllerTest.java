package com.shashi.bol.mancala.game.v1.handler;

import com.shashi.bol.mancala.game.v1.controller.ErrorController;
import com.shashi.bol.mancala.game.v1.model.ErrorResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


 class ErrorControllerTest {

    @Test
     void testCheckCustomErrorController() {

        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        ErrorController errorController = new ErrorController(errorAttributes);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getAttribute("javax.servlet.error.status_code")).thenReturn(403);
        when(httpServletRequest.getAttribute("javax.servlet.error.exception")).thenReturn(new RuntimeException("No such method"));
        ResponseEntity<ErrorResponse> response = errorController.error(httpServletRequest);
        assertEquals(403, response.getStatusCode().value());
        assertNotNull(response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
        assertNotNull(response.getBody().getDetails());
        assertEquals("Forbidden", response.getBody().getHttpStatus().getReasonPhrase());

    }

    @Test
     void HttpMediaTypeNotSupportedException() {

        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        ErrorController errorController = new ErrorController(errorAttributes);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getAttribute("javax.servlet.error.status_code")).thenReturn(415);
        when(httpServletRequest.getAttribute("javax.servlet.error.exception")).thenReturn(new HttpMediaTypeNotSupportedException("media type is not supported"));
        ResponseEntity<ErrorResponse> response = errorController.error(httpServletRequest);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        assertEquals(415, response.getBody().getHttpStatus().value());
        assertEquals("media type is not supported", response.getBody().getMessage());
    }

    @Test
     void testErrorPathCheck() {

        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        ErrorController errorController = new ErrorController(errorAttributes);
        assertNotNull(errorController);
    }
}
