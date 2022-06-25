

package com.shashi.bol.mancala.web.game.v1;

import com.shashi.bol.mancala.web.game.v1.controller.GameController;
import com.shashi.bol.mancala.web.game.v1.exceptions.ApiConnectionException;
import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import com.shashi.bol.mancala.web.game.v1.view.MancalaGameComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route ("games/v1/mancala/play")

public class MainView extends VerticalLayout  {

	private final static Logger LOGGER = LoggerFactory.getLogger(MainView.class);

	private final TextField gameIdTextField;
	private final Button startGameBtn;
	private final GameController gameController;
	private final MancalaGameComponent mancalaGameComponent;


	public MainView(MancalaGameComponent mancalaGameComponent, GameController gameController) {
		this.mancalaGameComponent = mancalaGameComponent;
		this.gameController = gameController;

		// build the game information layout
		this.startGameBtn = new Button("Start Game");
		Label gameIdLabel = new Label("Game Id:");
		this.gameIdTextField = new TextField("", "", "");
		this.gameIdTextField.setReadOnly(true);
		this.gameIdTextField.setMaxLength(35);
		this.gameIdTextField.setWidth("350px");


		// build layout for game id
		HorizontalLayout gameIdLayout = new HorizontalLayout(gameIdLabel, gameIdTextField);
		gameIdLayout.setAlignItems(Alignment.CENTER);
		add(gameIdLayout);

		// adding the game itself
		add(mancalaGameComponent);

		// build layout for actions
		HorizontalLayout actions = new HorizontalLayout(startGameBtn);
		add(actions);
        // Instantiate and edit new Customer the new button is clicked
		gameController.reset();
		startGameBtn.setAutofocus(true);
        startGameBtn.addClickListener(e -> {
            try {
					MancalaGameBoard game = this.gameController.startNewGame();
					mancalaGameComponent.newGame(game);
					this.gameIdTextField.setValue(game.getGameId());
					this.mancalaGameComponent.getPlayerTurnTextField().setValue("");
					Notification.show("New Game started. id:" + game.getGameId(), 3000, Notification.Position.TOP_CENTER);
					startGameBtn.setEnabled(false);
					gameIdTextField.focus();
            } catch (ApiConnectionException ex) {
                Notification.show("Error!. Message:" + ex.getMessage());
				LOGGER.error(ex.getMessage(), ex);
            }
        });

	}
}
