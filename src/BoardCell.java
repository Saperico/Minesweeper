public class BoardCell {
    private int value;
    private boolean isMine;
    private int x;
    private int y;

    BoardCell(int x, int y){
        value = 0;
        this.x = x;
        this.y = y;
    }

    public void setMine(){
        isMine = true;
    }

    public boolean isMine(){
        return isMine;
    }

    public int getValue()
    {
        if(isMine)
            return -1;
        return value;
    }

    public String toString()
    {
        return String.valueOf(getValue());
    }
}
