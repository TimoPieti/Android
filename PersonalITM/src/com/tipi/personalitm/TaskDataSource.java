package com.tipi.personalitm;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tipi.personalitm.MySQLiteHelper;
import com.tipi.personalitm.Task;

public class TaskDataSource {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dpHelper;
	private String[] allColumns = { MySQLiteHelper.TASK_ID,
			MySQLiteHelper.TASK_NAME,
			MySQLiteHelper.TASK_PRIORITY,
			MySQLiteHelper.TASK_DATE,
			MySQLiteHelper.TASK_INFO};

	
	public TaskDataSource(Context context) {
		
		dpHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dpHelper.getWritableDatabase();
	}
	
	public void close() {
		dpHelper.close();
	}
	
	//metodi, joka lis‰‰ teht‰v‰n tietokantaan
	public Task addTask(String task, String priority, String date, String info){
		
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.TASK_NAME, task);
		values.put(MySQLiteHelper.TASK_PRIORITY, priority);
		values.put(MySQLiteHelper.TASK_DATE, date);
		values.put(MySQLiteHelper.TASK_INFO, info);
		
		long insertId = database.insert(MySQLiteHelper.
				TABLE_TASKS, null, values);
				
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
				allColumns,
				MySQLiteHelper.TASK_ID + " = " + insertId,
				null, null, null, null);
		
		cursor.moveToFirst();
		
		Task newTask = cursorToTask(cursor);
		
		cursor.close();
		
		return newTask;
	}
	
	//metodi, joka p‰ivitt‰‰ teht‰v‰n tiedot tietokantaan
	public void updateTask(int id, String task, String priority, String date,
			String info){
		
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.TASK_NAME, task);
		values.put(MySQLiteHelper.TASK_PRIORITY, priority);
		values.put(MySQLiteHelper.TASK_DATE, date);
		values.put(MySQLiteHelper.TASK_INFO, info);
		
		database.update(MySQLiteHelper.TABLE_TASKS, values, 
				MySQLiteHelper.TASK_ID + " = " + id, null);
		
	}
	
	//metodi, joka poistaa teht‰v‰n tietokannasta
	public void deleteTask(Task task) {
		
		long id = task.getId();
		
		database.delete(MySQLiteHelper.TABLE_TASKS,
				MySQLiteHelper.TASK_ID + " = " + id, null);
		
	}
	
	//metodi, joka poistaa kaikki teht‰v‰t tietokannasta
	public void deleteAllTasks() {
		
		database.delete(MySQLiteHelper.TABLE_TASKS, null, null);
	}
	
	//metodi, joka hakee tietyn teht‰v‰n tietokannasta
	public Task getTask(int position){

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS, allColumns,
				null, null, null, null, null);	
		cursor.move(position);
		
		Task task = cursorToTask(cursor);		
		
		return task;
	}
	
	//metodi, joka hakee kaikki teht‰v‰t tietokannasta
	public List <Task> getAllTasks(){
		
		List <Task> tasks = new ArrayList<Task>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS, allColumns,
				null, null, null, null, null);
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			
			Task task = cursorToTask(cursor);
		
			tasks.add(task);
			
			cursor.moveToNext();
			
			
		}
		
		cursor.close();
		
		return tasks;
	}
	

	private Task cursorToTask(Cursor cursor) {
		
		Task task = new Task();
		
		task.setId(cursor.getInt(0));
		task.setName(cursor.getString(1));
		task.setPriority(cursor.getString(2));
		task.setDate(cursor.getString(3));
		task.setInfo(cursor.getString(4));
		
		return task;
	}


}
