package model;

import java.util.HashMap;
import java.util.Map;

public class Question {
    private int id;
    private String questionText;
    private Map<Integer, String> answers = new HashMap<>();
    private int rightAnswer;

    public Question(int id, String questionText, Map<Integer, String> answers, int rightAnswer) {
        if (answers.isEmpty() || rightAnswer < 0 || rightAnswer >= answers.size()) {
            throw new IllegalArgumentException("Invalid answers or right answer index.");
        }

        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public Question(String questionText, Map<Integer, String> answers, int rightAnswer) {
        this(0, questionText, answers, rightAnswer);
    }

    public int getId() {
        return id;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public boolean checkAnswer(int answer) {
        return this.rightAnswer == answer;
    }

    public void editQuestionText(String newText) {
        if (newText == null || newText.isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        this.questionText = newText;
    }

    public void editRightAnswer(int rightAnswer) {
        if (rightAnswer < 0 || rightAnswer >= answers.size()) {
            throw new IllegalArgumentException("Invalid right answer index.");
        }
        this.rightAnswer = rightAnswer;
    }

    public void editAnswers(Map<Integer, String> answers) {
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("Answers cannot be null.");
        }
        this.answers = answers;
    }

    public static Question createTestQuestion() {
        Map<Integer, String> answers = new HashMap<>();
        answers.put(0, "Answer A");
        answers.put(1, "Answer B");
        answers.put(2, "Answer C");
        return new Question("What is Java?", answers, 1);
    }
}
