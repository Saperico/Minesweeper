public class BoardCell {
    private boolean isMine;

    BoardCell(){
    }

    public void setMine(boolean isMine){
        this.isMine = isMine;
    }

    public boolean isMine(){
        return isMine;
    }
}
