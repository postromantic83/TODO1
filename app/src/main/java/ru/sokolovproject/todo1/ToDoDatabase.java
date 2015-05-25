package ru.sokolovproject.todo1;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import android.util.Log;

/**
 * Created by Sasha on 11.05.2015.
 */
public class ToDoDatabase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "todo_app.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "todos";

    // ���� �������
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";

    // ������ �� �������� ���� ������
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CATEGORY
            + " text not null, " + COLUMN_SUMMARY + " text not null,"
            + COLUMN_DESCRIPTION + " text not null" + ");";

    public ToDoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(ToDoDatabase.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    /**
     * ������ ����� ������� ������ ���. ���� ������ ������� - ������������
     * ����� ������ rowId, ����� -1
     */
    public long createNewTodo(String category, String summary,
                              String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(category, summary,
                description);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();

        return row;
    }

    /**
     * ��������� ������
     */
    public boolean updateTodo(long rowId, String category, String summary,
                              String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(category, summary,
                description);

        return db.update(DATABASE_TABLE, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }

    /**
     * ������� ������� ������
     */
    public void deleteTodo(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * ���������� ������ �� ����� ���������� ������ ���
     *
     * @return ������ � ������������ ���� �������
     */
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE, new String[] { COLUMN_ID,
                        COLUMN_CATEGORY, COLUMN_SUMMARY, COLUMN_DESCRIPTION }, null,
                null, null, null, null);
    }

    /**
     * ���������� ������ � ��������� ������
     */
    public Cursor getTodo(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE,
                new String[] { COLUMN_ID, COLUMN_CATEGORY, COLUMN_SUMMARY,
                        COLUMN_DESCRIPTION }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /*
     * ������ ���� ����-�������� � ���������� � ����
     */
    private ContentValues createContentValues(String category, String summary,
                                              String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_SUMMARY, summary);
        values.put(COLUMN_DESCRIPTION, description);
        return values;
    }
}
