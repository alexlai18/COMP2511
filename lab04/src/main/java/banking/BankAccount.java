package banking;
import banking.exceptions.NotEnoughMoneyException;

public class BankAccount {
    String ownerName;
    int balance;

    /**
     * Basic Cconstructor for a bank account
     * @param ownerName
     * @param principal
     * @invarient balance >= 0
     * @pre principal >= 0
     */
    BankAccount(String ownerName, int principal) {
        this.ownerName = ownerName;
        this.balance = principal;
    }

    /**
     * Adds money to bank account
     * @param deposit
     * @pre deposit > 0
     * @invarient balance >= 0
     * @post balance = old balance + deposit amount
     */
    public void deposit(int deposit) {
        this.balance += deposit;
    }

    /**
     * Withdraws money from account
     * @param request
     * @throws NotEnoughMoneyException
     * @pre request > 0
     * @invairnet balance >= 0
     * @post balance = old balance - request
     */
    public void withdraw(int request) throws NotEnoughMoneyException {
        if (this.balance < request) {
            throw new NotEnoughMoneyException("Account balance too low");
        } else {
            this.balance -= request;
        }
    }
}