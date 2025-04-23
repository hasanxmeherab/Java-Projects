import java.util.Scanner;

public class UserPanel {
    public static void show(Scanner scanner, User user) {
        int choice;
        do {
            System.out.println("==== User Panel ====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. View Transaction History");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = Double.parseDouble(scanner.nextLine());
                    AuthService.deposit(user.getId(), depositAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = Double.parseDouble(scanner.nextLine());
                    AuthService.withdraw(user.getId(), withdrawAmount);
                    break;
                case 3:
                    double balance = AuthService.getBalance(user.getId());
                    System.out.println("Your current balance is: " + balance);
                    break;
                case 4:
                    AuthService.showTransactionHistory(user.getId());
                    break;
                case 5:
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 5);
    }
}
