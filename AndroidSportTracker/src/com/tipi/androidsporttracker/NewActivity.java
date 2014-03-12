package com.tipi.androidsporttracker;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.LocationListener;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.*;

public class NewActivity extends FragmentActivity implements LocationListener {
	
	//flags
	boolean content = true;
	boolean tracking = false;
	boolean saveAction = false;
	
	//buttons
	private Button btnSave;
	private Button btnStart;
	private Button btnCancel;
	
	//exercise data
	private TextView headerTextView;
	private TextView durationTextView;
	private TextView speedTextView;
	private TextView distanceTextView;
	private double duration = 0.00;
	private double distance = 0.00;
	private double speed = 0.00;
	private List<Double> speeds = new ArrayList<Double>();
	private List<Double> lat = new ArrayList<Double>();
	private List<Double> lon = new ArrayList<Double>();
	
	//map
	private GoogleMap map;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 10;
    Location previousLocation = null;
	
    //timer
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	

	private ExerciseDataSource dataSource;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);

		btnSave = (Button) findViewById(R.id.button_Save);
		btnStart = (Button) findViewById(R.id.button_Start);
		btnCancel = (Button) findViewById(R.id.button_Cancel);
		durationTextView = (TextView) findViewById(R.id.duration);
		speedTextView = (TextView) findViewById(R.id.speed);
		distanceTextView = (TextView) findViewById(R.id.distance);
		btnStart.setEnabled(false);
		
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(content == true) //proceed button click
				{
					//change layout
					RelativeLayout rel1 = (RelativeLayout) findViewById(R.id.layout1);
					RelativeLayout rel2 = (RelativeLayout) findViewById(R.id.layout2);
					rel1.setVisibility(View.INVISIBLE);
					rel2.setVisibility(View.VISIBLE);
					btnSave.setText(getResources().getString(R.string.button_save));
					btnSave.setEnabled(false);
					btnStart.setEnabled(true);
					content = false;
					
					//get type and date of the exercise
					Spinner typeSpinner = (Spinner)findViewById(R.id.type_spinner);
					headerTextView = (TextView) findViewById(R.id.selected_header);
			    	String selectedString = String.valueOf(typeSpinner.getSelectedItem());
			    	Time today = new Time(Time.getCurrentTimezone());
			    	today.setToNow();
			    	selectedString = selectedString + " " + today.monthDay + "." + 
			    			(today.month+1) + "." + today.year;
			    	headerTextView.setText(selectedString);
					
				}
				else //save button click
				{
					saveAction = true;
					alertHandler();
				}
			}
		});
		
		btnStart.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				if(tracking == false && content == false) //start button click
				{
					btnStart.setText(getResources().getString(R.string.button_pause));
					tracking = true;
					btnSave.setEnabled(false);
					btnCancel.setEnabled(false);
	                startTime = SystemClock.uptimeMillis();
	                customHandler.postDelayed(updateTimerThread, 0);

				}
				else // pause button click
				{
					pauseAction();
				}
			}
		});
		
		//cancel button
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(content == true)
				{
					finish();
				}
				else
				{
					saveAction = false;
					alertHandler();
				}
			}
		});
	
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
        		MIN_TIME, MIN_DISTANCE, this);       

	}
	        

	//function that handles tracking pause
	private void pauseAction(){
		
		btnStart.setText(getResources().getString(R.string.button_continue));
		tracking = false;
		btnSave.setEnabled(true);
		btnCancel.setEnabled(true);
        timeSwapBuff += timeInMilliseconds; 
        customHandler.removeCallbacks(updateTimerThread); //stop timer
		
	}
	
	private void alertHandler() {
		
        AlertDialog.Builder builder = new AlertDialog.Builder(NewActivity.this);
        builder.setTitle(getResources().getString(R.string.alert_title));
        if(saveAction == true) //check if user wants to save or cancel exercise
        {
        	builder.setMessage(getResources().getString(R.string.alert_save));
        }
        else
        {
        	builder.setMessage(getResources().getString(R.string.alert_cancel));
        }
        builder.setPositiveButton(getResources().getString(R.string.alert_positive),
        		new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if(saveAction == true)
                {
					addExercise(); //save
                	finish();
                }
                else
                {
                	finish();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.alert_negative),
        		new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            	//do nothing
            }
        });
        builder.show();
		
	}
	
	//function that saves exercise data to database
	private void addExercise()
	{
		dataSource = new ExerciseDataSource(this);
		dataSource.open();
		double averageSpeed = 0.00;
		if(speeds.size() != 0){ //count average speed
			for(int i= 0; i < speeds.size(); i++)
			{
				averageSpeed = averageSpeed + speeds.get(i);
			}
			averageSpeed = averageSpeed / speeds.size();
			speedTextView.setText(String.format("%02f", averageSpeed)+ " km/h");
		}
		
		if(lat.size() == 0)
		{
			lat.add(65.0134);
			lon.add(25.4672);
		 /* lat.add(65.0245);
	    	lat.add(65.0256);
	    	lat.add(65.0256);
	    	lon.add(25.4667);
	    	lon.add(25.4699);
	    	lon.add(25.4749); */
		}
		
		dataSource.addExercise(headerTextView.getText().toString(),
				distanceTextView.getText().toString(),           				
				speedTextView.getText().toString(), 
				durationTextView.getText().toString(), lat, lon);
	}
	
	//function that handles back key
	@Override
	public void onBackPressed() {
		if(content == true)
		{
	        locationManager.removeUpdates(this); //stop gps
			finish();

		}
		else
		{
			saveAction = false;
			alertHandler();			
		}
	}
	
	@Override
	public void onPause() {
		
		super.onPause();
		locationManager.removeUpdates(this); //stop gps
		if(tracking == true)
		{
			pauseAction();
		}
        
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
        		MIN_TIME, MIN_DISTANCE, this);   
    }
	
	//Timer
    private Runnable updateTimerThread = new Runnable() {
    	
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            duration = secs;
            secs = secs % 60;
            durationTextView.setText("" + String.format("%02d", hours) + ":" 
            		+ String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };

    //function that handles tracking
	@Override
	public void onLocationChanged(Location location) {
		
	    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
	    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
	    map.animateCamera(cameraUpdate);
	    locationManager.removeUpdates(this);
	  

	  if( tracking == true)
	    {
	    	lat.add(location.getLatitude());
	    	lon.add(location.getLongitude());
	    	map.addPolyline(new PolylineOptions()
	    			.add(latLng, new LatLng(previousLocation.getLatitude(), 
	    					previousLocation.getLongitude()))
	    			.width(5)
	                .color(Color.RED)
	    			);
	    	distance = distance + previousLocation.distanceTo(location);
	    	distanceTextView.setText(String.format("%02f", distance)+ " km");        	
	    	speed = distance / duration; //count speed
	    	speeds.add(speed);
	    	speedTextView.setText(String.format("%02f", speed)+ " km/h");
	    	previousLocation = location;
	    }
	    else{
	    	previousLocation = location;
	    }
	    	
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
