package com.shashi.bol.mancala.game.v1.domain;



import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

public class MancalaPitTest {

    @Test
    public void testAccountProfile() {
        assertThat(MancalaPit.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

    @Test
    public void testToString() {
        MancalaPit mancalaPit = new MancalaPit();
        mancalaPit.setStones(1);
        mancalaPit.setPitLocation(1);
        assertThat(mancalaPit.isEmpty(),is(false));
        mancalaPit.setStones(0);
        assertThat(mancalaPit.isEmpty(),is(true));
        assertThat("checkToString", mancalaPit.toString(), is("pitLocation=1, stones=0"));
    }

}
