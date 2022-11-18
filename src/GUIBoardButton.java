import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIBoardButton {
    int x;
    int y;
    int value;
    ButtonState state;
    JToggleButton button;
    ActionListener actionListener;
    private MyIcon icon;
    GUIBoardButton(int x, int y, ActionListener actionListener){
        this.x = x;
        this.y = y;
        value = 0;
        state = ButtonState.None;
        icon = MyIcon.None;
        this.actionListener = actionListener;
        button = prepareButton();
    }

    private JToggleButton prepareButton(){
        JToggleButton button = new JToggleButton();
        button.addActionListener(actionListener);
        button.setIcon(icon.imageIcon);
        button.setMargin(new Insets(0,0,0,0));
        button.setContentAreaFilled(true);
        return button;
    }

    public void rightClick(){
        switch(state){
            case None:
                state = ButtonState.Flagged;
                icon = MyIcon.Flag;
                button.setIcon(icon.imageIcon);
                break;
            case Flagged:
                state = ButtonState.None;
                icon = MyIcon.None;
                button.setIcon(icon.imageIcon);
                break;
            case Pushed:
                break;
        }
    }

    public void setIcon(MyIcon icon){
        this.icon = icon;
        button.setIcon(icon.imageIcon);
    }
}
