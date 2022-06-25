package com.shashi.bol.mancala.game.v1.config;

import com.shashi.bol.mancala.game.v1.utils.ReflectionUtil;
import org.junit.jupiter.api.Test;
import springfox.documentation.service.Contact;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ApiContactTest {

    @Test
    void getContact() throws NoSuchFieldException, IllegalAccessException {
        ApiContact apiContact = new ApiContact();
        ReflectionUtil.set(apiContact,"contactName","name");
        ReflectionUtil.set(apiContact,"webSiteUrl","webSiteUrl");
        ReflectionUtil.set(apiContact,"emailId","emailId");
        Contact contact = apiContact.getApiContact();
        assertThat(contact.getEmail()).isEqualTo("emailId");
        assertThat(contact.getName()).isEqualTo("name");
        assertThat(contact.getUrl()).isEqualTo("webSiteUrl");
    }
}
