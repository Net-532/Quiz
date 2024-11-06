package app;

import model.Question;
import service.QuizService;
import state.MainMenuState;
import state.MenuState;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private MenuState currentState;

    public ConsoleApp() {
        QuizService quizService = new QuizService();
        addTestQuestions(quizService);
        this.currentState = new MainMenuState(quizService);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            currentState.displayMenu();
            String input = scanner.nextLine();
            currentState.handleInput(input);
        }
    }

    private static void addTestQuestions(QuizService quizService) {
        Map<Integer, String> answers1 = new HashMap<>();
        answers1.put(0, "Object-Oriented Programming");
        answers1.put(1, "Functional Programming");
        answers1.put(2, "Procedural Programming");
        quizService.addQuestion(new Question("What is Java?", answers1, 0));

        Map<Integer, String> answers2 = new HashMap<>();
        answers2.put(0, "Inheritance, Encapsulation, Abstraction, Polymorphism");
        answers2.put(1, "Classes, Objects, Methods");
        answers2.put(2, "Loops, Conditions, Arrays");
        quizService.addQuestion(new Question("Which of the following are OOP principles?", answers2, 0));
    }

    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.run();
    }
}
