package com.tipi.personalitm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class FragmentImportant extends Fragment {

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
        
		//luodaan tehtävälista ja laitetaan ne tärkeysjärjestykseen
        List <Task> priorityList = setTaskInPriorityOrder();
        
        //asetetaan luotu lista gridi-näkymään
        GridView gridView = (GridView) getActivity().
        		findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getActivity(), priorityList));
        
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = 
        		inflater.inflate(R.layout.fragment_important, container, false);
        return view;
    }
    
    //päivitetään tehtävälista
    public void onResume() {
    	super.onResume();
    	
    	dataSource = new TaskDataSource(this.getActivity());
		dataSource.open();
        
		//luodaan uusi tehtävälista ja laitetaan ne tärkeysjärjestykseen
        List <Task> priorityList = setTaskInPriorityOrder();
        
        //asetetaan päivitetty lista gridi-näkymään
        GridView gridView = (GridView) getActivity().
        		findViewById(R.id.gridview);
        if(priorityList.size() != 0) {
        	gridView.setAdapter(new ImageAdapter(getActivity(), priorityList)); 
        } 
    	
    }
	
    //metodi joka asettaa tehtävät tärkeys järjestykseen
    private List <Task> setTaskInPriorityOrder() {
    	
    	//haetaan kaikki tehtävät
    	List <Task> taskList = dataSource.getAllTasks();

    	//ensin järjestetään tehtävät prioriteetin mukaan (1 suurin 3 pienin)
    	Collections.sort(taskList, new Comparator<Object>(){

            public int compare(Object o1, Object o2) {
                Task p1 = (Task) o1;
                Task p2 = (Task) o2;
               return p1.getPriority().substring(0,1).
            		   compareToIgnoreCase(p2.getPriority().substring(0,1));
            }
    	});
    	
    	//sen jälkeen laitetaan tehtävät järjestykseen päivämäärän mukaan
    	Collections.sort(taskList, new Comparator<Object>(){

            public int compare(Object o1, Object o2) {
                Task p1 = (Task) o1;
                Task p2 = (Task) o2;
                //tutkitaan onko seuraavan tehtävän prioriteetti pienempi, jos
                //ei ole tutkitaan päivämäärää
                if(Integer.parseInt((p1.getPriority().substring(0,1))) <=
                		Integer.parseInt((p2.getPriority().substring(0,1)))){
                	return p1.getDate().compareToIgnoreCase(p2.getDate());
                }
                //jos on annetaan tehtävien olla paikallaan listassa
                else {
                	return	0;
                }
            }
    	});
    	
    	return taskList;
    }
    
}
