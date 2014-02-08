package com.tipi.personalitm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TASKS = "tasks";
	
	//nimet‰‰n kent‰t
	public static final String TASK_ID= "id";
	public static final String TASK_NAME= "name";
	public static final String TASK_PRIORITY = "priority";
	public static final String TASK_DATE = "date";
	public static final String TASK_INFO = "info";
	
	private static final String DATABASE_NAME= "tasks.dp";
	
	private static final int DATABASE_VERSION = 1;
	
	//luodaan tietokanta
	private static final String DATABASE_CREATE= "CREATE TABLE " +
			TABLE_TASKS + "("
			+ TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TASK_NAME + " TEXT NOT NULL, "
			+ TASK_PRIORITY + " TEXT NOT NULL, "
			+ TASK_DATE + " TEXT NOT NULL, "
			+ TASK_INFO +	" TEXT NOT NULL );";
	
	public MySQLiteHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		database.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
}
