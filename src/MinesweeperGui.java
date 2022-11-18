import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MinesweeperGui {
    final int boardHeight = 30;
    final int boardWidth = 20;
    MinesweeperBoard gameBoard = new MinesweeperBoard(boardHeight, boardWidth);
    GUIBoardButton[][] boardButtons = new GUIBoardButton[boardHeight][boardWidth];
    ActionListener actionListener;
    boolean rightClick = false;
    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if(e.getButton() == 3) {
                boardButtonOnClick((JToggleButton)e.getSource(),false);
                rightClick = true;
            }
        }
    };
    MinesweeperGui() {
        JPanel gui = new JPanel(new BorderLayout(0,0));
        gui.setBorder(new EmptyBorder(4,4,4,4));
        JPanel gameContainer = new JPanel(new GridLayout(boardHeight, boardWidth,0,0));
        gui.add(gameContainer);

        actionListener = e -> boardButtonOnClick((JToggleButton)e.getSource(),true);
        for (int i = 0; i< boardHeight; i++) {
            for(int j = 0; j < boardWidth; j++) {
                GUIBoardButton button = new GUIBoardButton(i,j,actionListener);
                button.button.addMouseListener(mouseListener);
                boardButtons[i][j] = button;
                gameContainer.add(button.button);
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

    private void boardButtonOnClick(JToggleButton button, boolean leftClick) {
        for (int i = 0; i< boardHeight; i++) {
            for (int j = 0; j< boardWidth; j++) {
                if (button.equals(boardButtons[i][j].button)) {
                    GUIBoardButton current = boardButtons[i][j];
                    if(leftClick){
                        if(current.state!= ButtonState.Pushed){
                            BoardCell curr = gameBoard.getValue(i,j);
                            if(curr.isMine())
                                //clicked mine
                                current.setIcon(MyIcon.Mine);
                            else{
                                int number_of_adjacent_mines = gameBoard.numberOfMines(i,j);
                                current.button.setText(String.valueOf(number_of_adjacent_mines));
                                ButtonModel model = current.button.getModel();
                                model.setPressed(true);
                                button.setModel(model);
                                current.state = ButtonState.Pushed;
                            }
                        }
                    }
                    else {
                        current.rightClick();
                        }
                    }
                }
            }
        }

    public void resetBoard(){
        for(int i = 0; i < boardHeight; i++)
            for(int j = 0; j < boardWidth; j++)
            {
                gameBoard.resetBoard();
            }
    }
    public static void main(String[] args) {
        Runnable r = MinesweeperGui::new;
        SwingUtilities.invokeLater(r);
    }
}