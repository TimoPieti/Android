package com.tipi.androidsporttracker;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private ExerciseDataSource dataSource;
	private ArrayAdapter<Exercise> adapter;
	private ListView excerciseList;
	
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
		
		Button btnDeleteAll = (Button) findViewById(R.id.button_DeleteAll);
		btnDeleteAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				
		        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		        builder.setTitle(getResources().getString(R.string.alert_title));
		        builder.setMessage(getResources().getString(R.string.alert_deleteAll));
		        
		        builder.setPositiveButton(getResources().getString(R.string.alert_positive),
		        		new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface arg0, int arg1) {
		            	
						dataSource.deleteAllExercises();
						adapter.clear();
						excerciseList.setAdapter(adapter); 
		            }
		        });
		        
		        builder.setNegativeButton(getResources().getString(R.string.alert_negative),
		        		new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface arg0, int arg1) {
		            	
		            }
		        });
		        builder.show();
			}
		});
		
		//get data from database for ListView
		dataSource = new ExerciseDataSource(this);
		dataSource.open();

		List <Exercise> values = dataSource.getAllExercises();
		adapter = new ArrayAdapter<Exercise>(this, 
				android.R.layout.simple_list_item_1, values); 
		excerciseList = (ListView) findViewById(R.id.activity_list);
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
