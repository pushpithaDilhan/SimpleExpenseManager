package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseHelperLogs;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Pushpitha on 19-Nov-16.
 */
public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context = context;
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {

        DatabaseHelper dbh = new DatabaseHelper(context,DatabaseHelper.DATABASE_NAME,null,DatabaseHelper.DATABASE_VERSION);
        DatabaseHelperLogs dbhl = new DatabaseHelperLogs(context,DatabaseHelperLogs.DATABASE_NAME,null,DatabaseHelperLogs.DATABASE_VERSION);

        TransactionDAO inMemoryTransactionDAO = new PersistentTransactionDAO(dbhl);
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbh);
        setAccountsDAO(persistentAccountDAO);

        /* dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        Account dummyAcct3 = new Account("40595M", "CoC Bank", "puppe mahatha", 180000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct3);
        */
    }
}
