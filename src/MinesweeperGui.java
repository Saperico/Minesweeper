import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Queue;

public class MinesweeperGui {
    final int boardHeight = 20;
    final int boardWidth = 20;
    MinesweeperBoard gameBoard = new MinesweeperBoard(boardHeight, boardWidth);
    GUIBoardButton[][] boardButtons = new GUIBoardButton[boardHeight][boardWidth];
    ActionListener actionListener;
    boolean rightClick = false;
    boolean firstLeftClick = true;
    int flags = 0;
    int flaggedMines = 0;
    JLabel flagsLabel;
    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (e.getButton() == 3) {
                boardButtonOnClick((JToggleButton) e.getSource(), false);
                rightClick = true;
            }
        }
    };

    MinesweeperGui() {
        JPanel gui = new JPanel(new BorderLayout(0, 0));
        gui.setBorder(new EmptyBorder(4, 4, 4, 4));
        JPanel gameContainer = new JPanel(new GridLayout(boardHeight, boardWidth, 0, 0));
        gui.add(gameContainer);

        actionListener = e -> boardButtonOnClick((JToggleButton) e.getSource(), true);
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                GUIBoardButton button = new GUIBoardButton(i, j, actionListener);
                button.jToggleButton.addMouseListener(mouseListener);
                boardButtons[i][j] = button;
                gameContainer.add(button.jToggleButton);
            }
        }
        JPanel pane = new JPanel();
        pane.add(Box.createHorizontalGlue());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        pane.add(resetButton);

        JButton testButton = new JButton("Test");
        testButton.addActionListener(e->test());
        pane.add(testButton);

        JLabel numberOfMines = new JLabel("Mines : "+gameBoard.getNumberOfMines());
        pane.add(numberOfMines);

        flagsLabel = new JLabel("Flags : 0");
        pane.add(flagsLabel);

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
        GUIBoardButton current = getGUIButton(button);
        if(current.state!= ButtonState.Pushed) {
            if (leftClick) {
                if (current.state == ButtonState.None) {
                    BoardCell curr = gameBoard.getCell(current.x, current.y);
                    if (curr.isMine())
                        clickedMine(current);
                    else {
                        clickedEmpty(current);
                    }
                }
            } else {
                rightClick(current);
                flagsLabel.setText("Flags " + String.valueOf(flags));
            }
        }
        else{
            current.jToggleButton.setSelected(true);
        }
    }

    public void rightClick(GUIBoardButton button){
        switch(button.state){
            case None:
                button.state = ButtonState.Flagged;
                button.icon = MyIcon.Flag;
                button.jToggleButton.setIcon(button.icon.imageIcon);
                flags+=1;
                break;
            case Flagged:
                button.state = ButtonState.None;
                button.icon = MyIcon.None;
                button.jToggleButton.setIcon(button.icon.imageIcon);
                flags-=1;
                break;
            case Pushed:
                break;
        }
    }

    private GUIBoardButton getGUIButton(JToggleButton button){
        String name = button.getName();
        String[] coordinates = name.split(":");
        int first = Integer.parseInt(coordinates[0]);
        int second = Integer.parseInt(coordinates[1]);
        return boardButtons[first][second];
    }

    public void clickedMine(GUIBoardButton button) {
        button.setIcon(MyIcon.Mine);
    }

    public void clickedEmpty(GUIBoardButton button) {
        int number_of_adjacent_mines = gameBoard.neighboringMines(button.x, button.y);
        if(number_of_adjacent_mines == 0)
            collapseZeros(button);
        invisibleClick(button);
    }

    public void invisibleClick(GUIBoardButton button) {
        button.jToggleButton.setIcon(null);
        button.jToggleButton.setSelected(true);
        button.state = ButtonState.Pushed;
        int numberOfMines = getNumberOfMines(button);
        if(numberOfMines !=0)
            button.jToggleButton.setText(String.valueOf(numberOfMines));
    }

    private int getNumberOfMines(GUIBoardButton button) {
        int x = button.x;
        int y = button.y;
        return gameBoard.neighboringMines(x,y);
    }

    public void collapseZeros(GUIBoardButton button) {
        Queue<GUIBoardButton> buttonsQueue = new LinkedList<>();
        buttonsQueue.add(button);
        while (!buttonsQueue.isEmpty()) {
            GUIBoardButton currentButton = buttonsQueue.remove();
            if (getNumberOfMines(currentButton) == 0) {
                int x = currentButton.x;
                int y = currentButton.y;
                addNeighbors(buttonsQueue,x,y);
            }
        }
    }

    private void addNeighbors(Queue<GUIBoardButton> queue, int x, int y) {
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (x + i >= 0 && y + j >= 0 && x + i < boardHeight && y + j < boardWidth) {
                    if (i != 0 || j != 0) {
                        int xNext = x + i;
                        int yNext = y + j;
                        GUIBoardButton next = boardButtons[xNext][yNext];
                        BoardCell cell = gameBoard.getCell(xNext, yNext);
                        if (next.state == ButtonState.None && !cell.isMine()) {
                            queue.add(next);
                            invisibleClick(next);
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

    public void test()
    {
        GUIBoardButton button = boardButtons[0][1];
        invisibleClick(button);
        System.out.println(button.jToggleButton.getName());
    }

    public static void main(String[] args) {
        Runnable r = MinesweeperGui::new;
        SwingUtilities.invokeLater(r);
    }
}