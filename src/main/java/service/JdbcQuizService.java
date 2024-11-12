package service;

import model.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcQuizService extends JdbcService {

    public JdbcQuizService(String connectionString, String user, String password) {
        super(connectionString, user, password);
    }

    public void addQuestion(Question question) {
        String insertQuestionQuery = "INSERT INTO question (question_text) VALUES (?)";
        String insertAnswerQuery = "INSERT INTO answer (text) VALUES (?)";
        String linkQuestionAnswerQuery = "INSERT INTO question_answers (question_id, answer_id) VALUES (?, ?)";
        String setRightAnswerQuery = "INSERT INTO right_answer (question_id, rigth_answer_id) VALUES (?, ?)";

        try {
            PreparedStatement questionStmt = getConn().prepareStatement(insertQuestionQuery, Statement.RETURN_GENERATED_KEYS);
            questionStmt.setString(1, question.getQuestionText());
            questionStmt.executeUpdate();

            ResultSet questionRs = questionStmt.getGeneratedKeys();
            questionRs.next();
            int questionId = questionRs.getInt(1);

            int rightAnswerId = -1;
            for (Map.Entry<Integer, String> entry : question.getAnswers().entrySet()) {
                PreparedStatement answerStmt = getConn().prepareStatement(insertAnswerQuery, Statement.RETURN_GENERATED_KEYS);
                answerStmt.setString(1, entry.getValue());
                answerStmt.executeUpdate();

                ResultSet answerRs = answerStmt.getGeneratedKeys();
                answerRs.next();
                int answerId = answerRs.getInt(1);

                PreparedStatement linkStmt = getConn().prepareStatement(linkQuestionAnswerQuery);
                linkStmt.setInt(1, questionId);
                linkStmt.setInt(2, answerId);
                linkStmt.executeUpdate();

                if (entry.getKey() == question.getRightAnswer()) {
                    rightAnswerId = answerId;
                }
                answerStmt.close();
                linkStmt.close();
            }

            PreparedStatement rightAnswerStmt = getConn().prepareStatement(setRightAnswerQuery);
            rightAnswerStmt.setInt(1, questionId);
            rightAnswerStmt.setInt(2, rightAnswerId);
            rightAnswerStmt.executeUpdate();

            questionStmt.close();
            rightAnswerStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.id AS question_id, q.question_text, a.id AS answer_id, a.text AS answer_text, " +
                "ra.rigth_answer_id AS correct_answer_id " +
                "FROM question q " +
                "JOIN question_answers qa ON q.id = qa.question_id " +
                "JOIN answer a ON qa.answer_id = a.id " +
                "LEFT JOIN right_answer ra ON q.id = ra.question_id " +
                "ORDER BY q.id, a.id";

        try {
            ResultSet rs = getStmt().executeQuery(query);

            int currentQuestionId = -1;
            String questionText = null;
            Map<Integer, String> answers = new HashMap<>();
            int correctAnswerIndex = -1;
            int answerIndex = 0;

            while (rs.next()) {
                int questionId = rs.getInt("question_id");

                if (questionId != currentQuestionId) {
                    if (currentQuestionId != -1) {
                        questions.add(new Question(questionText, answers, correctAnswerIndex));
                    }

                    currentQuestionId = questionId;
                    questionText = rs.getString("question_text");
                    answers = new HashMap<>();
                    correctAnswerIndex = -1;
                    answerIndex = 0;
                }

                String answerText = rs.getString("answer_text");
                int answerId = rs.getInt("answer_id");
                answers.put(answerIndex, answerText);

                if (answerId == rs.getInt("correct_answer_id")) {
                    correctAnswerIndex = answerIndex;
                }

                answerIndex++;
            }

            if (currentQuestionId != -1 && !answers.isEmpty()) {
                questions.add(new Question(questionText, answers, correctAnswerIndex));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public void removeQuestion(int questionId) {
        String deleteQuestionQuery = "DELETE FROM question WHERE id = ?";
        String deleteAnswersLinkQuery = "DELETE FROM question_answers WHERE question_id = ?";
        String deleteRightAnswerQuery = "DELETE FROM right_answer WHERE question_id = ?";

        try {
            PreparedStatement rightAnswerStmt = getConn().prepareStatement(deleteRightAnswerQuery);
            rightAnswerStmt.setInt(1, questionId);
            rightAnswerStmt.executeUpdate();
            rightAnswerStmt.close();

            PreparedStatement linkStmt = getConn().prepareStatement(deleteAnswersLinkQuery);
            linkStmt.setInt(1, questionId);
            linkStmt.executeUpdate();
            linkStmt.close();

            PreparedStatement questionStmt = getConn().prepareStatement(deleteQuestionQuery);
            questionStmt.setInt(1, questionId);
            questionStmt.executeUpdate();
            questionStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editQuestion(int questionId, Question newQuestion) {
        removeQuestion(questionId);
        addQuestion(newQuestion);
    }
}
