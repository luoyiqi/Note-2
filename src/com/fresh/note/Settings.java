package com.fresh.note;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class Settings extends PreferenceActivity {
	
	public static PreferenceActivity settingContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		settingContext = this ;
	    
	
		
//		Preference shareFriend = (Preference)findPreference("friend");
//		shareFriend.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//		                @Override
//		                public boolean onPreferenceClick(Preference arg0) { 
//		                   
//		                	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//		            		sharingIntent.setType("text/plain");
//		            		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I use lyrics Manager. Download today from ...");
//		            		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Lyrics Manager Application");
//		            		startActivity(Intent.createChooser(sharingIntent, "Share using"));	
//	                      
//		                    return true;
//		                }
//		            });
		
		Preference about = (Preference)findPreference("about");
		about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		                @Override
		                public boolean onPreferenceClick(Preference arg0) { 
		                   
		                	 Intent aboutApp = new Intent(Settings.this, About.class);
			                   startActivity(aboutApp);
		                	   return true;
		                }
		            });
		
	}	

}
