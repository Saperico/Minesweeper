import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public enum MyIcon {
    None(new ImageIcon(new BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB))),
    Flag(new ImageIcon("src/flag.png")),
    Mine(new ImageIcon("src/mine.png"));
    Icon imageIcon;
    MyIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    MyIcon() {

    }
}
