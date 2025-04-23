import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("==== Welcome to the Banking System ====");
            System.out.println("1. Register");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    System.out.print("Full Name: ");
                    String name = scanner.nextLine();

                    String email;
                    while (true) {
                        System.out.print("Email: ");
                        email = scanner.nextLine();
                        if (!AuthService.isValueTaken("email", email)) break;
                        System.out.println("Email already in use. Try another.");
                    }

                    String phone;
                    while (true) {
                        System.out.print("Phone: ");
                        phone = scanner.nextLine();
                        if (!AuthService.isValueTaken("phone", phone)) break;
                        System.out.println("Phone already in use. Try another.");
                    }

                    String username;
                    while (true) {
                        System.out.print("Username: ");
                        username = scanner.nextLine();
                        if (!AuthService.isValueTaken("username", username)) break;
                        System.out.println("Username already taken. Try another.");
                    }

                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    AuthService.register(username, password, name, email, phone, false);
                    break;

                case 2:
                    System.out.print("Username: ");
                    String userUsername = scanner.nextLine();
                    System.out.print("Password: ");
                    String userPassword = scanner.nextLine();

                    User user = AuthService.login(userUsername, userPassword);
                    if (user != null && !user.isAdmin()) {
                        System.out.println("Login successful. Welcome, " + user.getName() + "!");
                        UserPanel.show(scanner, user);
                    } else {
                        System.out.println("Invalid credentials or user not found.");
                    }
                    break;

                case 3:
                    System.out.print("Admin Username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Admin Password: ");
                    String adminPassword = scanner.nextLine();

                    User admin = AuthService.login(adminUsername, adminPassword);
                    if (admin != null && admin.isAdmin()) {
                        System.out.println("Admin login successful. Welcome, " + admin.getName() + "!");
                        AdminPanel.showDashboard();

                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                    break;

                case 4:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
