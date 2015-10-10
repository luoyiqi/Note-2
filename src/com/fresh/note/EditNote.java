package com.fresh.note;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EditNote extends ActionBarActivity {
	EditText editBox,  enteredName;
	TextView charCounter, wordCounter;
	DatabaseManager database;
	long id;
	String titleOfNote;
	
	
	Handler countHandler = new Handler();
	Runnable upWordCount = new Runnable() {
		@Override
		public void run() {
		// this part can talk to the ui
			String[] splitedWord  = editBox.getText().toString().split("[\\s]+");
			String numberOfWords =  Integer.toString(splitedWord.length);
			wordCounter.setText(numberOfWords);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		id = getIntent().getExtras().getLong(database.KEY_ROWID);
		database = new DatabaseManager(this);
		database.open();
		enteredName = new EditText(this);
		charCounter = (TextView)findViewById(R.id.char_count_edit);
		wordCounter = (TextView)findViewById(R.id.word_count_edit);
		editBox = (EditText)findViewById(R.id.editBox_edit);
		editBox.setText(database.getNoteText(id));
		editBox.addTextChangedListener(new TextWatcher() {
            @Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				saveToDatabase();
				 //This sets a textview to the current length
		         charCounter.setText(String.valueOf(s.length()));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		displayDialog();
		backgroundCount();
	}

	
	
	public void saveToDatabase(){
		database.updateSingleNote(id, getCurrentTimeAndDate(), getNoteInfo(), getNoteText());
	}
	
	
    
public String getNoteInfo(){
    	return "";
    }

public String getNoteText(){
	return editBox.getText().toString();
}

public String getCurrentTimeAndDate() {

	Calendar c = Calendar.getInstance();
	int seconds = c.get(Calendar.SECOND);
	return getDate(seconds, "dd/MM/yyyy hh:mm");

}

public static String getDate(long milliSeconds, String dateFormat) {
	// Create a DateFormatter object for displaying date in specified
	// format.
	SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

	// Create a calendar object that will convert the date and time value in
	// milliseconds to date.
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(milliSeconds);
	return formatter.format(calendar.getTime());
}


public void displayDialog(){
	
	// create an EditText for the dialog
	  
	        enteredName.setText(database.getTitle(id));
			
			AlertDialog.Builder builder = new AlertDialog.Builder(EditNote.this); 
			builder.setTitle("Title of Note?");
			builder.setView(enteredName);
			builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// this should edit the title of the note
					titleOfNote = enteredName.getText().toString();
					updateNoteTitle();
					dialog.cancel();
				}
			});
			builder.create().show();
}



public void updateNoteTitle(){
	database.updateNoteTitle(id, titleOfNote);
}


public void backgroundCount(){
	// this will create a word count thread that will update every one seconds
	Thread myTread = new Thread(){
		@Override
	   public void run() {
				try 
				{
					while(true){
					sleep(200);
					countHandler.post(upWordCount);
				}
				 } 
					catch (InterruptedException e) {e.printStackTrace();}
					finally{}
					};
		 };
        myTread.start();
}


}
