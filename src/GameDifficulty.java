public enum GameDifficulty {
    BabyMode(4,4,1),
    Easy(5,8,10),
    Medium(10,12,50),
    High(15,20,100);
    final int height;
    final int width;
    final int mines;
    GameDifficulty(int height, int width, int mines){
        this.height = height;
        this.width = width;
        this.mines = mines;
    }

    public int getHeight()
    {

        return height;
    }
    public int getWidth(){
        return this.width;
    }
    public int getMines(){
        return this.mines;
    }
}
