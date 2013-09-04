package com.arafat.resttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {

	Button signMeUpButton;
	EditText signUpNameEditText;
	EditText signUpEmailEditText;
	EditText signUpPasswordEditText;
	ProgressDialog pd;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		signUpNameEditText=(EditText)findViewById(R.id.signUpNameEditText);
		signUpEmailEditText=(EditText)findViewById(R.id.signUpEmailEditText);
		signUpPasswordEditText=(EditText)findViewById(R.id.signUpPasswordEditText);
		signMeUpButton=(Button)findViewById(R.id.signMeUpButton);
		
		context=this;
		
		signMeUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String signUpCredentials[]={signUpNameEditText.getText().toString(),
						signUpEmailEditText.getText().toString(),
						signUpPasswordEditText.getText().toString()};
				
				SignUpAsyncTask signUpAsyncTask=new SignUpAsyncTask();
				signUpAsyncTask.execute(signUpCredentials);
				
				}
			});
		}
	
		class SignUpAsyncTask extends AsyncTask<String, String, String>{
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pd = new ProgressDialog(context);
				pd.setTitle("Signing you up...");
				pd.setMessage("Please wait...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

			@Override
			protected String doInBackground(String... signUpCredentials) {
				HttpClient client=new DefaultHttpClient();
				String signUpUserName=signUpCredentials[0];
				String signUpEmail=signUpCredentials[1];
				String signUpPassword=signUpCredentials[2];
				String signUpRequest="http://monir.dreamchasersoft.com/mobile/index.php/api/registration/name/"+
						signUpUserName+"/email/"+signUpEmail+"/password/"+signUpPassword+"/format/json/";
				HttpGet request=new HttpGet(signUpRequest);
				try {
					HttpResponse response=client.execute(request);
				//	HttpStatus status=(HttpStatus) response.getStatusLine();
					
						StringBuilder string=new StringBuilder();
						HttpEntity entity=response.getEntity();
						InputStream input=entity.getContent();
						
						BufferedReader reader=new BufferedReader(new InputStreamReader(input));
						
						String nextLine;
						
						while((nextLine=reader.readLine())!=null)
						{
							string.append(nextLine);
						}
						
						return string.toString();
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
			
				pd.dismiss();
				
				String response;
				response=result;
				//convert string to JsonObject
				try {
					JSONObject loginSuccess=new JSONObject(response);
					
					String message=loginSuccess.getString("status");
					
					if(message.equals("success"))
					{
						Intent intent=new Intent(getApplicationContext(),loginStatusActivity.class);
						intent.putExtra("userName", signUpNameEditText.getText().toString());
						startActivity(intent);
					}
					else{
						
						Toast.makeText(getApplicationContext(), "SignUp failed, try again!", Toast.LENGTH_LONG).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
				
}
	
	

