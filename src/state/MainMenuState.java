package state;

import model.Question;
import service.QuizService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

public class MainMenuState implements MenuState {

    private QuizService quizService;
    private Scanner scanner = new Scanner(System.in);

    public MainMenuState(QuizService quizService) {
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
        System.out.println("6. Find Question.");
        System.out.println("7. Exit");
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1" -> addQuestion();
            case "2" -> deleteQuestion();
            case "3" -> editQuestion();
            case "4"-> viewAllQuestions();
            case "5" -> startQuiz();
            case "6" -> viewQuestion();
            case "7"  -> System.exit(0);
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
        System.out.println("Enter question id: ");
        int requestedQuestion = scanner.nextInt();

        if (quizService.findQuestionByIndex(requestedQuestion) == null) {
            throw new IllegalArgumentException("Wrong index of question.");
        }

        quizService.removeQuestion(requestedQuestion);
    }

    private void editQuestion() {
        System.out.println("Enter question id: ");
        int requestedQuestion = scanner.nextInt();
        scanner.nextLine();
        if (requestedQuestion > quizService.getAllQuestions().size()) {
            throw new IllegalArgumentException("Invalid question id.");
        }

        System.out.println("Enter new text for question: ");
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

        Question question = new Question(newText, answers, rightAnswer);
        quizService.editQuestion(requestedQuestion, question);
        System.out.println("Question edited successfully.");

    }

    private void printQuestion(Question q) {
        System.out.println(q.getQuestionText());
        Map<Integer, String> answers = q.getAnswers();
        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void viewAllQuestions() {
        List<Question> allQuestions = quizService.getAllQuestions();
        for (Question q : allQuestions) {
            System.out.println(q.getQuestionText());
            Map<Integer, String> answers = q.getAnswers();
            for (Map.Entry<Integer, String> entry : answers.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    private void startQuiz() {
        int correctAnswers = 0;
        int totalQuestions = quizService.getAllQuestions().size();

        for (Question question : quizService.getAllQuestions()) {
            System.out.println(question.getQuestionText());
            question.getAnswers().forEach((index, answer) -> System.out.println(index + ": " + answer));
            System.out.println("Your answer (enter the number):");
            int userAnswer = scanner.nextInt();

            if (question.checkAnswer(userAnswer)) {
                System.out.println("Correct!");
                correctAnswers++;
            } else {
                System.out.println("Wrong! The correct answer is: " + question.getAnswers().get(question.getRightAnswer()));
            }
        }

        System.out.println("Quiz finished! You answered correctly " + correctAnswers + " out of " + totalQuestions);
    }

    private void viewQuestion() {
        System.out.println("Enter question id: ");
        int requestedQuestion = Integer.parseInt(scanner.nextLine());

        if (requestedQuestion > quizService.getAllQuestions().size()) {
            throw new IllegalArgumentException("Invalid question id.");
        }

        Question q = quizService.getAllQuestions().get(requestedQuestion);

        printQuestion(q);
    }
}
