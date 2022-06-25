
package com.shashi.bol.mancala.web.game.v1.view;

import com.shashi.bol.mancala.web.game.v1.controller.GameController;
import com.shashi.bol.mancala.web.game.v1.exceptions.ApiConnectionException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;

@UIScope
@SpringComponent
public class PitComponent extends VerticalLayout {

    private final static Logger LOGGER = LoggerFactory.getLogger(PitComponent.class);

    private static final Integer DEFAULT_PIT_STONES = 0;

    private final TextField pit = new TextField();
    private final Button btn = new Button();

    private GameController gameController;

    public PitComponent() {
        pit.getElement().setAttribute("theme", "align-center");
        pit.setReadOnly(true);
        pit.setValue(DEFAULT_PIT_STONES.toString());
        pit.getStyle().set("font-size", "15px");
        pit.getStyle().set("font-weight", "bold");
        pit.setMaxLength(30);
        pit.setMinLength(30);
        btn.getElement().setAttribute("theme", "align-center");
        add(btn, pit);
        setAlignItems(Alignment.CENTER);

        pit.addValueChangeListener(e -> {
            pit.getStyle().set("background-color", "#ff9933");
            new ChangeColorThread(UI.getCurrent(), pit).start();
        });
    }

    public TextField getPit() {
        return pit;
    }

    public Button getBtn() {
        return btn;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setStones(String stones) {
        this.pit.setValue(stones);
    }

    public PitComponent(Integer pitIndex, GameController gameController) {
        this();
        this.gameController = gameController;
        pit.setId(pitIndex.toString());
        btn.setText(pitIndex.toString());
        btn.setTabIndex(pitIndex);
        eventOnClick();
    }

    private static class ChangeColorThread extends Thread {

        private final UI ui;
        private final TextField textField;
        public ChangeColorThread(UI ui, TextField textField) {
            this.ui = ui;
            this.textField = textField;
        }
        @Override
        public void run() {
            //sleep(1000)- if want to
            ui.access(() -> textField.getStyle().set("background-color", "#ffffff"));
        }
    }

    private void eventOnClick() {
        btn.addClickListener(e -> {
            if (!this.gameController.hasGameStarted()) {
                Notification.show("Please click on 'Start Game' button to start the game first!", 2000, MIDDLE);
                return;
            }
            Notification.show("Clicked:on pit:" + e.getSource().getTabIndex());
            try {
                this.gameController.applyEvent(e.getSource().getTabIndex());
            } catch (ApiConnectionException ex) {
                LOGGER.error(ex.getMessage(), ex);
                Notification.show("Error connecting to the server!. Try later", 2000, MIDDLE);
            }
        });
    }



}
