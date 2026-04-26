# 🏦 Banking System (Java + SQL using JDBC) 


## 📌 Overview

This is a console-based banking application built using Java and SQLite.
The system allows users to perform basic banking operations such as account creation, deposits, withdrawals, transfers, and transaction tracking.

---

## 🚀 Features

* 👤 Create account
* 💰 Deposit money
* 💸 Withdraw money
* 🔍 Check balance
* 🔁 Transfer money between accounts
* 📄 View transaction history
* ❌ Delete account

---

## 🛠️ Technologies Used

* ☕ Java
* 🔌 JDBC
* 🗄️ SQLite
* 💻 IntelliJ IDEA

---

## 🗄️ Database Structure

### 📊 Accounts Table

| Column         | Type    |
| -------------- | ------- |
| account_number | INTEGER |
| name           | TEXT    |
| balance        | REAL    |
| pin            | INTEGER |

### 📊 Transactions Table

| Column         | Type     |
| -------------- | -------- |
| id             | INTEGER  |
| account_number | INTEGER  |
| type           | TEXT     |
| amount         | REAL     ||

---

## ⚙️ How to Run

1. Clone the repository
```
git clone https://github.com/your-username/Banking-System-Java.git
```

2. Open the project in IntelliJ IDEA

3. Add SQLite JDBC driver (.jar file)

4. Run `TestDB.java`

---

## ▶️ Sample Menu

```
====== BANK MENU ======
1. Create Account
2. Deposit
3. Withdraw
4. Check Balance
5. Transaction History
6. Transfer Money
7. Delete Account
8. Exit
```

## 📌 Notes

* Database (`bank.db`) is created automatically
* Transactions are stored with timestamp
* Uses PreparedStatement for secure queries

---

## 👤 Author

* Chenna Venkata Satish


