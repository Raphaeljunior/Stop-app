package com.android.stop;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Capture extends Activity {
	ProgressDialog dialog; 
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	UserFunctions funcs = new UserFunctions();
	
	private static String KEY_SUCCESS = "success";
	
	String locationName; 
	String category;
	String numberInjured;
	String numberDead;
	String image_resource;
		
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    
    
    public void beforeSync(){
    	cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Capture.this,"Internet Connection Error","Please connect to a working Internet connection", false);
			return;
		}
		new MyNewTask().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_activity);
        this.imageView = (ImageView)this.findViewById(R.id.sceneImage);
        Button photoButton = (Button) this.findViewById(R.id.cap_button);
        Spinner categorySpinner = (Spinner) findViewById(R.id.spinCategory);
        Button createAccident = (Button) findViewById(R.id.submit_button);        
        
        
        createAccident.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getData();
			}
		});
        
        String[] accidentCategories = {getResources().getString(R.string.category_collision), 
        		getResources().getString(R.string.category_loss), getResources().getString(R.string.category_pedestrian)};
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, accidentCategories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);
        
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
        }  
    } 
    
    public void getData(){
    	EditText loc = (EditText) findViewById(R.id.locText);
    	EditText num_injured = (EditText) findViewById(R.id.injText);
    	EditText num_dead = (EditText) findViewById(R.id.deadText);
    	Spinner categorySpinner = (Spinner) findViewById(R.id.spinCategory);
    	TextView _category = (TextView)categorySpinner.getSelectedView();
    	ImageView sceneImage = (ImageView) findViewById(R.id.sceneImage);
    	
    	//Converts the whole ImageView layout into a Bitmap
    	BitmapDrawable drawable = (BitmapDrawable) sceneImage.getDrawable();
    	Bitmap bitmap = drawable.getBitmap();
    	
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
    	
    	byte [] byte_arr = stream.toByteArray();    	
    	
    	locationName = loc.getText().toString();
    	numberInjured = num_injured.getText().toString();
    	numberDead = num_dead.getText().toString();
    	category = _category.getText().toString();
    	image_resource = Base64.encodeBytes(byte_arr);
    	
    	if(locationName.isEmpty() || numberInjured.isEmpty() || numberDead.isEmpty() || category.isEmpty() || image_resource.isEmpty()){
    		Toast.makeText(getApplicationContext(), "Please Fill All Fields Before Proceeding", Toast.LENGTH_SHORT).show();
    	}
    	else{
    		beforeSync();
    	}
    }
    
    private class MyNewTask extends AsyncTask<String, String, String>{
    	String indicator = null;
    	
    	@Override
        protected void onPreExecute(){
			dialog = new ProgressDialog(Capture.this);
			dialog.setIndeterminate(true);
			dialog.setTitle("Accident");
			dialog.setMessage("Processing...");
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			JSONObject json = funcs.createAccident(locationName, category, numberInjured, numberDead, image_resource);
			
			if(json != null){
				try {
					indicator = json.getString(KEY_SUCCESS);				
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else{
            	dialog.dismiss();            	
            }			
			return indicator;
		}
    	
		@Override
        protected void onPostExecute(String flag){
			super.onPostExecute(flag); 
			dialog.dismiss();
			
			try{
				int indicator = Integer.parseInt(flag);	
				if(indicator == 1){
					Toast.makeText(getApplicationContext(), "Response Recorded", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), MenuActivity.class);
					startActivity(i);
					finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "Could Not Record Response", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), MenuActivity.class);
					startActivity(i);
					finish();
				}
			}
			catch(Exception e){
				serverError();
			}
		}
    }
    
    public void serverError(){
    	alert.showAlertDialog(Capture.this,"Network Error","Could Not Establish Connection To Server, Check Network", false);
		return;
    }
}
