package com.example.mobile.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import com.example.mobile.Constant;
import com.example.mobile.model.ExpenseEntity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseDatabaseAssessObject {
    private SQLiteDatabase db;
    public MutableLiveData<List<ExpenseEntity>> expenseList;
    public MutableLiveData<ExpenseEntity> expense;
    public int tripID;
    SimpleDateFormat sdf = new SimpleDateFormat((("MMM dd, yyyy HH:mm:ss aaa")));
    public ExpenseDatabaseAssessObject(Context context, int tripID){
        dbHandler database = new dbHandler(context);
        this.db = database.getWritableDatabase();
        this.tripID = tripID;
        expenseList = new MutableLiveData<List<ExpenseEntity>>();
        {
            expenseList.setValue(getAll());
        }
        expense = new MutableLiveData<>();

    }

    public void getExpense(){
        expenseList.setValue(getAll());
    }

    public List<ExpenseEntity> get(String sql, String ...selectArgs){
        List<ExpenseEntity> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);
        while (cursor.moveToNext()){
            ExpenseEntity exp = new ExpenseEntity();
            exp.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
            exp.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            exp.setAmount(cursor.getFloat(cursor.getColumnIndexOrThrow("amount")));
            Date date = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("date")),new ParsePosition(0));
            exp.setTime(date);
            exp.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
            exp.setTripID(cursor.getInt(cursor.getColumnIndexOrThrow("tripID")));
            list.add(exp);
        }
        System.out.println("================================================" + list.size());
        return list;
    }

    public List<ExpenseEntity> getAll(){

        String sql = "SELECT * FROM expense WHERE tripID = ?";
        List<ExpenseEntity> list = get(sql, String.valueOf(tripID));
        expenseList.setValue(list);
        return get(sql);
    }

    public ExpenseEntity getByID(String id){
        String sql =" SELECT * FROM expense WHERE id = ? and tripID = ?";
        List<ExpenseEntity> list = get(sql, id, String.valueOf(tripID));
        expense.setValue(list.get(0));
        return list.get(0);
    }

    public void getExpenseByID(String id){
        if (id == null || id.equals(String.valueOf(Constant.NEW_ENTITY_ID))){
            expense.setValue(new ExpenseEntity());
            return;
        }
        getByID(id);
    }

    public long insert(ExpenseEntity expense){
        ContentValues values = new ContentValues();
        values.put("type", expense.getType());
        values.put("amount", expense.getAmount());
        values.put("date", sdf.format(expense.getTime()));
        values.put("comment", expense.getComment());
        values.put("tripID",tripID);
        return db.insert("expense", null, values);
    }

    public long update(ExpenseEntity expense){
        ContentValues values = new ContentValues();
        values.put("type", expense.getType());
        values.put("amount", expense.getAmount());
        values.put("date", sdf.format(expense.getTime()));
        values.put("comment", expense.getComment());
        return db.update("expense", values, "id=? and tripID =?", new String[]{String.valueOf(expense.getId()),String.valueOf(tripID)});
    }

    public int delete(String id){

        return db.delete("expense", "id=? and tripID =?", new String[]{id,String.valueOf(tripID)});
    }
}
