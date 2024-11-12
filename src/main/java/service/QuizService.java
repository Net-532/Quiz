package service;

import model.Question;

import java.util.ArrayList;
import java.util.List;


public class QuizService {
    private final List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

    public Question findQuestionByIndex(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new IllegalArgumentException("Index can't be higher then actual question list.");
        }
        return questions.get(index);
    }

    public void removeQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
        questions.remove(index);
    }

    public void editQuestion(int index, Question newQuestion) {
        Question q = findQuestionByIndex(index);

        q.editQuestionText(newQuestion.getQuestionText());
        q.editAnswers(newQuestion.getAnswers());
        q.editRightAnswer(newQuestion.getRightAnswer());
    }
}
