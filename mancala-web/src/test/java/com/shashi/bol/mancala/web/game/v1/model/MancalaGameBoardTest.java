package com.shashi.bol.mancala.web.game.v1.model;

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;


class MancalaGameBoardTest {

    @Test
     void testAccountProfile() {
        assertThat(MancalaGameBoard.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()));
    }
}
