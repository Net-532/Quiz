package app;

import model.Question;
import service.HibernateQuizService;
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

    public ConsoleApp(HibernateQuizService quizService) {
        this.currentState = new MainMenuState(quizService);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            currentState.displayMenu();
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();
            currentState.handleInput(input);
        }
    }

    public static void main(String[] args) {
        HibernateQuizService quizService = new HibernateQuizService();

        ConsoleApp app = new ConsoleApp(quizService);

        app.run();

        HibernateQuizService.closeSessionFactory();
    }

}