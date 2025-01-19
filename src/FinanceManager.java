import java.io.*;
import java.util.*;

public class FinanceManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "finance_records.dat";
    private static final Map<String, User> userDatabase = new HashMap<>();
    private static User activeUser;

    public static void main(String[] args) {
        loadUserData();
        while (true) {
            System.out.println("\n1. Войти в систему\n2. Создать аккаунт\n3. Завершить работу");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> signIn();
                case "2" -> signUp();
                case "3" -> {
                    saveUserData();
                    System.out.println("До свидания!");
                    System.exit(0);
                }
                default -> System.out.println("Введите корректный номер действия.");
            }
        }
    }

    private static void signIn() {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (userDatabase.containsKey(login) && userDatabase.get(login).validatePassword(password)) {
            activeUser = userDatabase.get(login);
            System.out.println("Приветствуем, " + activeUser.getUsername() + "!");
            walletOperations();
        } else {
            System.out.println("Неверные данные. Попробуйте снова.");
        }
    }

    private static void signUp() {
        System.out.print("Придумайте логин: ");
        String login = scanner.nextLine();
        if (userDatabase.containsKey(login)) {
            System.out.println("Этот логин уже занят. Попробуйте другой.");
            return;
        }
        System.out.print("Придумайте пароль: ");
        String password = scanner.nextLine();
        userDatabase.put(login, new User(login, password));
        System.out.println("Вы успешно зарегистрировались!");
    }

    private static void walletOperations() {
        while (true) {
            System.out.println("\n1. Добавить доход\n2. Добавить расход\n3. Настроить лимит\n4. Показать отчёт\n5. Назад");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> recordIncome();
                case "2" -> recordExpense();
                case "3" -> setSpendingLimit();
                case "4" -> displayStatistics();
                case "5" -> {
                    activeUser = null;
                    return;
                }
                default -> System.out.println("Выберите действие из предложенного списка.");
            }
        }
    }

    private static void recordIncome() {
        try {
            System.out.print("Введите категорию дохода: ");
            String category = scanner.nextLine();
            System.out.print("Введите сумму: ");
            double amount = Double.parseDouble(scanner.nextLine());
            activeUser.addIncome(category, amount);
            System.out.println("Доход записан.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: сумма должна быть числом.");
        }
    }

    private static void recordExpense() {
        try {
            System.out.print("Введите категорию расхода: ");
            String category = scanner.nextLine();
            System.out.print("Введите сумму: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (activeUser.addExpense(category, amount)) {
                System.out.println("Расход записан.");
            } else {
                System.out.println("Недостаточно средств для выполнения операции.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: сумма должна быть числом.");
        }
    }

    private static void setSpendingLimit() {
        try {
            System.out.print("Категория: ");
            String category = scanner.nextLine();
            System.out.print("Введите лимит: ");
            double limit = Double.parseDouble(scanner.nextLine());
            activeUser.setBudget(category, limit);
            System.out.println("Лимит установлен.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: лимит должен быть числом.");
        }
    }

    private static void displayStatistics() {
        activeUser.showStatistics();
    }

    private static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(userDatabase);
            System.out.println("Данные успешно сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных.");
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadUserData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Map<String, User> loadedUsers = (Map<String, User>) ois.readObject();
                userDatabase.putAll(loadedUsers);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка при загрузке данных.");
            }
        }
    }
}

