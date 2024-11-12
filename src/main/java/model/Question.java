package model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @ElementCollection
    @CollectionTable(name = "answer", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "answer_index")
    @Column(name = "answer_text")
    private Map<Integer, String> answers = new HashMap<>();

    @Column(name = "right_answer")
    private int rightAnswer;

    public Question() {}

    public Question(String questionText, Map<Integer, String> answers, int rightAnswer) {
        if (answers.isEmpty() || rightAnswer < 0 || rightAnswer >= answers.size()) {
            throw new IllegalArgumentException("Invalid answers or right answer index.");
        }
        this.questionText = questionText;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
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
}
