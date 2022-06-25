

package com.shashi.bol.mancala.web.game.v1.view;

import com.shashi.bol.mancala.web.game.v1.controller.GameController;
import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FIFTH_PIT_PLAYER_A;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FIFTH_PIT_PLAYER_B;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FIRST_PIT_PLAYER_A;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FIRST_PIT_PLAYER_B;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FORTH_PIT_PLAYER_A;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FORTH_PIT_PLAYER_B;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.SECOND_PIT_PLAYER_A;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.SECOND_PIT_PLAYER_B;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.SIXTH_PIT_PLAYER_A;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.SIXTH_PIT_PLAYER_B;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.THIRD_PIT_PLAYER_A;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.THIRD_PIT_PLAYER_B;

@SpringComponent
@UIScope
public class PitLayoutComponent extends VerticalLayout implements KeyNotifier {

    private final PitComponent pit1;
    private final PitComponent pit2;
    private final PitComponent pit3;
    private final PitComponent pit4;
    private final PitComponent pit5;
    private final PitComponent pit6;

    private final PitComponent pit8;
    private final PitComponent pit9;
    private final PitComponent pit10;
    private final PitComponent pit11;
    private final PitComponent pit12;
    private final PitComponent pit13;

    public PitLayoutComponent(GameController gameController) {
        this.pit1 = new PitComponent(FIRST_PIT_PLAYER_A, gameController);
        this.pit2 = new PitComponent(SECOND_PIT_PLAYER_A, gameController);
        this.pit3 = new PitComponent(THIRD_PIT_PLAYER_A, gameController);
        this.pit4 = new PitComponent(FORTH_PIT_PLAYER_A, gameController);
        this.pit5 = new PitComponent(FIFTH_PIT_PLAYER_A, gameController);
        this.pit6 = new PitComponent(SIXTH_PIT_PLAYER_A, gameController);

        this.pit8 = new PitComponent(FIRST_PIT_PLAYER_B, gameController);
        this.pit9 = new PitComponent(SECOND_PIT_PLAYER_B, gameController);
        this.pit10 = new PitComponent(THIRD_PIT_PLAYER_B, gameController);
        this.pit11 = new PitComponent(FORTH_PIT_PLAYER_B, gameController);
        this.pit12 = new PitComponent(FIFTH_PIT_PLAYER_B, gameController);
        this.pit13 = new PitComponent(SIXTH_PIT_PLAYER_B, gameController);

        HorizontalLayout playerAHomePits = new HorizontalLayout(pit1, pit2, pit3, pit4, pit5, pit6);
        HorizontalLayout playerBHomePits = new HorizontalLayout(pit13, pit12, pit11, pit10, pit9, pit8);

        add(playerBHomePits, playerAHomePits);
    }

    public void fillPitStones(final MancalaGameBoard game) {
        this.pit1.setStones(game.getPitStones(FIRST_PIT_PLAYER_A).toString());
        this.pit2.setStones(game.getPitStones(SECOND_PIT_PLAYER_A).toString());
        this.pit3.setStones(game.getPitStones(THIRD_PIT_PLAYER_A).toString());
        this.pit4.setStones(game.getPitStones(FORTH_PIT_PLAYER_A).toString());
        this.pit5.setStones(game.getPitStones(FIFTH_PIT_PLAYER_A).toString());
        this.pit6.setStones(game.getPitStones(SIXTH_PIT_PLAYER_A).toString());
        this.pit8.setStones(game.getPitStones(FIRST_PIT_PLAYER_B).toString());
        this.pit9.setStones(game.getPitStones(SECOND_PIT_PLAYER_B).toString());
        this.pit10.setStones(game.getPitStones(THIRD_PIT_PLAYER_B).toString());
        this.pit11.setStones(game.getPitStones(FORTH_PIT_PLAYER_B).toString());
        this.pit12.setStones(game.getPitStones(FIFTH_PIT_PLAYER_B).toString());
        this.pit13.setStones(game.getPitStones(SIXTH_PIT_PLAYER_B).toString());
    }
}
