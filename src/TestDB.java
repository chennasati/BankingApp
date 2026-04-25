import java.sql.*;
import java.util.Scanner;

public class TestDB {

    static final String URL = "jdbc:sqlite:bank.db";

    public static void main(String[] args) throws Exception {

        Connection con = DriverManager.getConnection(URL);
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n====== BANK MENU ======");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Transaction History");
            System.out.println("6. Transfer Money");
            System.out.println("7. Delete Account");
            System.out.println("8. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1: createAccount(con, sc); break;
                case 2: deposit(con, sc); break;
                case 3: withdraw(con, sc); break;
                case 4: checkBalance(con, sc); break;
                case 5: showTransactions(con, sc); break;
                case 6: transferMoney(con, sc); break;
                case 7: deleteAccount(con, sc); break;
                case 8:
                    System.out.println("Thank you!");
                    System.exit(0);
            }
        }
    }

    // ✅ Create Account
    static void createAccount(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Set PIN: ");
        int pin = sc.nextInt();

        String sql = "INSERT INTO accounts(name, balance, pin) VALUES(?, 0, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, pin);
        ps.executeUpdate();

        System.out.println("Account created successfully!");
    }

    // ✅ Deposit
    static void deposit(Connection con, Scanner sc) throws Exception {
        System.out.print("Account Number: ");
        int acc = sc.nextInt();

        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE account_number = ?");
        ps.setDouble(1, amount);
        ps.setInt(2, acc);
        ps.executeUpdate();

        PreparedStatement t = con.prepareStatement(
                "INSERT INTO transactions(account_number, type, amount) VALUES(?, 'DEPOSIT', ?)");
        t.setInt(1, acc);
        t.setDouble(2, amount);
        t.executeUpdate();

        System.out.println("Deposit successful!");
    }

    // ✅ Withdraw
    static void withdraw(Connection con, Scanner sc) throws Exception {
        System.out.print("Account Number: ");
        int acc = sc.nextInt();

        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE account_number=?");
        ps.setInt(1, acc);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");

            if (balance >= amount) {

                PreparedStatement ps2 = con.prepareStatement(
                        "UPDATE accounts SET balance = balance - ? WHERE account_number=?");
                ps2.setDouble(1, amount);
                ps2.setInt(2, acc);
                ps2.executeUpdate();

                PreparedStatement t = con.prepareStatement(
                        "INSERT INTO transactions(account_number, type, amount) VALUES(?, 'WITHDRAW', ?)");
                t.setInt(1, acc);
                t.setDouble(2, amount);
                t.executeUpdate();

                System.out.println("Withdraw successful!");
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    // ✅ Check Balance
    static void checkBalance(Connection con, Scanner sc) throws Exception {
        System.out.print("Account Number: ");
        int acc = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE account_number=?");
        ps.setInt(1, acc);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Balance: " + rs.getDouble("balance"));
        } else {
            System.out.println("Account not found!");
        }
    }

    // ✅ Transaction History
    static void showTransactions(Connection con, Scanner sc) throws Exception {
        System.out.print("Account Number: ");
        int acc = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM transactions WHERE account_number=?");
        ps.setInt(1, acc);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Transaction History ---");

        while (rs.next()) {
            System.out.println(
                    rs.getInt("id") + " | " +
                            rs.getString("type") + " | " +
                            rs.getDouble("amount") + " | " +
                            rs.getString("date")
            );
        }
    }

    // ✅ Transfer Money
    static void transferMoney(Connection con, Scanner sc) throws Exception {

        System.out.print("From Account: ");
        int from = sc.nextInt();

        System.out.print("To Account: ");
        int to = sc.nextInt();

        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE account_number=?");
        ps.setInt(1, from);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");

            if (balance >= amount) {

                con.setAutoCommit(false);

                PreparedStatement p1 = con.prepareStatement(
                        "UPDATE accounts SET balance = balance - ? WHERE account_number=?");
                p1.setDouble(1, amount);
                p1.setInt(2, from);
                p1.executeUpdate();

                PreparedStatement p2 = con.prepareStatement(
                        "UPDATE accounts SET balance = balance + ? WHERE account_number=?");
                p2.setDouble(1, amount);
                p2.setInt(2, to);
                p2.executeUpdate();

                PreparedStatement t1 = con.prepareStatement(
                        "INSERT INTO transactions(account_number, type, amount) VALUES(?, 'TRANSFER_OUT', ?)");
                t1.setInt(1, from);
                t1.setDouble(2, amount);
                t1.executeUpdate();

                PreparedStatement t2 = con.prepareStatement(
                        "INSERT INTO transactions(account_number, type, amount) VALUES(?, 'TRANSFER_IN', ?)");
                t2.setInt(1, to);
                t2.setDouble(2, amount);
                t2.executeUpdate();

                con.commit();
                con.setAutoCommit(true);

                System.out.println("Transfer successful!");
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Invalid account!");
        }
    }

    // ✅ Delete Account
    static void deleteAccount(Connection con, Scanner sc) throws Exception {

        System.out.print("Account Number: ");
        int acc = sc.nextInt();

        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM accounts WHERE account_number=? AND pin=?");
        ps.setInt(1, acc);
        ps.setInt(2, pin);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            PreparedStatement t = con.prepareStatement(
                    "DELETE FROM transactions WHERE account_number=?");
            t.setInt(1, acc);
            t.executeUpdate();

            PreparedStatement d = con.prepareStatement(
                    "DELETE FROM accounts WHERE account_number=?");
            d.setInt(1, acc);
            d.executeUpdate();

            System.out.println("Account deleted successfully!");
        } else {
            System.out.println("Invalid account or PIN!");
        }
    }
}