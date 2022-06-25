package com.shashi.bol.mancala.game.v1.dto;

import com.shashi.bol.mancala.game.v1.model.ErrorResponse;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;



public class ErrorResponseTest {

    @Test
    public void testAccountProfile() {
        assertThat(ErrorResponse.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()));
    }

}
