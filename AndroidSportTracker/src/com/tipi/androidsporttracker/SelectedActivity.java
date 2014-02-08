package com.tipi.androidsporttracker;


import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class SelectedActivity extends FragmentActivity {

	private ExerciseDataSource dataSource;
	private Button btnDelete;
	private Button btnBack;
	int id;
	
	private TextView header;
	private TextView duration;
	private TextView speed;
	private TextView distance;
	private List<Double> lat;
	private List<Double> lon;
	
	Exercise exercise;
	
	private GoogleMap map;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("selected_id"); //get id
		}
		
		//get selected data
		dataSource = new ExerciseDataSource(this);
		dataSource.open();
		
		exercise = dataSource.getExercise(id);
		lat = exercise.getLat();
		lon = exercise.getLon();
		
		header = (TextView) findViewById(R.id.selected_header);
		duration = (TextView) findViewById(R.id.duration);
		speed = (TextView) findViewById(R.id.speed);
		distance = (TextView) findViewById(R.id.distance);
		
		header.setText(exercise.getHeader());
		duration.setText(exercise.getDuration());
		speed.setText(exercise.getSpeed());
		distance.setText(exercise.getDistance());
		
		btnDelete = (Button) findViewById(R.id.button_Delete);
		btnBack = (Button) findViewById(R.id.button_Back);
		
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
		        AlertDialog.Builder builder = new AlertDialog.Builder(SelectedActivity.this);
		        builder.setTitle(getResources().getString(R.string.alert_title));
		        builder.setMessage(getResources().getString(R.string.alert_delete));
		        builder.setPositiveButton(getResources().getString(R.string.alert_positive),
		        		new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface arg0, int arg1) {
		            	dataSource.deleteExercise(exercise);
		            	finish();
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
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					finish();
			}
		}); 
		
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		//center to beginning of the exercise
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat.get(0), lon.get(0)), 13));
		
		//draw route to map
		for(int i = 0; i < lat.size(); i++)
		{
		map.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(lat.get(i), lon.get(i)))  
                );
		}
	}

}
