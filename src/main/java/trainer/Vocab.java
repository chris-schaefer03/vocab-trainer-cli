package trainer;

import java.time.LocalDate;

public class Vocab {
    private String deutsch;
    private String albanisch;

    private int repetitions = 0;
    private int interval = 1;
    private double easeFactor = 2.5;
    private LocalDate nextReviewDate = LocalDate.now();
    private int correctInErrorQuizCount = 0;

    public Vocab(String deutsch, String albanisch) {
        this.deutsch = deutsch;
        this.albanisch = albanisch;
        this.nextReviewDate = LocalDate.now();
    }

    public String getDeutsch() {
        return deutsch;
    }

    public String getAlbanisch() {
        return albanisch;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getInterval() {
        return interval;
    }

    public double getEaseFactor() {
        return easeFactor;
    }

    public LocalDate getNextReviewDate() {
        return nextReviewDate;
    }

    public int getCorrectInErrorQuizCount() {
        return correctInErrorQuizCount;
    }

    public void incrementCorrectInErrorQuizCount() {
        correctInErrorQuizCount++;
    }

    public void resetCorrectInErrorQuizCount() {
        correctInErrorQuizCount = 0;
    }

    public void updateAfterAnswer(boolean correct) {
        if (correct) {
            repetitions++;
            easeFactor = Math.max(1.3, easeFactor + 0.1);
            if (repetitions == 1) {
                interval = 1;
            } else if (repetitions == 2) {
                interval = 6;
            } else {
                interval = (int) Math.round(interval * easeFactor);
            }
        } else {
            repetitions = 0;
            interval = 1;
            easeFactor = Math.max(1.3, easeFactor - 0.2);
        }

        nextReviewDate = LocalDate.now().plusDays(interval);
    }

    @Override
    public String toString() {
        return deutsch + " → " + albanisch;
    }

    public String toDetailedString() {
        return deutsch + " → " + albanisch + " (Nächste Wiederholung: " + getNextReviewDate() + ")";
    }
}