package state;

import model.Question;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import service.HibernateQuizService;
import service.JdbcQuizService;
import service.QuizService;

import java.util.*;

public class MainMenuState implements MenuState {

    private HibernateQuizService quizService;
    private Scanner scanner = new Scanner(System.in);

    public MainMenuState(HibernateQuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Add question");
        System.out.println("2. Delete question");
        System.out.println("3. Edit question");
        System.out.println("4. View all questions");
        System.out.println("5. Start Quiz");
        System.out.println("6. Find Question");
        System.out.println("7. Exit");
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1" -> addQuestion();
            case "2" -> deleteQuestion();
            case "3" -> editQuestion();
            case "4" -> viewAllQuestions();
            case "5" -> startQuiz();
            case "6" -> viewQuestion();
            case "7" -> {
                System.out.println("Exiting...");
                HibernateQuizService.closeSessionFactory();
                System.exit(0);
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    private void addQuestion() {
        System.out.println("Enter question text:");
        String questionText = scanner.nextLine();

        System.out.println("Enter number of answers:");
        int numAnswers = scanner.nextInt();
        scanner.nextLine();

        Map<Integer, String> answers = new HashMap<>();
        for (int i = 0; i < numAnswers; i++) {
            System.out.println("Enter answer " + (i + 1) + ":");
            answers.put(i, scanner.nextLine());
        }

        System.out.println("Enter the index of the correct answer:");
        int rightAnswer = scanner.nextInt();
        scanner.nextLine();

        Question question = new Question(questionText, answers, rightAnswer);
        quizService.addQuestion(question);
        System.out.println("Question added successfully.");
    }

    private void deleteQuestion() {
        System.out.println("Enter question ID to delete: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        quizService.removeQuestion(questionId);
        System.out.println("Question deleted successfully.");
    }

    private void editQuestion() {
        System.out.println("Enter question ID to edit: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter new text for question:");
        String newText = scanner.nextLine();

        System.out.println("Enter number of answers:");
        int numAnswers = scanner.nextInt();
        scanner.nextLine();

        Map<Integer, String> answers = new HashMap<>();
        for (int i = 0; i < numAnswers; i++) {
            System.out.println("Enter answer " + (i + 1) + ":");
            answers.put(i, scanner.nextLine());
        }

        System.out.println("Enter the index of the correct answer:");
        int rightAnswer = scanner.nextInt();
        scanner.nextLine();

        Question newQuestion = new Question(newText, answers, rightAnswer);
        quizService.editQuestion(questionId, newQuestion);
        System.out.println("Question edited successfully.");
    }

    private void viewAllQuestions() {
        var questions = new HashSet<>(quizService.getAllQuestions());
        if (questions.isEmpty()) {
            System.out.println("No questions available.");
        } else {
            for (Question question : questions) {
                printQuestion(question);
            }
        }
    }

    private void startQuiz() {
        int correctAnswers = 0;
        var questions = quizService.getAllQuestions();

        if (questions.isEmpty()) {
            System.out.println("No questions available to start a quiz.");
            return;
        }

        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            question.getAnswers().forEach((index, answer) -> System.out.println(index + ": " + answer));
            System.out.println("Your answer (enter the number):");

            int userAnswer = scanner.nextInt();
            scanner.nextLine();

            if (question.checkAnswer(userAnswer)) {
                System.out.println("Correct!");
                correctAnswers++;
            } else {
                System.out.println("Wrong! The correct answer is: " + question.getAnswers().get(question.getRightAnswer()));
            }
        }

        System.out.println("Quiz finished! You answered correctly " + correctAnswers + " out of " + questions.size());
    }

    private void viewQuestion() {
        System.out.println("Enter question ID to view: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();

        Question question = quizService.getAllQuestions().stream()
                .filter(q -> q.getId() == questionId)
                .findFirst()
                .orElse(null);

        if (question == null) {
            System.out.println("Question not found.");
        } else {
            printQuestion(question);
        }
    }

    private void printQuestion(Question question) {
        if (!Hibernate.isInitialized(question.getAnswers())) {
            try (Session session = HibernateQuizService.getSessionFactory().openSession()) {
                question = (Question) session.merge(question);
                Hibernate.initialize(question.getAnswers());
            }
        }

        System.out.println("Question ID: " + question.getId());
        System.out.println("Question: " + question.getQuestionText());
        question.getAnswers().forEach((index, answer) -> System.out.println(index + ": " + answer));
    }

}