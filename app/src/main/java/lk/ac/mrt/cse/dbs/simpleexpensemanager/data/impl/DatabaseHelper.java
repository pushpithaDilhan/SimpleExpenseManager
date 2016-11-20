package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pushpitha on 19-Nov-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "expenseManager.db";

    // Table Names
    public static final String TABLE_ACCOUNT = "account";

    // account Table Columns
    public static final String AC_ACNO = "ac_no";
    public static final String AC_BANK = "bank";
    public static final String AC_ACHOLDER = "ac_holder";
    public static final String AC_BALANCE = "balance";


    // Tag table create statement
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_ACCOUNT
            + "(" + AC_ACNO + " VARCHAR(6) PRIMARY KEY," + AC_BANK + " VARCHAR(30) ,"
            + AC_ACHOLDER + " VARCHAR(30) ," +  AC_BALANCE + " DOUBLE "+ " )";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TABLE_ACCOUNT+" IF EXISTS ;");

        // create new tables
        onCreate(db);
    }
}
