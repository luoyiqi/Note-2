package com.fresh.note;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ListNote extends Activity  implements OnItemClickListener {

	DatabaseManager database;
	ListView listNote;
	AutoCompleteTextView autoComplete;
	Cursor cr;
	EditText searchBox;
	SharedPreferences settings;
	String filter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list_note);
		
		database = new DatabaseManager(this);
		database.open();
		searchBox = (EditText) findViewById(R.id.search_box);
		listNote = (ListView) findViewById(R.id.listView_note);
		settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		filter = settings.getString("search", "1");
		searchBox.addTextChangedListener(new TextWatcher() {
            @Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				populateListViewFromDatabase();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	
		populateListViewFromDatabase();
		
		listNote.setOnItemClickListener(this);

	}

	public void populateListViewFromDatabase() {
        if (filter.contains("1")) {
			cr = database.getFilterdResultByTitle(searchBox.getText().toString());
			searchBox.setHint("Search By title");
		} // search filter is by title

		else {
			cr = database.getFilteredByText(searchBox.getText().toString());
			searchBox.setHint("Search By content");
		} // if search query is by content

		startManagingCursor(cr);

		// Create an array to specify the fields we want
		String[] from = new String[] {DatabaseManager.KEY_ROWID,DatabaseManager.KEY_TITLE, DatabaseManager.KEY_LAST_UPDATED };

		// and an array of the fields we want to bind in the view
		int[] to = new int[] { R.id.sample_num, R.id.title , R.id.last_updated};

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,R.layout.sample_item, cr, from, to);
        listNote.setAdapter(cursorAdapter);
	}
	
	

	public void onClickaddNewBtn(View view) {
		Intent changeActivityToAddNew = new Intent(ListNote.this, NewNote.class);
		startActivity(changeActivityToAddNew);
    }
	
	
	
	
	public void onClickFilter(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListNote.this);
		builder.setTitle("Filter By");
		builder.setItems(
				new String[] { "Title", "Content"},
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						SharedPreferences.Editor editor = settings.edit();
						// The 'which' argument contains the index position  of the selected item

						switch (which) {
						case 0:
							editor.putString("search", "1");
							break;
						case 1:
							editor.putString("search", "2");
							break;
						}
						editor.commit();
						Intent newClass = new Intent(ListNote.this,ListNote.class);
						startActivity(newClass);
						finish();
					}
				});
		builder.create().show();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// this will be executed in respective of id
		Intent i = new Intent(this, View_Note.class);
		Bundle sendid = new Bundle();
	    sendid.putLong(database.KEY_ROWID, id);
		 i.putExtras(sendid);
		 startActivity(i);			
	} 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.settings) {
			Intent intent = new Intent(ListNote.this, Settings.class);
	        startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	

}
