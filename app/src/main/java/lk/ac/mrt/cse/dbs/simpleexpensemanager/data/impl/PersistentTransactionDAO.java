package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Pushpitha on 19-Nov-16.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions;
    private DatabaseHelperLogs dbhl;
    private SQLiteDatabase sqldb;

    public PersistentTransactionDAO(DatabaseHelperLogs dbhl) {
        transactions = new LinkedList<>();
        this.dbhl = dbhl;

        // load data into the linked list when object is created
        sqldb = dbhl.getReadableDatabase();
        Cursor c = sqldb.rawQuery("SELECT * FROM logs;",null);
        if(c.moveToFirst()){
            do{
                // handle date format
                if(c.getString(2).equals(ExpenseType.INCOME.toString())) {
                    transactions.add(new Transaction(new Date(c.getString(0)) , c.getString(1), ExpenseType.INCOME, Double.parseDouble(c.getString(3))));
                }
                else{
                    transactions.add(new Transaction(new Date(c.getString(0)), c.getString(1), ExpenseType.EXPENSE, Double.parseDouble(c.getString(3))));
                }
            }while (c.moveToNext());
        }
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);

        // add to the database
        sqldb = dbhl.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperLogs.LOGS_DATE,date.toString());
        values.put(DatabaseHelperLogs.LOGS_ACNO,accountNo);
        values.put(DatabaseHelperLogs.LOGS_TYPE,expenseType.toString());
        values.put(DatabaseHelperLogs.LOGS_AMOUNT,amount);

        long rowid = sqldb.insert(DatabaseHelperLogs.TABLE_LOGS,null,values);
        System.out.println("row addded "+rowid);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }





}
