package com.tipi.androidsporttracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_EXERCISES = "exercises";
	
	//fields
	public static final String EXERCISE_ID= "id";
	public static final String EXERCISE_HEADER= "header";
	public static final String EXERCISE_DISTANCE = "distance";
	public static final String EXERCISE_SPEED = "speed";
	public static final String EXERCISE_DURATION = "duration";
	public static final String EXERCISE_LAT = "lat";
	public static final String EXERCISE_LON = "lon";
	
	private static final String DATABASE_NAME= "exercises.dp";
	
	private static final int DATABASE_VERSION = 1;
	
	//database
	private static final String DATABASE_CREATE= "CREATE TABLE " +
			TABLE_EXERCISES + "("
			+ EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ EXERCISE_HEADER + " TEXT NOT NULL, "
			+ EXERCISE_DISTANCE + " TEXT NOT NULL, "
			+ EXERCISE_SPEED + " TEXT NOT NULL, "
			+ EXERCISE_DURATION + " TEXT NOT NULL, "
			+ EXERCISE_LAT + " TEXT NOT NULL, "
			+ EXERCISE_LON + " TEXT NOT NULL );";
	
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
