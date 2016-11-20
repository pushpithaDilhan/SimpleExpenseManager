package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.ManageExpensesFragment;

/**
 * Created by Pushpitha on 19-Nov-16.
 */
public class PersistentAccountDAO implements AccountDAO {
    private final Map<String, Account> accounts;
    private DatabaseHelper dbh;
    private SQLiteDatabase sqldb ;

    public PersistentAccountDAO(DatabaseHelper dbh) {
        this.accounts = new HashMap<>();
        this.dbh = dbh ;

    }

    @Override
    public List<String> getAccountNumbersList() {
        //load keys in database
        sqldb = dbh.getReadableDatabase();
        Cursor c = sqldb.rawQuery("SELECT * FROM account;",null);

        if(c.moveToFirst()){
            do{
                accounts.put(c.getString(0),new Account(c.getString(0),c.getString(1),c.getString(2), Double.parseDouble(c.getString(3))));

            }while (c.moveToNext());
        }
        return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)) {
            return accounts.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        accounts.put(account.getAccountNo(), account);

        // write to database

        sqldb = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.AC_ACNO, account.getAccountNo());
        values.put(DatabaseHelper.AC_BANK, account.getBankName());
        values.put(DatabaseHelper.AC_ACHOLDER, account.getAccountHolderName());
        values.put(DatabaseHelper.AC_BALANCE, account.getBalance());

        // insert row
        long row_id = sqldb.insert(DatabaseHelper.TABLE_ACCOUNT, null, values);

        sqldb.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        accounts.remove(accountNo);

        // remove from database
        sqldb = dbh.getWritableDatabase();
        sqldb.delete(DatabaseHelper.TABLE_ACCOUNT, DatabaseHelper.AC_ACNO + " = ?",
                new String[] { accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        accounts.put(accountNo, account);

        // update database table
        sqldb = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.AC_ACNO, account.getAccountNo());
        values.put(DatabaseHelper.AC_BANK, account.getBankName());
        values.put(DatabaseHelper.AC_ACHOLDER, account.getAccountHolderName());
        values.put(DatabaseHelper.AC_BALANCE, account.getBalance());

        // updating row
        sqldb.update(DatabaseHelper.TABLE_ACCOUNT, values, DatabaseHelper.AC_ACNO + " = ?",
                new String[] { accountNo });

        System.out.println("row updated");

    }
}