package com.tipi.personalitm;

import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentTasks extends Fragment {

	private TaskDataSource dataSource;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
		dataSource = new TaskDataSource(this.getActivity());
		dataSource.open();
		//haetaan teht‰v‰t tietokannasta
		List <Task> values = dataSource.getAllTasks();
		//luodaan adapteri listalle
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this.getActivity(),
				android.R.layout.simple_list_item_1, values);
		//syˆtet‰‰n arvot listaan
		ListView taskList = (ListView) getActivity().
				findViewById(R.id.tasklist);
		taskList.setAdapter(adapter);
		
		//muodostetaan OnItemClickListeneri listalle
		taskList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override //metodi, joka hakee valitun teht‰v‰n tiedot
			public void onItemClick(AdapterView<?> parent, 
					View view,int position, long id) {
		        //alustetaan muuttujat
				TextView taskInfoName = (TextView) 
		        		getActivity().findViewById(R.id.task_info_name);
		        TextView taskInfoPriority = (TextView) 
		        		getActivity().findViewById(R.id.task_info_priority);
		        TextView taskInfoDate = (TextView) 
		        		getActivity().findViewById(R.id.task_info_date);
		        TextView taskInfoInfo = (TextView) 
		        		getActivity().findViewById(R.id.task_info_info);
		        
		        //haetaan tietokannasta valittu teht‰v‰
		        Task task = dataSource.getTask(position+1);
		       
		        //asetetaan kenttiin valitun teht‰v‰n tiedot
				taskInfoName.setText(getResources().
						getString(R.string.task_name) + " " + task.getName());
				taskInfoPriority.setText(getResources().
						getString(R.string.task_priority) + " " 
						+ task.getPriority());
				taskInfoDate.setText(getResources().
						getString(R.string.task_time) + " " 
						+ setDateAndTime(task.getDate()));
				taskInfoInfo.setText(getResources().
						getString(R.string.task_info) + " " +task.getInfo());
			}
			
		});
        

    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        
        
        return view;
    }
    
    //metodi, joka luo oikeanlaisen p‰iv‰m‰‰r‰ ja aika tiedon
    private String setDateAndTime(String sourceString) {
    	
    	String parsedString = "";
    	//tutkitaan onko teht‰v‰ll‰ p‰iv‰m‰‰r‰‰, jos on asetetaan p‰iv‰m‰‰r‰
    	//tieto 
    	if(sourceString.charAt(0) != 'A') {
    		parsedString = sourceString.substring(6, 8) + "." +
    	sourceString.substring(4,6) + "." + sourceString.substring(0,4) + " ";
    	} //tutkitaan onko teht‰v‰ll‰ aikaa, jos on asetetaan tieto
    	if(sourceString.charAt(8) != 'A') {
    		parsedString = parsedString + sourceString.substring(8, 10) + ":" +
    		    	sourceString.substring(10,12);
    	}
    	
    	return parsedString;
    }
}
