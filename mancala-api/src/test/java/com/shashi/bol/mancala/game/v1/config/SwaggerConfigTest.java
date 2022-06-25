package com.shashi.bol.mancala.game.v1.config;



import io.swagger.v3.oas.annotations.info.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SwaggerConfigTest {

    @Mock
    ApiContact apiContact;

//    @Test
//     void configTest(){
//        SwaggerConfig swaggerConfig= new SwaggerConfig();
//        when(apiContact.getApiContact())
//                .thenReturn(new Contact("name","url","email"));
//        Docket config = swaggerConfig.mancalaApi(apiContact);
//        assertNotNull(config);
//        assertEquals("application/json",config.getDocumentationType().getMediaType().toString());
//        verify(apiContact).getApiContact();
//    }
//
//    @Test
//    void testGetApiContact(){
//        SwaggerConfig swaggerConfig= new SwaggerConfig();
//        assertNotNull(swaggerConfig.getContact());
//    }

}
