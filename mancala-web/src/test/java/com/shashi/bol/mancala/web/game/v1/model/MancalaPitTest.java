package com.shashi.bol.mancala.web.game.v1.model;

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;


class MancalaPitTest {

    @Test
    void testAccountProfile() {
        assertThat(MancalaPit.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()));
    }
}
