package com.tipi.personalitm;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private Context context;
	private Field[] ID_Fields;
	private int[] resourceArray;
	private String[] taskNames;
	private String[] taskPriorities;
	private String[] taskDates;
	private String[] taskInfos;
	private String name;
	private String priority;
	private String date;
	private String info;
	
	public ImageAdapter(Context context, List <Task> list) {
		this.context = context;
		
		//luodaan tietueet nelj‰lle t‰rkeimm‰lle teht‰v‰lle
		ID_Fields = R.drawable.class.getFields();
		resourceArray = new int[ID_Fields.length];
		taskNames = new String[ID_Fields.length];
		taskPriorities = new String[ID_Fields.length];
		taskDates = new String[ID_Fields.length];
		taskInfos = new String[ID_Fields.length];
		
		//haetaan oikean kieliset otsikot kentille
		name = context.getString(R.string.task_name);
		priority = context.getString(R.string.task_priority);
		date = context.getString(R.string.task_date);
		info = context.getString(R.string.task_info);
		
		//asetetaan nelj‰n t‰rkeimm‰n teht‰v‰n tiedot kenttiin
		for (int i=0; i < ID_Fields.length; i++) {
			if(list.size() > i) {
				taskNames[i] = list.get(i).getName();
				taskPriorities[i] = list.get(i).getPriority();
				taskDates[i] = list.get(i).getDate();
				taskInfos[i] = list.get(i).getInfo();
			} //jos tietokannassa on v‰hemm‰n kuin nelj‰ teht‰v‰‰, laitetaan
			//tiedot tyhjiksi
			else {
				taskNames[i] = "";
				taskPriorities[i] = "";
				taskDates[i] = "";
				taskInfos[i] = "";
			}
		}
		
		//asetetaan kuvien tiedot
		for(int i=0; i < ID_Fields.length; i++) {			
			try {
				resourceArray[i] = ID_Fields[i].getInt(null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int getCount() {
		return resourceArray.length;
	}
	
	public int[] getImageResourceArray() {
		return resourceArray;
	}



	@SuppressWarnings("static-access")
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = 
				(LayoutInflater) context.
				getSystemService(context.LAYOUT_INFLATER_SERVICE);

		View gridview;
		//luodaan n‰kym‰
		if (convertView == null) {
			gridview = new View(context);
			gridview = inflater.inflate(R.layout.image_adapter, null);
			
			//asetetaan arvot kenttiin
			ImageView imageView = (ImageView) 
					gridview.findViewById(R.id.grid_item_image);
			imageView.setImageResource(resourceArray[index]);
            TextView taskName = (TextView) 
            		gridview.findViewById(R.id.grid_item_name);
            taskName.setText(name + " " + taskNames[index]);
            TextView taskPriority = (TextView) 
            		gridview.findViewById(R.id.grid_item_priority);
            taskPriority.setText(priority + " " + taskPriorities[index]);
            TextView taskDate = (TextView) 
            		gridview.findViewById(R.id.grid_item_date);
            taskDate.setText(date + " " + setDateAndTime(taskDates[index]));
            TextView taskInfo = (TextView) 
            		gridview.findViewById(R.id.grid_item_info);
            taskInfo.setText(info + " " + taskInfos[index]);
            
			
		}
		else {
			gridview = convertView;
		}
		
		return gridview;
	}
	

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//metodi, joka luo oikeanlaisen p‰iv‰m‰‰r‰ ja aika tiedon
    private String setDateAndTime(String sourceString) {
    	
    	String parsedString = "";
    	if(sourceString.length() != 0){
    		//tutkitaan onko teht‰v‰ll‰ p‰iv‰m‰‰r‰‰, jos on asetetaan p‰iv‰m‰‰r‰
        	//tieto 
    		if(sourceString.charAt(0) != 'A') {
    			parsedString = sourceString.substring(6, 8) + "." +
    					sourceString.substring(4,6) + "." + 
    					sourceString.substring(0,4) + " ";
    		}//tutkitaan onko teht‰v‰ll‰ aikaa, jos on asetetaan tieto
    		if(sourceString.charAt(8) != 'A') {
    			parsedString = parsedString + sourceString.substring(8, 10) + 
    					":" + sourceString.substring(10,12);
    		}
    	}
    		
    	return parsedString;
    }
	
}
