package com.android.stop;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
public class UserFunctions {
	
    private JSONParser jsonParser;
    private static String universalURL = "http://stopapp.davinaqualispess.com/index.php";
     
    private static String create_accident = "create_accidents";
    private static String get_stats = "get_stats";
    
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    
    //Get Accident Statistics
    public JSONObject getStats(){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_stats));
        JSONObject json = jsonParser.getJSONFromUrl(universalURL, params);
        if(json != null){
        	return json;
        }
        else {
        	return null;
        }
    }
    
    //Report an accident
    public JSONObject createAccident(String locName, String category, String no_injured, String no_dead,String image_resource){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", create_accident));
    	params.add(new BasicNameValuePair("locName", locName));
    	params.add(new BasicNameValuePair("category", category));
    	params.add(new BasicNameValuePair("no_injured", no_injured));
    	params.add(new BasicNameValuePair("no_dead", no_dead));
    	params.add(new BasicNameValuePair("image", image_resource));
    	JSONObject json = jsonParser.getJSONFromUrl(universalURL, params);
    	if(json != null){
        	return json;
        }
        else {
        	return null;
        }
    }  
}