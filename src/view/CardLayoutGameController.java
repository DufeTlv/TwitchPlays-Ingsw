package view;

import java.awt.CardLayout;
import java.awt.Container;

public class CardLayoutGameController{
    public static final String GAME_PANEL = "GamePanel";
    public static final String MENU_PANEL = "MenuPanel";

    private Container container;
    private CardLayout cardLayout;

    public CardLayoutGameController(Container parent, CardLayout layout) {
        container = parent;
        cardLayout = layout;
    }

    public void showMenu() {
        cardLayout.show(container, MENU_PANEL);
    }

    public void showGame() {
    	container.add(new GamePanel(this), GAME_PANEL);
        cardLayout.show(container, GAME_PANEL);
        container.transferFocus();
    }
}
