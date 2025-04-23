import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.Statement;
//import java.sql.SQLException;


public class AdminPanel {
    private static Scanner scanner = new Scanner(System.in);

    public static void showDashboard() {
        while (true) {
            System.out.println("\n===== Admin Dashboard =====");
            System.out.println("1. View All Users");
            System.out.println("2. Search User by Account Number");
            System.out.println("3. Delete or Block a User");
            System.out.println("4. Reset User Password");
            System.out.println("5. Export User Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    searchUserByAccountNumber();
                    break;
                case 3:
                    deleteOrBlockUser();
                    break;
                case 4:
                    resetUserPassword();
                    break;
                case 5:
                    exportTransactionHistory();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void viewAllUsers() {
        System.out.println("\n--- Registered Users ---");
    
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, username, email, phone, account_number FROM users")) {
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String accountNumber = rs.getString("account_number");
    
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println("Account No: " + accountNumber);
                System.out.println("--------------------------");
            }
    
        } catch (Exception e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }
    

    private static void searchUserByAccountNumber() {
    System.out.print("Enter account number: ");
    String accNo = scanner.nextLine();

    String query = "SELECT * FROM users WHERE account_number = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, accNo);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Username: " + rs.getString("username"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Phone: " + rs.getString("phone"));
            System.out.println("Is Admin: " + rs.getBoolean("is_admin"));
        } else {
            System.out.println("User not found.");
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}


private static void deleteOrBlockUser() {
    System.out.print("Enter user ID to delete: ");
    int userId = Integer.parseInt(scanner.nextLine());

    String query = "DELETE FROM users WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, userId);
        int rows = stmt.executeUpdate();

        if (rows > 0) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    } catch (Exception e) {
        System.out.println("Error deleting user: " + e.getMessage());
    }
}


private static void resetUserPassword() {
    System.out.print("Enter user ID to reset password: ");
    int userId = Integer.parseInt(scanner.nextLine());
    System.out.print("Enter new password: ");
    String newPassword = scanner.nextLine();

    String query = "UPDATE users SET password = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, newPassword); // For production, hash the password!
        stmt.setInt(2, userId);
        int rows = stmt.executeUpdate();

        if (rows > 0) {
            System.out.println("Password reset successfully.");
        } else {
            System.out.println("User not found.");
        }
    } catch (Exception e) {
        System.out.println("Error resetting password: " + e.getMessage());
    }
}


private static void exportTransactionHistory() {
    System.out.print("Enter account number: ");
    String accNo = scanner.nextLine();

    String getUserIdQuery = "SELECT id FROM users WHERE account_number = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement getUserStmt = conn.prepareStatement(getUserIdQuery)) {

        getUserStmt.setString(1, accNo);
        ResultSet rs = getUserStmt.executeQuery();

        if (!rs.next()) {
            System.out.println("Account not found.");
            return;
        }

        int userId = rs.getInt("id");

        String txnQuery = "SELECT * FROM transactions WHERE user_id = ?";
        PreparedStatement txnStmt = conn.prepareStatement(txnQuery);
        txnStmt.setInt(1, userId);
        ResultSet txnRs = txnStmt.executeQuery();

        String filename = "transactions_user_" + userId + ".csv";
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("Timestamp,Type,Amount\n");
            while (txnRs.next()) {
                writer.write(txnRs.getTimestamp("timestamp") + "," +
                             txnRs.getString("type") + "," +
                             txnRs.getDouble("amount") + "\n");
            }
            System.out.println("Transaction history exported to: " + filename);
        }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

}
