import java.util.HashSet;
import java.util.Set;

public class MinesweeperBoard {
    private final int height;
    private final int width;
    private final BoardCell[][] gameBoard;
    private int numberOfMines;

    public MinesweeperBoard(int height, int width, int mines)
    {
        this.height = height;
        this.width = width;
        this.gameBoard = new BoardCell[height][width];
        numberOfMines = mines;
        setUpBoard();
        pickMines();
    }

    public MinesweeperBoard(GameDifficulty difficulty)
    {
        this.height = difficulty.getHeight();
        this.width = difficulty.getWidth();
        this.gameBoard = new BoardCell[height][width];
        numberOfMines = difficulty.getMines();
        setUpBoard();
        pickMines();
    }

    private void pickMines(){
        int added = 0;
        Set<Pair> pairs = new HashSet<>();
        while(added<numberOfMines) {
            int randomX = (int) (Math.random() * height);
            int randomY = (int) (Math.random() * width);
            Pair current = new Pair(randomX, randomY);
            if(!pairs.contains(current))
            {
                gameBoard[randomX][randomY].setMine(true);
                pairs.add(current);
                added++;
            }
        }
    }

    private void setUpBoard(){
        for(int i = 0 ; i < height ; i ++)
            for (int j = 0; j < width; j++)
                gameBoard[i][j] = new BoardCell();
    }

    public void firstClick(int x, int y){
        for(int i = -1 ; i < 2 ; i++)
        {
            for(int j = -1; j<2 ; j++)
            {
                int x_p = x+i;
                int y_p = y+j;
                if(x_p >= 0 & x_p < height & y_p >= 0 & y_p < width)
                {
                    BoardCell current = getCell(x_p,y_p);
                    if(current.isMine()){
                        current.setMine(false);
                        moveMine(x,y);
                    }
                }
            }
        }
    }

    private void moveMine(int x, int y){
        boolean foundPlace = false;
        while(!foundPlace)
        {
            int randomX = (int) (Math.random() * height);
            int randomY = (int) (Math.random() * width);
            boolean isMine = gameBoard[randomX][randomY].isMine();
            if(Math.abs(randomX - x)>1 && Math.abs(randomY - y) > 1 && !isMine)
            {
                gameBoard[randomX][randomY].setMine(true);
                foundPlace = true;
            }
        }
    }

    public void showBoard(){
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String output = gameBoard[i][j].toString();
                System.out.print(output + " ");
            }
            System.out.println();
        }
    }

    public int neighboringMines(int x, int y){
        int numberOfNeighbors = 0;
        for(int i = -1 ; i < 2; i++){
            for(int j = -1 ; j < 2; j++){
                if(i!=0 || j != 0)
                    if(x+i < height & x+i >= 0 & y+j < width & y+j>=0){
                        BoardCell current = gameBoard[x+i][y+j];
                        if(current.isMine()){
                            numberOfNeighbors++;
                        }
                    }
            }
        }
        return numberOfNeighbors;
    }

    public BoardCell getCell(int x, int y){
        return this.gameBoard[x][y];
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getMines(){
        return this.numberOfMines;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    private static class Pair{
        int first;
        int second;

        public Pair(int x, int y)
        {
            first = x;
            second =y;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Pair)
            {
                return this.first == ((Pair) obj).first && this.second == ((Pair) obj).second;
            }
            return false;
        }
    }

    public static void main(String[] args){
        MinesweeperBoard board2 = new MinesweeperBoard(10,5,2);
        board2.showBoard();
    }
}
