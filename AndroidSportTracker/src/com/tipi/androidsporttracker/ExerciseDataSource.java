package com.tipi.androidsporttracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ExerciseDataSource {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dpHelper;
	private String[] allColumns = { MySQLiteHelper.EXERCISE_ID,
			MySQLiteHelper.EXERCISE_HEADER,
			MySQLiteHelper.EXERCISE_DISTANCE,
			MySQLiteHelper.EXERCISE_SPEED,
			MySQLiteHelper.EXERCISE_DURATION,
			MySQLiteHelper.EXERCISE_LAT,
			MySQLiteHelper.EXERCISE_LON};

	
	public ExerciseDataSource(Context context) {
		
		dpHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dpHelper.getWritableDatabase();
	}
	
	public void close() {
		dpHelper.close();
	}
	
	//function that adds exercise to database
	public Exercise addExercise(String header, String distance, String speed,
			String duration, List<Double> lat, List<Double> lon) {
		
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.EXERCISE_HEADER, header);
		values.put(MySQLiteHelper.EXERCISE_DISTANCE, distance);
		values.put(MySQLiteHelper.EXERCISE_SPEED, speed);
		values.put(MySQLiteHelper.EXERCISE_DURATION, duration);
		values.put(MySQLiteHelper.EXERCISE_LAT, serializeList(lat));
		values.put(MySQLiteHelper.EXERCISE_LON, serializeList(lon));
		
		long insertId = database.insert(MySQLiteHelper.
				TABLE_EXERCISES, null, values);
				
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES,
				allColumns,
				MySQLiteHelper.EXERCISE_ID + " = " + insertId,
				null, null, null, null);
		
		cursor.moveToFirst();
		
		Exercise newExercise = cursorToExercise(cursor);
		
		cursor.close();
		
		return newExercise;
	}

	
	//function that deletes selected exercise from database
	public void deleteExercise(Exercise exercise) {
		
		long id = exercise.getId();
		
		database.delete(MySQLiteHelper.TABLE_EXERCISES,
				MySQLiteHelper.EXERCISE_ID + " = " + id, null);
		
	}

	
	//function that gets selected exercise from database
	public Exercise getExercise(int position){

		Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES, allColumns,
				null, null, null, null, null);	
		cursor.move(position);
		
		Exercise exercise = cursorToExercise(cursor);		
		
		return exercise;
	}
	
	//function that gets all exercises for Listview
	public List <Exercise> getAllExercises(){
		
		List <Exercise> exercises = new ArrayList<Exercise>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES, allColumns,
				null, null, null, null, null);
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			
			Exercise exercise = cursorToExercise(cursor);
		
			exercises.add(exercise);
			
			cursor.moveToNext();
			
			
		}
		
		cursor.close();
		
		return exercises;
	}
	
	//function that creates Exercise from database data
	private Exercise cursorToExercise(Cursor cursor) {
		
		Exercise exercise = new Exercise();
		
		exercise.setId(cursor.getLong(0));
		exercise.setHeader(cursor.getString(1));
		exercise.setDistance(cursor.getString(2));
		exercise.setSpeed(cursor.getString(3));
		exercise.setDuration(cursor.getString(4));
		exercise.setLat(deserializeList(cursor.getString(5)));
		exercise.setLon(deserializeList(cursor.getString(6)));
		
		return exercise;
	}
	
	//function that serializes data for database
	private String serializeList(List<Double> list) {

		String JSONString = new Gson().toJson(list);
		return JSONString;
	}
	
	//function that deserializes data from database
	private List<Double> deserializeList(String JSONString)
	{
		Type listType = new TypeToken<ArrayList<Double>>() {
        }.getType();
        List<Double> arrayList = new Gson().fromJson(JSONString, listType);
        return arrayList;
	}


}
