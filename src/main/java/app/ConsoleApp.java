package app;

import model.Question;
import service.JdbcQuizService;
import service.JdbcService;
import service.QuizService;
import state.MainMenuState;
import state.MenuState;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private MenuState currentState;

    public ConsoleApp(JdbcQuizService quizService) {
        this.currentState = new MainMenuState(quizService);
    }

    public void run() {
        while (true) {
            currentState.displayMenu();
            String input = new java.util.Scanner(System.in).nextLine();
            currentState.handleInput(input);
        }
    }

    public static void main(String[] args) {
        String connectionString = "jdbc:mysql://localhost/Quiz";
        String user = "workbench";
        String password = "workbench";

        JdbcQuizService quizService = new JdbcQuizService(connectionString, user, password);
        ConsoleApp app = new ConsoleApp(quizService);
        app.run();
    }
}