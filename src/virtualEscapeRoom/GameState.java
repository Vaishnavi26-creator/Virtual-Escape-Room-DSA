package virtualEscapeRoom;

public class GameState {

    private int score;
    private int lifelines;

    public GameState() {
        reset();
    }

    public void reset() {
        score = 0;
        lifelines = 3;
    }

    public int getScore() {
        return score;
    }

    public int getLifelines() {
        return lifelines;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLifelines(int lifelines) {
        this.lifelines = lifelines;
    }
}
