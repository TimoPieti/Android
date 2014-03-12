package com.tipi.personalitm;


import java.util.Calendar;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class FragmentManage extends Fragment {
	
	private TaskDataSource dataSource;
	private static TextView taskNameText;
	private static TextView taskInfoText;
	private static TextView taskDayText;
	private static TextView taskMonthText;
	private static TextView taskYearText;
	private static TextView taskHoursText;
	private static TextView taskMinsText;
	private int selectedItem = 0;
	private String dateAndTimeString;
	private Button btnAdd;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        

    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        //alustetaan textViewit
		taskNameText = (TextView) getActivity().findViewById(R.id.task_name);
		taskNameText.setText("");
		taskInfoText = (TextView) getActivity().findViewById(R.id.task_info);
		taskInfoText.setText("");
		taskDayText = (TextView) getActivity().findViewById(R.id.task_day);
		taskDayText.setText("");
		taskMonthText = (TextView) getActivity().findViewById(R.id.task_month);
		taskMonthText.setText("");
		taskYearText = (TextView) getActivity().findViewById(R.id.task_year);
		taskYearText.setText("");
		taskHoursText = (TextView) getActivity().findViewById(R.id.task_hours);
		taskHoursText.setText("");
		taskMinsText = (TextView) getActivity().findViewById(R.id.task_mins);
		taskMinsText.setText("");
		
		//alustetaan buttonit
		btnAdd = (Button) getActivity().findViewById(R.id.button_Add);
		Button btnClear = (Button) getActivity().
				findViewById(R.id.button_Clear);
		Button btnDelete = (Button) getActivity().
				findViewById(R.id.button_Delete);
		Button btnDeleteAll = (Button) getActivity().
				findViewById(R.id.button_DeleteAll);
		
		//luodaan tarvittavat OnClickListenerit buttoneille
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//lis‰t‰‰n teht‰v‰ tietokantaan, n‰ytet‰‰n tiedote 
				//ja alustetaan muuttujat
				if(taskNameText.getText().toString().length() != 0 
						&& selectedItem == 0)	{
					if(validateDateAndTime() == true){
						addTask(); 
						showToast(getResources().getString(R.string.note_add));
						callTasksFragment();
					}
				}
				//p‰ivitett‰‰n teht‰v‰, n‰ytet‰‰n tiedote 
				//ja alustetaan muuttujat
				else if(taskNameText.getText().toString().length() != 0 
						&& selectedItem != 0) {
					if(validateDateAndTime() == true){
						updateTask();
						showToast(getResources().
								getString(R.string.note_update));
						callTasksFragment();
					}
				}
				//n‰ytet‰‰n virheilmoitus tyhj‰st‰ nimi kent‰st‰
				else {
					showToast(getResources().getString(R.string.error_name));
				}
				
			}
		});
		
		//tyhjennet‰‰n kent‰t
		btnClear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				taskNameText.setText("");
				taskInfoText.setText("");
				taskDayText.setText("");
				taskMonthText.setText("");
				taskYearText.setText("");
				taskHoursText.setText("");
				taskMinsText.setText("");
				selectedItem = 0;
		        btnAdd.setText(getResources().getString(R.string.button_add));
		        setRadioButtons(1);
			}
		});
		
		//poistetaan valittu teht‰v‰
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//tutkitaan, ett‰ joku teht‰v‰ on valittu, poistetaan valittu 
				//teht‰v‰, alustetaan kent‰t
				if(selectedItem != 0)
				{
					Task task = dataSource.getTask(selectedItem);
				
					dataSource.deleteTask(task);
				
					btnAdd.setText(getResources().
							getString(R.string.button_add));
					showToast(getResources().getString(R.string.note_delete));
					callTasksFragment();
				}
			}
		});
		
		//poistetaan kaikki teht‰v‰t tietokannasta, alustetaan muuttujat
		btnDeleteAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dataSource.deleteAllTasks();
		        btnAdd.setText(getResources().getString(R.string.button_add));
				showToast(getResources().getString(R.string.note_deleteAll));
				callTasksFragment();
				
			}
		});
		
		//haetaan tietokannasta teht‰v‰t ja asetetaan ne listViewiin
		dataSource = new TaskDataSource(this.getActivity());
		dataSource.open();
		
		List <Task> values = dataSource.getAllTasks();
		
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this.getActivity(),
				android.R.layout.simple_list_item_1, values);
		
		ListView taskList = 
				(ListView) getActivity().findViewById(R.id.tasklist2);
		taskList.setAdapter(adapter);
		
		//haetaan teht‰v‰n arvot, kun se on valittu
		taskList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent,
					View view,int position, long id) {

				//tallennetaan sen paikka
		        selectedItem = position+1;
		        
		        //haetaan teht‰v‰ ja asetetaan nimi ja lis‰tiedot
				Task task = dataSource.getTask(selectedItem);
				selectedItem = task.getId();
		        taskNameText.setText(task.getName());
		        taskInfoText.setText(task.getInfo());
		        
		        //tutkitaan p‰iv‰m‰‰r‰ tietoja, ja jos siin‰ ei ole A kirjainta
		        //asetetaan kenttiin p‰iv‰m‰‰r‰n tiedot
		    	if(task.getDate().charAt(0) != 'A') {
		    		taskDayText.setText(task.getDate().substring(6, 8));
		    		taskMonthText.setText(task.getDate().substring(4,6));
		    		taskYearText.setText(task.getDate().substring(0,4));
		    	}
		    	else
		    	{
		    		taskDayText.setText("");
		    		taskMonthText.setText("");
		    		taskYearText.setText("");
		    	}

		        //tutkitaan kellonaika tietoja, ja jos siin‰ ei ole A kirjainta
		        //asetetaan kenttiin kellonajan tiedot
		    	if(task.getDate().charAt(8) != 'A') {
		    		taskHoursText.setText(task.getDate().substring(8, 10));
		    		taskMinsText.setText(task.getDate().substring(10,12));
		    	}
		    	else {
		    		taskHoursText.setText("");
		    		taskMinsText.setText("");
		    	}
		        
		    	//asetetaan priority tieto erillisess‰ metodissa
		        setRadioButtons(Integer.
		        		parseInt((task.getPriority().substring(0,1))));
		        btnAdd.setText(getResources().getString(R.string.button_save));
			}
			
		});
		
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.
        		fragment_manage, container, false);

        return view;
    }
    
    //metodi joka alustaa kenttien tiedot
    private void callTasksFragment() {
		taskNameText.setText("");
		taskInfoText.setText("");
		taskDayText.setText("");
		taskMonthText.setText("");
		taskYearText.setText("");
		taskHoursText.setText("");
		taskMinsText.setText("");
		selectedItem = 0;
		setRadioButtons(1);
		ViewPager viewPager =
				(ViewPager) getActivity().findViewById(R.id.pager);
		viewPager.setCurrentItem(0);
    }
    
    //metodi joka hakee kenttien tiedot ja ja luo uuden teht‰v‰n tietokantaan
	public void addTask() {
		
		String strTaskName = taskNameText.getText().toString();
		String strTaskInfo = taskInfoText.getText().toString();
		String priority = makePriorityString();
		dataSource = new TaskDataSource(this.getActivity());
		dataSource.open();
		dataSource.addTask(strTaskName, priority,
				dateAndTimeString, strTaskInfo);
	}
	
	//metodi joka hakee kenttien tiedot ja ja luo p‰ivitt‰ teht‰v‰n
	//tietokannnassa
	public void updateTask() {
		
		String strTaskName = taskNameText.getText().toString();
		String strTaskInfo = taskInfoText.getText().toString();
		String priority = makePriorityString();
		dataSource = new TaskDataSource(this.getActivity());
		dataSource.open();
		dataSource.updateTask(selectedItem, strTaskName, priority,
				dateAndTimeString, strTaskInfo);
	}
	
	//metodi joka luo t‰rkeys arvosta String muotoa olevan muuttujan 
	//tietokantaan
	private String makePriorityString() {
		
		RadioGroup radioGroupPriority = (RadioGroup) 
				getActivity().findViewById(R.id.task_priority_group);		
		int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
		View radioButton = radioGroupPriority.findViewById(radioButtonId);		
		int index = radioGroupPriority.indexOfChild(radioButton);
		
		switch(index) {
		case 0: {
			return getResources().getString(R.string.priority1);
		}
		case 1: {
			return getResources().getString(R.string.priority2);
		}
		case 2: {
			return getResources().getString(R.string.priority3);
		}
		default:
			return "";
		}
		
	}
	
	//metodi joka asettaa radiobuttonit oikein haetun tiedon perusteella
	private void setRadioButtons(int selected) {
		
		RadioButton priorityButton1 = (RadioButton) 
				getActivity().findViewById(R.id.priority1);
		RadioButton priorityButton2 = (RadioButton) 
				getActivity().findViewById(R.id.priority2);
		RadioButton priorityButton3 = (RadioButton) 
				getActivity().findViewById(R.id.priority3);
		
		if(selected == 1) {
			priorityButton1.setChecked(true);
			priorityButton2.setChecked(false);
			priorityButton3.setChecked(false);
		}
		else if(selected == 2) {
			priorityButton1.setChecked(false);
			priorityButton2.setChecked(true);
			priorityButton3.setChecked(false);
		}
		else {
			priorityButton1.setChecked(false);
			priorityButton2.setChecked(false);
			priorityButton3.setChecked(true);
		}
	}
	
	//tutkii, ett‰ syˆtetty aika ja p‰iv‰m‰‰r‰ on halutun laisia
	private boolean validateDateAndTime() {
		
		String strTaskDay = taskDayText.getText().toString();
		String strTaskMonth = taskMonthText.getText().toString();
		String strTaskYear = taskYearText.getText().toString();
		String strTaskHours = taskHoursText.getText().toString();
		String strTaskMins = taskMinsText.getText().toString();
		boolean hasDateAndTime = false;
		boolean hasDate = false;
		boolean isToday = false;
		
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        
		//Tutkitaan mit‰ tietoja k‰ytt‰j‰ on syˆtt‰nyt
		//Ensin p‰iv‰m‰‰r‰ ja siit‰ ett‰ onhan kaikki kent‰t t‰ynn‰
		if(strTaskDay.length() == 0 && strTaskMonth.length() == 0 
				&& strTaskYear.length() == 0)
		{
			hasDate = false;
		} //Sitten ett‰, onhan kaikki p‰iv‰m‰‰r‰ kent‰t t‰ytetty			
		else if(strTaskDay.length() == 0 || strTaskMonth.length() == 0 
				|| strTaskYear.length() == 0) {
			showToast(getResources().getString(R.string.note_date));
			return false;
		}
		else { //ja sitten ett‰ on validit arvot
			if(Integer.parseInt(strTaskMonth) < 1 ||
					Integer.parseInt(strTaskMonth) > 12 ||
					Integer.parseInt(strTaskDay) < 1 || 
					Integer.parseInt(strTaskDay) > 31  ){
				showToast(getResources().getString(R.string.note_date));
				return false;
			} //tutkintaan onko vuosi jo mennyt
			else if(Integer.parseInt(strTaskYear) < year) {
				showToast(getResources().getString(R.string.note_old));
				return false;
			}//jos vuosi on sama kuin kuluva, niin tutkitaan onko kuukausi jo
			//mennyt
			else if(Integer.parseInt(strTaskYear) == year 
					&& Integer.parseInt(strTaskMonth) < month) {
				showToast(getResources().getString(R.string.note_old));
				return false;
			}//jos vuosi ja kuukausi on sama kuin kuluva, niin tutkitaan onko 
			//p‰iv‰ jo mennyt		
			else if (Integer.parseInt(strTaskYear) == year 
					&& Integer.parseInt(strTaskMonth) == month
					&& Integer.parseInt(strTaskDay) < day) {
				showToast(getResources().getString(R.string.note_old));
				return false;
			}

			else { 
				//tarkistetaan p‰iv‰m‰‰r‰n validisuus kuukausissa, joissa on 30 p‰iv‰‰
				if(Integer.parseInt(strTaskDay) == 31)
				{
					if (Integer.parseInt(strTaskMonth) == 4 ||
							Integer.parseInt(strTaskMonth) == 6 ||
							Integer.parseInt(strTaskMonth) == 9 ||
							Integer.parseInt(strTaskMonth) == 11 
							) {
						showToast(getResources().getString(R.string.note_date));
						return false;
					}
				}
				//tarkistetaan karkausvuosi
				if (Integer.parseInt(strTaskMonth) == 2 )
				{
					if(Integer.parseInt(strTaskYear)%4==0 && 
						!(Integer.parseInt(strTaskYear)%100==0) 
						|| Integer.parseInt(strTaskYear)%400==0)	{
						if(Integer.parseInt(strTaskDay) > 29){
							showToast(getResources().getString(R.string.note_date));
							return false;
						}
					}
					else if(Integer.parseInt(strTaskDay) > 28){
						showToast(getResources().getString(R.string.note_date));
						return false;
					}
				}
				
				//ja viel‰ lis‰t‰‰n tarvittaessa 0
				if(strTaskMonth.length() == 1){
					strTaskMonth = "0" + strTaskMonth;
				}
				if(strTaskDay.length() == 1){
					strTaskDay = "0" + strTaskDay;
				}
				hasDate = true;
			}
				
		}
		//tarkistetaan onko p‰iv‰ t‰m‰ p‰iv‰
		if(Integer.parseInt(strTaskYear) == year 
					&& Integer.parseInt(strTaskMonth) == month
					&& Integer.parseInt(strTaskDay) == day){
			isToday = true;
		}
		
		//ja sitten aika. Ensin tarkistetaan, ett‰ onko p‰iv‰m‰‰r‰‰ ja aikaa 
		//syˆtetty
		if(hasDate == false && strTaskHours.length() == 0 
				&& strTaskMins.length() == 0) {
			hasDateAndTime = false;
		} //ja jos p‰iv‰m‰‰r‰ lˆytyy, mutta aikaa ei ole asetettu
		else if(hasDate == true && strTaskHours.length() == 0 
				&& strTaskMins.length() == 0) {
			hasDateAndTime = false;
		} // sitten tutkitaan onko molemmat aika kent‰t syˆtetty. ensin tunnit
		else if(strTaskHours.length() != 0 && strTaskMins.length() == 0) {
			showToast(getResources().getString(R.string.note_time));
			return false;
		} // ja sitten minuutit
		else if (strTaskHours.length() == 0 && strTaskMins.length() != 0) {
			showToast(getResources().getString(R.string.note_time));
			return false;
		} //sitten tutkitaan onko aika tiedoissa jotakin, 
		//mutta p‰iv‰m‰‰r‰‰ ei ole annettu
		else if(hasDate == false && strTaskHours.length() != 0 
				&& strTaskMins.length() != 0) {
			showToast(getResources().getString(R.string.note_date));
			return false;
		}
		else { //seuraavaksi tutkitaan, ett‰ annetut arvot on oikeasti 
			//kellonaika
			if(Integer.parseInt(strTaskHours) > 23 
					|| Integer.parseInt(strTaskMins) > 59) {
				showToast(getResources().getString(R.string.note_time));
				return false;
			} 
			else { 
				if(isToday == true)
				{
					//tutkitaan onko aika mennyt jo. ensin tunnit
					if(Integer.parseInt(strTaskHours) < hour) {
						showToast(getResources().getString(R.string.note_old));
						return false;
					} //ja sitten minuutit
					else if(Integer.parseInt(strTaskHours) == hour &&
							Integer.parseInt(strTaskMins) < min) {
						showToast(getResources().getString(R.string.note_old));
						return false;
					}
				}	
				//ja viel‰ lis‰t‰‰n tarvittaessa 0
				if(strTaskHours.length() == 1){
					strTaskHours = "0" + strTaskHours;
				}
				if(strTaskMins.length() == 1){
					strTaskMins = "0" + strTaskMins;
				}
				
				hasDateAndTime = true;
			}
		}
		
		//lopuksi luodaan aika ja p‰iv‰m‰‰r‰ String annettujen tietojen mukaan
		//jos molemmat kent‰t on syˆtetty oikein luodaan String niiden tietojen
		//mukaan
		if(hasDateAndTime == true) {
			dateAndTimeString = strTaskYear + strTaskMonth + strTaskDay +
					strTaskHours + strTaskMins;
		} 
		//jos pelkk‰ p‰iv‰m‰‰r‰ on syˆtetty, korvataan aika tiedolla AAAA
		else if(hasDate == true){
			dateAndTimeString = strTaskYear + strTaskMonth + strTaskDay +
					"AAAA";
		} 
		//jos aikaa eik‰ p‰iv‰m‰‰r‰‰ ole syˆtetty, koostuu String tietue
		//pelk‰st‰‰n A-kirjaimista
		else { 
			dateAndTimeString = "AAAAAAAAAAAA";
		}
		return true;
	}
	
	//metodi joka tulostaa halutun tiedon
	private void showToast(String message) {
		
		Toast toast = Toast.makeText(this.getActivity(),
				message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}


