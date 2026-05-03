package models;

public class Player {

    private int gameId;
    private String displayName;
    private String imagePath;
    private int score;

    public Player(int gameId, String displayName, String imagePath) {
        this.gameId = gameId;
        this.displayName = displayName;
        this.imagePath = imagePath;
        this.score = 0;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
