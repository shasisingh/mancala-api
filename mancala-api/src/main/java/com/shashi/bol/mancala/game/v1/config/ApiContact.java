package com.shashi.bol.mancala.game.v1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.service.Contact;

@Configuration
@Profile("demo")
public class ApiContact {

    @Value("${api.contact.person:shashi}")
    private String contactName;

    @Value("${api.contact.website:shashi}")
    private String webSiteUrl;

    @Value("${api.contact.emailId:shashi}")
    private String emailId;

    public Contact getApiContact(){
        return new Contact(contactName, webSiteUrl, emailId);
    }

}
