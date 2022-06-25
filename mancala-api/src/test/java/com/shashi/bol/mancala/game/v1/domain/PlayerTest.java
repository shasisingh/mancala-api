package com.shashi.bol.mancala.game.v1.domain;



import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;


 class PlayerTest {

    @Test
     void testAccountProfile() {
        assertThat(Player.class, allOf(
                hasValidBeanConstructor(),
                hasValidBeanToString(),
                hasValidGettersAndSetters()));
    }
}
