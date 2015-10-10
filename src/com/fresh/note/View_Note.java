package com.fresh.note;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class View_Note extends Activity {

	static Long id;
	DatabaseManager database;
	TextView  info, createdDate, lastUpdated, text, wordCount, charCount;
	
	String sharedText;
	Typeface font1;
	RelativeLayout container;
	ClipData copy;
	ClipboardManager clipboard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view__note);
		id = getIntent().getExtras().getLong(database.KEY_ROWID);
		// font1 = Typeface.createFromAsset(this.getAssets(),"Roboto-Bold.ttf");
		font1 = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");

		 container = (RelativeLayout)findViewById(R.id.container);
		 // defining on long click listerner class
		 container.setOnLongClickListener(new OnLongClickListener() {
		 @Override
		 public boolean onLongClick(View v) {
		
		 copyToClipBoard();
		 return false;
		 }
		 });

		text = (TextView) findViewById(R.id.text_view);
		wordCount = (TextView) findViewById(R.id.word_count_view);
		charCount = (TextView) findViewById(R.id.char_count_view);
		createdDate = (TextView)findViewById(R.id.created_date_view);
		lastUpdated = (TextView)findViewById(R.id.last_updated_view);
        database = new DatabaseManager(this);
		database.open();
		text.setText(database.getNoteText(id));
		createdDate.setText(database.getDateCreated(id));
		lastUpdated.setText(database.getLastUpdated(id));
		charCount.setText(returnCharCount());
		wordCount.setText(returnWordCount());
		database.close();
		


	}

	public void onClickEdit(View view) {
		Intent i = new Intent(this, EditNote.class);
		Bundle sendToActivity = new Bundle();
		sendToActivity.putLong(database.KEY_ROWID, id);
		i.putExtras(sendToActivity);
		startActivity(i);
		finish();
	}

	public void onClickShare(View view) {

		sharedText = text.getText().toString() + "\nshared via IEverNote";
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharedText);
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Note");
		startActivity(Intent.createChooser(sharingIntent, "Share using"));
	}

	public void onClickDeleteNote(View view) {
		// an alert box to confirm if the user really wants to delete the record
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this note ?");
		builder.setTitle("Confirmation");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Long deleteRowID = View_Note.id;
				database.open();
				database.deleteNote(deleteRowID);
				database.close();
				Toast.makeText(View_Note.this, "Deleted Succesfully",
						Toast.LENGTH_LONG).show();
				finish();

			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.create().show();

	}

	public void copyToClipBoard() {

		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(text.getText().toString());
		Toast.makeText(View_Note.this, "Copied To ClipBoard", Toast.LENGTH_LONG).show();
	}

  	
	public String returnCharCount(){
		int textCountValue = text.getText().length();
		return Integer.toString(textCountValue);
   }
	
	public String returnWordCount(){
		String[] splitedWord  = text.getText().toString().split("[\\s]+");
		return Integer.toString(splitedWord.length);
   }
	
	
	public void onClickExport(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(View_Note.this);
		builder.setTitle("Export To");
		builder.setItems(
				new String[] { "Microsoft 2003(doc)","Microsoft 2010(docx)", "ODT Text(odt)","OpenOffice Text(sxw)",
						"DocBook(xml)", "HTML Document(html)","RichText(rtf)", "Text(txt)"
						},
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						case 0:
							displayDialog("Microsoft 2003(doc)");
							break;
						case 1:
							displayDialog("Microsoft 2010(docx)");
							break;
						case 2:
							displayDialog("ODT Text(odt)");
							break;
						case 3:
							displayDialog("OpenOffice Text(sxw)");
							break;
						case 4:
							displayDialog("DocBook(xml)");
							break;
						case 5:
							displayDialog("HTML Document(html)");
							break;
						case 6:
							displayDialog("RichText(rtf)");
							break;
						case 7:
							displayDialog("Text(txt)");
							break;
						}
//						Intent newClass = new Intent(View_Note.this,ListNote.class);
//						startActivity(newClass);
//						finish();
					}
				});
		builder.create().show();

	}
	
	public void displayDialog(String documentName){
		// an alert box to confirm if the user really wants to export text to a file format
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Export to "+documentName+" ?");
				builder.setTitle("Confirmation");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
//						Long deleteRowID = View_Note.id;
//						database.open();
//						database.deleteNote(deleteRowID);
//						database.close();
						Toast.makeText(View_Note.this, "Feature Not Yet Available",Toast.LENGTH_LONG).show();
                     }
				});

				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				builder.create().show();
		
	}
	
	public void onClickColour(View view){
		
		Intent intent = new Intent(View_Note.this, Settings.class);
        startActivity(intent);
	}

}
