import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MinesweeperGui {
    final int boardHeight = 20;
    final int boardWidth = 50;
    MinesweeperBoard gameBoard = new MinesweeperBoard(boardHeight, boardWidth);
    JButton[][] boardButtons = new JButton[boardHeight][boardWidth];
    ActionListener actionListener;

    int iconSize = 15;
    Icon white_icon = new ImageIcon(new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB));
    Icon black_icon = new ImageIcon(new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_RGB));
    MinesweeperGui() {
        JPanel gui = new JPanel(new BorderLayout(0,0));
        gui.setBorder(new EmptyBorder(4,4,4,4));
        JPanel gameContainer = new JPanel(new GridLayout(boardHeight, boardWidth,0,0));
        gui.add(gameContainer);
        actionListener = e -> boardButtonOnClick((JButton)e.getSource());
        for (int i = 0; i< boardHeight; i++) {
            for(int j = 0; j < boardWidth; j++) {
                JButton b = prepareAButton();
                gameContainer.add(b);
                boardButtons[i][j] = b;
            }
        }
        JPanel pane = new JPanel();
        pane.add(Box.createHorizontalGlue());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        pane.add(resetButton);

        gui.add(pane, BorderLayout.SOUTH);

        JFrame f = new JFrame("MineSweeper");
        f.add(gui);
        f.pack();
        f.setMinimumSize(f.getSize());
        f.setLocationByPlatform(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    private JButton prepareAButton() {
        JButton b = new JButton();
        b.setIcon(white_icon);
        b.setMargin(new Insets(0,0,0,0));
        b.setContentAreaFilled(true);
        b.addActionListener(actionListener);
        return b;
    }

    private void boardButtonOnClick(JButton button) {
        for (int i = 0; i< boardHeight; i++) {
            for (int j = 0; j< boardWidth; j++) {
                if (button.equals(boardButtons[i][j])) {
                    JButton current = boardButtons[i][j];
                    }
                }
            }
        }

    public void resetBoard(){
        for(int i = 0; i < boardHeight; i++)
            for(int j = 0; j < boardWidth; j++)
            {
                gameBoard.resetBoard();
                boardButtons[i][j].setIcon(white_icon);
            }
    }
    public static void main(String[] args) {
        Runnable r = MinesweeperGui::new;
        SwingUtilities.invokeLater(r);
    }
}