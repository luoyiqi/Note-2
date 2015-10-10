package com.fresh.note;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseManager {
	
//	DECLATION OF ALL THE VARIABLES AND CONSTANT THAT WILL BE USED TO CREATE THE TABLE
	
	private static final String DATABASE_NAME = "NoteDatabase"; 
	private static final String DATABASE_TABLE = "TableNote";
	private static final int DATABASE_VERSION = 1;
	
	
//	DECLARATION OF ALL THE COLUMN REQUIRED TO BE CREATED
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DATE_CREATED = "datecreated";
	public static final String KEY_LAST_UPDATED = "lastupdated";
	public static final String KEY_TEXT = "text";
	public static final String KEY_INFO = "noteinfo";
	
	
	
				
	
	private DatabaseHelper mDbHelper; 
	private SQLiteDatabase ourDatabase;
	private final Context ourContext;
	
//	THIS IS THE ACTUAL CLASS USED TO CREATE THE DATABASE AND TABLE, IT IS NESTED IN THIS CLASS
	
//		Beginning if this class
	
	

	
//	This is creating a database dynamically, but in this case the database has been preloaded
	
	public class DatabaseHelper extends SQLiteOpenHelper{		
		
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
		
		
			@Override
			public void onCreate(SQLiteDatabase db) {
				
			db.execSQL("create table " + DATABASE_TABLE + " ("
					+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_TITLE + " text not null, "
					+ KEY_DATE_CREATED + " text not null, "
					+ KEY_LAST_UPDATED + " text not null, "
					+ KEY_TEXT + " text not null, "
					+ KEY_INFO + " text not null);");
			
			}
			
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXIT "+ DATABASE_TABLE);
				onCreate(db);
			}

			

	}
	
//	End of this class
	
	
//	//	Constructor of this external class
	
	
	public DatabaseManager(Context context){
		ourContext = context;		
		
	}
	
//	constructor terminated
	
//	open the database for access
	
	public  DatabaseManager open() throws SQLException {
		mDbHelper = new DatabaseHelper(ourContext);
		ourDatabase = mDbHelper.getWritableDatabase();
		return this;
		}
	
//	Enter Values into the database or create database values
	
	public long createRecords(String dateCreated, String lastUpdated , String title,  String info, String text) {
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE_CREATED, dateCreated);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_LAST_UPDATED, lastUpdated);
		initialValues.put(KEY_INFO, info);
		initialValues.put(KEY_TEXT, text);
		return ourDatabase.insert(DATABASE_TABLE, null, initialValues);	
		
	}
	
//	close the database after creating the values for security purposes
	
	public void close() {
		mDbHelper.close();
		}
	
