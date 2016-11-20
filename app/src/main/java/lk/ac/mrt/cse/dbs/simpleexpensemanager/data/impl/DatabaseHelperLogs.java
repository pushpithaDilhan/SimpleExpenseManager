package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pushpitha on 20-Nov-16.
 */
public class DatabaseHelperLogs extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "140595M-logs.db";

    // Table Names
    public static final String TABLE_LOGS = "logs";

    // logs Table Columns
    public static final String LOGS_DATE = "date";
    public static final String LOGS_ACNO = "ac_no";
    public static final String LOGS_TYPE = "type";
    public static final String LOGS_AMOUNT = "amount";

    private static final String CREATE_TABLE_LOGS = "CREATE TABLE "
            + TABLE_LOGS + "(" + LOGS_DATE + " DATE ," + LOGS_ACNO
            + " VARCHAR(6) ," + LOGS_TYPE + " VARCHAR(7) ," + LOGS_AMOUNT
            + " DOUBLE" + ")";

    public DatabaseHelperLogs(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TABLE_LOGS+" IF EXISTS ;");

        // create new tables
        onCreate(db);
    }
}

