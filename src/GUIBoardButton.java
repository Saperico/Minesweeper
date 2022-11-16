import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIBoardButton {
    int x;
    int y;
    int value;
    boolean isFlagged;
    JButton button;
    ActionListener actionListener;
    GUIBoardButton(int x, int y, ActionListener actionListener){
        this.x = x;
        this.y = y;
        value = 0;
        isFlagged = false;
        this.actionListener = actionListener;
        button = prepareButton();
    }

    private JButton prepareButton(){
        JButton button = new JButton();
        button.addActionListener(actionListener);
        button.setMargin(new Insets(0,0,0,0));
        button.setContentAreaFilled(true);
        return button;
    }
}
