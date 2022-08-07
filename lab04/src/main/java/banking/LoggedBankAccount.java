package banking;
import java.util.ArrayList;
import java.util.List;
import banking.exceptions.NotEnoughMoneyException;

public class LoggedBankAccount extends BankAccount {
    List<String> actions = new ArrayList<String>();

    LoggedBankAccount(String ownerName, int principal) {
        super(ownerName, principal);
    }

    /**
     * Adds money to bank account and logs the change
     * @param deposit
     * @pre deposit > 0
     * @invarient balance >= 0
     * @post balance = old balance + deposit amount and also logs change
     */
    @Override
    public void deposit(int deposit) {
        super.deposit(deposit);
        actions.add(this.ownerName + " has deposited $" + deposit);
    }


    /**
     * Withdraws money from account
     * @param request
     * @throws NotEnoughMoneyException
     * @pre request > 0
     * @invarient balance >= 0
     * @post balance = old balance - request
     */
    @Override
    public void withdraw(int request) throws NotEnoughMoneyException {
        try {
            super.withdraw(request);
            actions.add(this.ownerName + " has withdrawn $" + request);
        } catch (NotEnoughMoneyException e1) {
            actions.add(this.ownerName + " tried to withdraw $" + request + "but did not have enough");
        }

    }
}
