
package com.shashi.bol.mancala.web.game.v1.view;

import com.shashi.bol.mancala.web.game.v1.controller.GameController;
import com.shashi.bol.mancala.web.game.v1.events.DisplayGameEvent;
import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.LEFT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.RIGHT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard.GameStatus.FINISHED;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

@UIScope
@SpringComponent
@Component
public class MancalaGameComponent extends VerticalLayout implements KeyNotifier {


    private final PitLayoutComponent pitLayoutComponent;
    private PitComponent rightHouse ;
    private PitComponent leftHouse ;
    private TextField playerTurnTextField;
    private Label player1Name;
    private Label player2Name;
    private final Label winLabel;


    public MancalaGameComponent(PitLayoutComponent pitLayoutComponent, GameController gameController) {

        this.pitLayoutComponent = pitLayoutComponent;

        Label playerTurnLabel = createLabelPlayerTurn();

        // build layout for game information
        HorizontalLayout turnLayout = new HorizontalLayout(playerTurnLabel, playerTurnTextField);
        turnLayout.setAlignItems(CENTER);
        add(turnLayout);

        rightHouse = createPlayer1Component(gameController);
        leftHouse = createPlayer2Component(gameController);

        HorizontalLayout gameLayout = new HorizontalLayout(leftHouse, pitLayoutComponent, rightHouse);
        gameLayout.setAlignItems(CENTER);

        add(gameLayout);

        winLabel = setWinLabel();
        setAlignItems(CENTER);
    }



    @EventListener
    public void handleFlushEvent(DisplayGameEvent event) {
        final MancalaGameBoard game = event.getGame();
        this.fillMancala(game);
        this.playerTurnTextField.setValue(getNameOfPlayerWithTurn(game));

        if (checkPlayerTurn(game)) {
            Notification.show(playerTurnTextField.getValue() + " get free turn!", 1500, Notification.Position.MIDDLE);
        }

        if (game.onGoingGame()) {
            return;
        }

        if (FINISHED.equals(game.getGameStatus())) {
            if (getOrDefault(game.getPlayer1().getStatus()).equals("WINNER")) {
                this.winLabel.setText("Game Over!. " + game.getPlayer1().getName() + " Won!!!");
            } else {
                this.winLabel.setText("Game Over!." + game.getPlayer2().getName() + " Won!!!");
            }
            this.winLabel.setVisible(true);
        } else {
            this.winLabel.setText("Game Over!.It's a Tie");
            this.winLabel.setVisible(true);
        }

    }


    public TextField getPlayerTurnTextField() {
        return playerTurnTextField;
    }

    public void newGame(MancalaGameBoard game) {
        this.fillMancala(game);
        this.player1Name.setText(game.getPlayer1().getName());
        this.player2Name.setText(game.getPlayer2().getName());
        this.winLabel.setVisible(false);
    }

    private PitComponent createPlayer2Component(GameController gameController) {
        leftHouse = new PitComponent(LEFT_PIT_HOUSE_ID, gameController);
        leftHouse.setAlignItems(CENTER);
        player2Name= new Label("Player unknown 2");
        leftHouse.add(player2Name);
        return leftHouse;
    }

    private PitComponent createPlayer1Component(GameController gameController) {
        rightHouse = new PitComponent(RIGHT_PIT_HOUSE_ID , gameController);
        rightHouse.setAlignItems(CENTER);
        player1Name= new Label("Player unknown 1");
        rightHouse.add(player1Name);
        return rightHouse;
    }

    private Label createLabelPlayerTurn() {
        Label playerTurnLabel = new Label("Player turn:");
        this.playerTurnTextField = new TextField("");
        this.playerTurnTextField.setReadOnly(true);
        return playerTurnLabel;
    }

    private String getNameOfPlayerWithTurn(final MancalaGameBoard game) {
        if (game.getPlayer1().isTurn()) {
            return game.getPlayer1().getName();
        }
        return game.getPlayer2().getName();
    }

    private boolean checkPlayerTurn(final MancalaGameBoard game) {
        return game.getPlayer1().playerGetFreeTurn() || game.getPlayer2().playerGetFreeTurn();
    }

    private String getOrDefault(String var1) {
        return var1 == null || var1.isBlank() ? "" : var1;
    }

    private void fillMancala(MancalaGameBoard game) {
        this.leftHouse.setStones(game.leftHouseStones().toString());
        this.rightHouse.setStones(game.rightHouseStones().toString());
        this.pitLayoutComponent.fillPitStones(game);
    }
    private Label setWinLabel() {
        // Adding the win layout
        Label label= new Label("");
        label.setVisible(false);
        label.getStyle().set("font-size", "50px");
        label.getStyle().set("color", "#38761d");
        HorizontalLayout winLayout = new HorizontalLayout(label);
        winLayout.setAlignItems(CENTER);
        add(winLayout);
        return label;
    }



}
