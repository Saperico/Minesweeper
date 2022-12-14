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
    GameDifficulty difficulty = GameDifficulty.Easy;
    GameDifficulty[] difficulties = {GameDifficulty.BabyMode, GameDifficulty.Easy , GameDifficulty.Medium, GameDifficulty.High};
    MinesweeperBoard board;
    GUIBoardButton[][] boardButtons;
    ActionListener actionListener;
    boolean rightClick = false;
    boolean firstLeftClick = true;
    int flags = 0;
    int pushed_values;
    JLabel flagsLabel;
    boolean gameIsBeingPlayed = true;

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
    static JComboBox<GameDifficulty> difficultyList;
    static JPanel gui;
    static JPanel pane;
    JFrame mainFrame;

    MinesweeperGui() {
        board = new MinesweeperBoard(difficulty);
        gui = new JPanel(new BorderLayout(0, 0));
        gui.setBorder(new EmptyBorder(4, 4, 4, 4));
        gui.add(createPanel(difficulty));

        pane = new JPanel();
        pane.add(Box.createHorizontalGlue());

        difficultyList = new JComboBox<>(difficulties);
        difficultyList.setSelectedIndex(1);
        difficultyList.addItemListener(e-> itemChanged());
        pane.add(difficultyList);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        pane.add(resetButton);

        JLabel numberOfMines = new JLabel("Mines : "+difficulty.mines);
        pane.add(numberOfMines);

        flagsLabel = new JLabel("Flags : 0");
        pane.add(flagsLabel);



        mainFrame = new JFrame("MineSweeper");
        mainFrame.add(pane, BorderLayout.NORTH);
        mainFrame.add(gui);
        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(320,20));
        mainFrame.setSize(difficulty.getWidth()*30, difficulty.getHeight()*25+60);
        mainFrame.setLocationByPlatform(true);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setVisible(true);

        pushed_values = board.getHeight()*board.getWidth() - board.getNumberOfMines();
    }

    private void boardButtonOnClick(JToggleButton button, boolean wasLeftClicked) {
        GUIBoardButton current = getGUIButton(button);
        if(current.state!= ButtonState.Pushed) {
            if (wasLeftClicked) {
                if(firstLeftClick) {
                    board.firstClick(current.x, current.y);
                    firstLeftClick = false;
                }
                leftClick(current);
            } else {
                rightClick(current);
            }
        }
        else{
            current.jToggleButton.setSelected(true);
        }
    }

    private void itemChanged(){
        difficulty = (GameDifficulty) difficultyList.getSelectedItem();
        mainFrame.remove(gui);
        gui = new JPanel(new BorderLayout(0, 0));
        gui.setBorder(new EmptyBorder(4, 4, 4, 4));
        gui.add(createPanel(difficulty));
        mainFrame.add(gui);
        mainFrame.setSize(difficulty.getWidth()*30, difficulty.getHeight()*25+60);
        mainFrame.repaint();
        resetBoard();
    }

    private JPanel createPanel(GameDifficulty difficulty){
        boardButtons = new GUIBoardButton[difficulty.getHeight()][difficulty.getWidth()];
        JPanel gameContainer = new JPanel(new GridLayout(difficulty.getHeight(), difficulty.getWidth(), 0, 0));
        actionListener = e -> boardButtonOnClick((JToggleButton) e.getSource(), true);
        for (int i = 0; i <difficulty.getHeight(); i++) {
            for (int j = 0; j < difficulty.getWidth(); j++) {
                GUIBoardButton button = new GUIBoardButton(i, j, actionListener);
                button.jToggleButton.addMouseListener(mouseListener);
                boardButtons[i][j] = button;
                gameContainer.add(button.jToggleButton);
            }
        }
        return gameContainer;
    }

    public void leftClick(GUIBoardButton button){
        if (button.state == ButtonState.None) {
            BoardCell curr = board.getCell(button.x, button.y);
            if (curr.isMine()) {
                button.jToggleButton.setSelected(false);
                if(gameIsBeingPlayed)
                    gameEnd();
            }
            else {
                clickedEmpty(button);
            }
        }
    }

    public void gameEnd(){
        for(int i = 0 ; i < board.getHeight(); i ++)
        {
            for(int j = 0 ; j < board.getWidth() ; j++)
            {
                GUIBoardButton current = boardButtons[i][j];
                BoardCell cell = board.getCell(i,j);
                if(cell.isMine())
                    current.setIcon(MyIcon.Mine);
            }
        }
        JOptionPane.showMessageDialog(mainFrame, "You lost! :< ");
        gameIsBeingPlayed = false;
        resetBoard();
    }

    public void rightClick(GUIBoardButton button){
        switch(button.state){
            case None:
                button.state = ButtonState.Flagged;
                button.setIcon(MyIcon.Flag);
                flags+=1;
                break;
            case Flagged:
                button.state = ButtonState.None;
                button.setIcon(MyIcon.None);
                flags-=1;
                break;
            case Pushed:
                break;
        }
        flagsLabel.setText("Flags " + flags);
    }

    private GUIBoardButton getGUIButton(JToggleButton button){
        String name = button.getName();
        String[] coordinates = name.split(":");
        int first = Integer.parseInt(coordinates[0]);
        int second = Integer.parseInt(coordinates[1]);
        return boardButtons[first][second];
    }


    public void clickedEmpty(GUIBoardButton button) {
        int number_of_adjacent_mines = board.neighboringMines(button.x, button.y);
        if(number_of_adjacent_mines == 0)
            collapseZeros(button);
        invisibleClick(button);
    }

    public void invisibleClick(GUIBoardButton button) {
        if(button.state!= ButtonState.Pushed) {
            button.jToggleButton.setIcon(null);
            button.jToggleButton.setSelected(true);
            button.state = ButtonState.Pushed;
            int numberOfMines = getNumberOfMines(button);
            if (numberOfMines != 0)
                button.jToggleButton.setText(String.valueOf(numberOfMines));
            pushed_values -= 1;
            if (pushed_values == 0 && gameIsBeingPlayed)
                gameVictory();
        }
        if(button.state == ButtonState.Flagged)
        {
            button.jToggleButton.setSelected(false);
        }
    }

    private void gameVictory()
    {
        for(int i = 0 ; i < board.getHeight(); i++)
        {
            for(int j = 0 ; j < board.getWidth(); j ++)
            {
                GUIBoardButton button = boardButtons[i][j];
                if(button.state != ButtonState.Pushed) {
                    button.setIcon(MyIcon.Mine);
                    button.state = ButtonState.Flagged;
                }
            }
        }
        JOptionPane.showMessageDialog(mainFrame, "You won!");
        gameIsBeingPlayed = false;
    }

    private int getNumberOfMines(GUIBoardButton button) {
        int x = button.x;
        int y = button.y;
        return board.neighboringMines(x,y);
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
                if (x + i >= 0 && y + j >= 0 && x + i < board.getHeight() && y + j < board.getWidth()) {
                    if (i != 0 || j != 0) {
                        int xNext = x + i;
                        int yNext = y + j;
                        GUIBoardButton next = boardButtons[xNext][yNext];
                        BoardCell cell = board.getCell(xNext, yNext);
                        if (next.state == ButtonState.None && !cell.isMine()) {
                            queue.add(next);
                            invisibleClick(next);
                        }
                    }
                }
    }

    public void resetBoard(){
        board = new MinesweeperBoard(difficulty.getHeight(), difficulty.getWidth(), difficulty.getMines());
        for(int i = 0 ; i < board.getHeight() ; i++)
        {
            for(int j = 0 ; j < board.getWidth() ; j++)
            {
                GUIBoardButton button = boardButtons[i][j];
                button.reset();
            }
        }
        firstLeftClick = true;
        pushed_values = board.getWidth()*board.getHeight() - board.getNumberOfMines();
        flags = 0;
        gameIsBeingPlayed = true;
    }

    public static void main(String[] args) {
        Runnable r = MinesweeperGui::new;
        SwingUtilities.invokeLater(r);
    }
}