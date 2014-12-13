package com.android.stop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		setContentView(R.layout.home_screen);
		
		Button scene = (Button) findViewById(R.id.btn_capture);
		Button stats = (Button) findViewById(R.id.btn_stats);
		
		scene.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Capture.class);
				startActivity(i);
			}
		});
		
		stats.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
				startActivity(i);
			}
		});
	}
}
