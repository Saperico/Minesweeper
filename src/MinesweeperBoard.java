import java.util.Random;

public class MinesweeperBoard {
    private final int height;
    private final int width;
    private final BoardCell[][] gameBoard;

    public MinesweeperBoard(int height, int width){
        this.height = height;
        this.width = width;
        this.gameBoard = new BoardCell[height][width];
        setUpBoard();
    }

    private void setUpBoard(){
        for(int i = 0 ; i < height ; i ++)
            for (int j = 0; j < width; j++)
                gameBoard[i][j] = new BoardCell(i, j);
        randomBoard();
    }

    public void randomBoard(){
        for(int i = 0 ; i < height ; i++)
        {
            for(int j = 0 ; j < width ; j++)
            {
                double random = new Random().nextDouble();
                if(random<0.5)
                    gameBoard[i][j].setMine();
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

    private int numberOfNeighbors(int x, int y){
        int numberOfNeighbors = 0;
        for(int i = -1 ; i < 2; i++){
            for(int j = -1 ; j < 2; j++){
                if(i!=0 || j != 0)
                    if(x+i < height & x+i >= 0 & y+j < width & y+j>=0){
                        BoardCell current = gameBoard[x+i][y+j];
                        if(!current.isMine()){
                            numberOfNeighbors++;
                        }
                    }
            }
        }
        return numberOfNeighbors;
    }

    public BoardCell getValue(int x, int y){
        return this.gameBoard[x][y];
    }

    public void resetBoard(){
        setUpBoard();
    }

    public static void main(String[] args){
        MinesweeperBoard board = new MinesweeperBoard(10,5);
        board.showBoard();
    }
}
