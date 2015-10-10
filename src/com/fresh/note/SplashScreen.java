package com.fresh.note;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class SplashScreen extends Activity {

	AlphaAnimation alpha;
	Typeface font, font2;
	TextView splashText, splashText2;
	Handler handler;
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			splashText2.setVisibility(1);
			splashText2.setText(splashText2.getText().toString() + ".");
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
		
		alpha= new AlphaAnimation(0, 1);
	    alpha.setDuration(1000);
	    handler = new Handler();
	    
		
		splashText = (TextView)findViewById(R.id.slash_test);
		splashText2 = (TextView)findViewById(R.id.slash_test2);
		font =  Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");
		font2 =  Typeface.createFromAsset(this.getAssets(), "smart watch.ttf");
		splashText.setTypeface(font2);
		splashText2.setTypeface(font);
		
		
        
	    Thread myTread = new Thread(){
			@Override
			public void run() {
			try {
				sleep(1500);
				handler.post(runnable);
				sleep(500);
				handler.post(runnable);
				sleep(500);	
				handler.post(runnable);
				sleep(500);	
				handler.post(runnable);
										
				Intent changeActivity = new Intent(SplashScreen.this, ListNote.class);
				startActivity(changeActivity);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				
//				this will stop the song after this activity
				   
					finish();
			}
			
			
			};
		     	};
		   myTread.start();
		   splashText.setAnimation(alpha);
	}



}
