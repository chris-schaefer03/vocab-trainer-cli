package trainer;

public class StatsManager {
    private int correctCount;
    private int incorrectCount;

    public void addCorrect() {
        correctCount++;
    }

    public void addIncorrect() {
        incorrectCount++;
    }

    public int getCorrect() {
        return correctCount;
    }

    public int getIncorrect() {
        return incorrectCount;
    }

    public int getTotal() {
        return correctCount + incorrectCount;
    }

    public double getAccuracyPercent() {
        int total = getTotal();
        return total == 0 ? 0.0 : (correctCount * 100.0) / total;
    }

    public void reset() {
        correctCount = 0;
        incorrectCount = 0;
    }
}
