import java.io.*;
import java.util.*;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final Map<String, Double> incomes = new HashMap<>();
    private final Map<String, Double> expenseRecords = new HashMap<>();
    private final Map<String, Double> budgetLimits = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public void recordIncome(String category, double amount) {
        if (amount <= 0) {
            System.out.println("Ошибка: сумма дохода должна быть больше нуля.");
            return;
        }
        incomes.put(category, incomes.getOrDefault(category, 0.0) + amount);
    }

    public boolean recordExpense(String category, double amount) {
        if (amount <= 0) {
            System.out.println("Ошибка: сумма расхода должна быть больше нуля.");
            return false;
        }

        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenseRecords.values().stream().mapToDouble(Double::doubleValue).sum();

        if (totalIncome - totalExpenses >= amount) {
            expenseRecords.put(category, expenseRecords.getOrDefault(category, 0.0) + amount);
            return true;
        } else {
            System.out.println("Недостаточно средств для этой операции.");
            return false;
        }
    }

    public void defineBudget(String category, double budget) {
        if (budget <= 0) {
            System.out.println("Ошибка: бюджет должен быть больше нуля.");
            return;
        }
        budgetLimits.put(category, budget);
    }

    public void displayReport() {
        System.out.println("\n--- Отчёт пользователя: " + username + " ---");
        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenseRecords.values().stream().mapToDouble(Double::doubleValue).sum();

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + totalExpenses);
        System.out.println("Остаток средств: " + (totalIncome - totalExpenses));

        System.out.println("\nБюджеты по категориям:");
        for (String category : budgetLimits.keySet()) {
            double budget = budgetLimits.get(category);
            double spent = expenseRecords.getOrDefault(category, 0.0);
            double remaining = budget - spent;
            double percentageSpent = (spent / budget) * 100;

            System.out.printf("Категория: %s\n", category);
            System.out.printf("  Бюджет: %.2f\n  Потрачено: %.2f\n  Остаток: %.2f\n  Процент расхода: %.2f%%\n",
                    budget, spent, remaining, percentageSpent > 100 ? 100 : percentageSpent);
        }
    }
}