//	Return all data via the cursor to the calling function
	
	public Cursor getAllData() throws SQLException{
		
		String[] columns = {KEY_ROWID, KEY_DATE_CREATED, KEY_TITLE, KEY_LAST_UPDATED, KEY_INFO, KEY_TEXT};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);		
		return c ;
		}
		
      public boolean deleteNote(long rowId) {
	  return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0 ;	 }
	
	
	 public Cursor fetchSingleNote(long rowId) throws SQLException
	 {	String[] columns = {KEY_ROWID, KEY_DATE_CREATED, KEY_TITLE, KEY_LAST_UPDATED, KEY_INFO, KEY_TEXT};
	 	Cursor c =  ourDatabase.query(true, DATABASE_TABLE, columns , KEY_ROWID + "=" +  rowId, null, null, null, null, null);
	 	if (c != null) { c.moveToFirst(); }
	 	return c;
	 }
	 
	 
	//This session of code msy or may not be written here. i mean in this side of the database 
	 // this will get the string result of author
	public String getDateCreated(Long rowId) {
		Cursor c2 = fetchSingleNote(rowId);
		int rowIndex = c2.getColumnIndex(KEY_DATE_CREATED);
		return c2.getString(rowIndex);
	}
	
	public String getNoteText(Long rowId) {
		Cursor c2 = fetchSingleNote(rowId);
		int rowIndex = c2.getColumnIndex(KEY_TEXT);
		return c2.getString(rowIndex);
	}

	public String getTitle(Long rowId) {
		Cursor c2 = fetchSingleNote(rowId);
		int rowIndex = c2.getColumnIndex(KEY_TITLE);
		return c2.getString(rowIndex);
	}

	public String getLastUpdated(Long rowId) {
		Cursor c2 = fetchSingleNote(rowId);
		int rowIndex = c2.getColumnIndex(KEY_LAST_UPDATED);
		return c2.getString(rowIndex);
	}

	public String getinfo(Long rowId) {
		Cursor c2 = fetchSingleNote(rowId);
		int rowIndex = c2.getColumnIndex(KEY_INFO);
		return c2.getString(rowIndex);
	}
		
	
	public boolean updateSingleNote(long rowId, String lastUpdated, String info, String text)
	    
	{  
			ContentValues updatedValues = new ContentValues(); 
			updatedValues.put(KEY_TEXT, text);
			updatedValues.put(KEY_LAST_UPDATED, lastUpdated);
			updatedValues.put(KEY_INFO, info);
			return 	ourDatabase.update(DATABASE_TABLE, updatedValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
	public boolean updateNoteTitle(long rowId, String title)
    
	{  
			ContentValues updatedTitleValues = new ContentValues(); 
			updatedTitleValues.put(KEY_TITLE, title);
			return 	ourDatabase.update(DATABASE_TABLE, updatedTitleValues, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
	
	//
	
	public void deleteDatabase(){
		ourContext.deleteDatabase(DATABASE_NAME);
	}
	
	
	// a method to filter the result in the database based on what will be supplied by title
	
	public Cursor getFilterdResultByTitle(String value){
		String[] from ={KEY_ROWID, KEY_LAST_UPDATED, KEY_TITLE, KEY_DATE_CREATED, KEY_INFO, KEY_TEXT };
		
		
		Cursor c= ourDatabase.query(true, DATABASE_TABLE, from, KEY_TITLE + " LIKE ?", new String[] {"%"+ value + "%" }, null, null, null, null);
		return c;
		
	}
	
	// a method to filter by autho the result in the datase based on what will be supplied from the autocomplete textview
	
	public Cursor getFilteredByText(String value){
		
		String[] from ={KEY_ROWID, KEY_LAST_UPDATED, KEY_TITLE, KEY_DATE_CREATED, KEY_INFO, KEY_TEXT };
		
		
		Cursor c= ourDatabase.query(true, DATABASE_TABLE, from, KEY_TEXT + " LIKE ?", new String[] {"%"+ value + "%" }, null, null, null, null);
		return c;
	}
	

// this function will add the note to favourite if need be, the option is not available for the current version
	
//   public boolean addFavourite(Long rowId){
//	
//	ContentValues updatedValues = new ContentValues(); 
//	updatedValues.put(KEY_FAVOURITE, "favourite");
//	return 	ourDatabase.update(DATABASE_TABLE, updatedValues, KEY_ROWID + "=" + rowId, null) > 0;	
//}

// this function will remove favourite from the note if need be the option is  not available for the current version
	
//    public boolean removeFavourite(Long rowId){
//	
//	ContentValues updatedValues = new ContentValues(); 
//	updatedValues.put(KEY_FAVOURITE, "");
//	return 	ourDatabase.update(DATABASE_TABLE, updatedValues, KEY_ROWID + "=" + rowId, null) > 0;	
//}

	// this function will return all the added favourite, the features is not available for the current version

//public Cursor getFavourite(){	
//	String[] from ={KEY_FAVOURITE, KEY_ROWID, KEY_AUTHOR, KEY_TITLE, KEY_LYRICS, KEY_INFO };	
//	
//	Cursor c= ourDatabase.query(true, DATABASE_TABLE, from, KEY_FAVOURITE + " LIKE ?", new String[] {"%"+ "favourite" + "%" }, null, null, null, null);
//	return c;
//}




	
	

}    


