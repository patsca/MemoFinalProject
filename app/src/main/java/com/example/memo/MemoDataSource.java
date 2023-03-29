package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoDataSource {

    private SQLiteDatabase database;
    private MemoDBHelper dbHelper;

    public MemoDataSource(Context context) {
        dbHelper = new MemoDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertMemo(Memo m) {
        boolean didSucceed = false;
        try {

            ContentValues initialValues = new ContentValues();

            initialValues.put("memoname", m.getMemoName());

            if (m.getMemoPrio() == "Low") {
                initialValues.put("prio", "0");
            } else if (m.getMemoPrio() == "Medium") {
                initialValues.put("prio", "1");
            } else {
                initialValues.put("prio", "2");
            }
            initialValues.put("content", m.getMemoContent());


            initialValues.put("date", m.getMemoTime());
            didSucceed = database.insert("memo", null, initialValues) > 0;

        } catch (Exception e) {

        }
        return didSucceed;
    }

    public boolean updateMemo(Memo m) {
        boolean didSucceed = false;
        try {

            Long rowId = (long) m.getMemoId();
            ContentValues updateValues = new ContentValues();

            updateValues.put("memoname", m.getMemoName());
            if (m.getMemoPrio() == "Low") {
                updateValues.put("prio", "0");
            } else if (m.getMemoPrio() == "Medium") {
                updateValues.put("prio", "1");
            } else {
                updateValues.put("prio", "2");
            }
            updateValues.put("content", m.getMemoContent());
            didSucceed = database.update("memo", updateValues, "_id = " + rowId, null) > 0;
        } catch (Exception e) {

        }

        return didSucceed;
    }

    public int getLastMemoID() {
        int lastId;
        try {
            String query = "Select MAX(_id) from memo";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getMemoName() {
        ArrayList<String> memoNames = new ArrayList<>();
        try {
            String query = "Select memoname from memo";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                memoNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            memoNames = new ArrayList<String>();
        }
        return memoNames;
    }

    public ArrayList<Memo> getMemos(String sortField, String sortOrder) {
        ArrayList<Memo> memo = new ArrayList<Memo>();
        try {
            String query = "SELECT * FROM memo ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Memo newMemo;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMemo = new Memo();
                newMemo.setMemoId(cursor.getInt(0));
                newMemo.setMemoName(cursor.getString(1));
                newMemo.setMemoPrio(cursor.getString(2));
                newMemo.setMemoContent(cursor.getString(3));
                newMemo.setMemoTime(cursor.getString(4));
                memo.add(newMemo);
                cursor.moveToNext();

            }
            cursor.close();
        } catch (Exception e) {
            memo = new ArrayList<Memo>();
        }
        return memo;
    }

    public Memo getSpecificMemo(int memoId) {
        Memo memo = new Memo();
        String query = "SELECT * FROM memo WHERE _id=" + memoId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            memo.setMemoId(cursor.getInt(0));
            memo.setMemoName(cursor.getString(1));
            memo.setMemoPrio(cursor.getString(2));
            memo.setMemoContent(cursor.getString(3));

            cursor.close();
        }

        return memo;
    }

    public boolean deleteMemo (int memoId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("memo", "_id=" + memoId, null) > 0;

        }
        catch (Exception e) {

        }
        return didDelete;
    }


}



