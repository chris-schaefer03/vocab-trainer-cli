package trainer;

import java.util.List;

public class AnswerCheckResult {
    public final boolean isCorrect;
    public final List<String> otherPossibleAnswers;

    public AnswerCheckResult(boolean isCorrect, List<String> otherPossibleAnswers) {
        this.isCorrect = isCorrect;
        this.otherPossibleAnswers = otherPossibleAnswers;
    }
}