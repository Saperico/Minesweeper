import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIBoardButton {
    int x;
    int y;
    int value;
    ButtonState state;
    JToggleButton jToggleButton;
    ActionListener actionListener;
    public MyIcon icon;
    GUIBoardButton(int x, int y, ActionListener actionListener){
        this.x = x;
        this.y = y;
        value = 0;
        state = ButtonState.None;
        icon = MyIcon.None;
        this.actionListener = actionListener;
        jToggleButton = prepareButton();
    }

    private JToggleButton prepareButton(){
        JToggleButton button = new JToggleButton();
        button.addActionListener(actionListener);
        button.setIcon(icon.imageIcon);
        button.setMargin(new Insets(0,0,0,0));
        button.setContentAreaFilled(true);
        button.setName(x +":" + y);
        return button;
    }

    public void setIcon(MyIcon icon){
        this.icon = icon;
        jToggleButton.setIcon(icon.imageIcon);
    }
}
