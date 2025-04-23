import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.Scanner;

public class AuthService {

    public static void register(String username, String password, String name, String email, String phone, boolean isAdmin) {
        try (Connection conn = DBConnection.getConnection()) {
            // Generate 12-digit account number
            String accountNumber = generateAccountNumber();

            String insertUserQuery = "INSERT INTO users (username, password, name, email, phone, is_admin, account_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertUserQuery);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setBoolean(6, isAdmin);
            stmt.setString(7, accountNumber);
            stmt.executeUpdate();

            System.out.println("Registered successfully! Your account number is: " + accountNumber);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Registration failed.");
        }
    }

    public static User login(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getBoolean("is_admin")
                );
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValueTaken(String column, String value) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE " + column + " = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true; // assume taken if error
        }
    }

    private static String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }

    // TransactionService methods
    public static void deposit(int userId, double amount) {
        try (Connection conn = DBConnection.getConnection()) {
            String insert = "INSERT INTO transactions (user_id, type, amount) VALUES (?, 'deposit', ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
            System.out.println("Deposit successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(int userId, double amount) {
        try (Connection conn = DBConnection.getConnection()) {
            // Check current balance
            double balance = getBalance(userId);
            if (balance < amount) {
                System.out.println("Insufficient funds.");
                return;
            }

            String insert = "INSERT INTO transactions (user_id, type, amount) VALUES (?, 'withdraw', ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
            System.out.println("Withdrawal successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getBalance(int userId) {
        double balance = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT type, amount FROM transactions WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("type").equals("deposit")) {
                    balance += rs.getDouble("amount");
                } else if (rs.getString("type").equals("withdraw")) {
                    balance -= rs.getDouble("amount");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    public static void showTransactionHistory(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("==== Transaction History ====");
            while (rs.next()) {
                System.out.printf("%s | %s | %.2f\n", rs.getTimestamp("timestamp"), rs.getString("type"), rs.getDouble("amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
