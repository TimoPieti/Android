package com.tipi.androidsporttracker;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private ExerciseDataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnAdd = (Button) findViewById(R.id.button_Add);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MainActivity.this, NewActivity.class);
				startActivity(intent);	//new NewActivity();
			}
		});
		
		//get data from database for ListView
		dataSource = new ExerciseDataSource(this);
		dataSource.open();

		List <Exercise> values = dataSource.getAllExercises();
		ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(this,
				android.R.layout.simple_list_item_1, values); 
		ListView excerciseList = (ListView) findViewById(R.id.activity_list);
		excerciseList.setAdapter(adapter); 
		
		excerciseList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view,int position, long id) {
				
				Intent intent = new Intent(MainActivity.this, 
						SelectedActivity.class);
				intent.putExtra("selected_id", position + 1); //selected id
				startActivity(intent);	//new SelectedActivity();
				
			}
			
		});
	}

	@Override
	public void onPause() {
		
		super.onPause();
	}
	
	public void onResume() {
		
		super.onResume();
		dataSource = new ExerciseDataSource(this);
		dataSource.open();

		//updates ListView
		List <Exercise> values = dataSource.getAllExercises();
		ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(this,
				android.R.layout.simple_list_item_1, values);
		ListView excerciseList = (ListView) findViewById(R.id.activity_list);
		excerciseList.setAdapter(adapter);
	}

}
