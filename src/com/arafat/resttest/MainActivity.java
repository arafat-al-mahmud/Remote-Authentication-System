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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button loginButton;
	Button signUpButton;
	ProgressDialog pd;
	Context context;
	
	EditText loginEditText;
	EditText passwordEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_home);
		
		context=this;
		
		loginEditText=(EditText)findViewById(R.id.loginEditText);
		passwordEditText=(EditText)findViewById(R.id.loginPasswordEditText);
		loginButton=(Button)findViewById(R.id.loginButton);
		signUpButton=(Button)findViewById(R.id.signUpButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LoginAsyncTask apitask=new LoginAsyncTask();
				String[] loginCredentials={loginEditText.getText().toString()
						,passwordEditText.getText().toString()};
				apitask.execute(loginCredentials);
			}
		});
		
		signUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
				startActivity(intent);
				
			}
		});
	}

	class LoginAsyncTask extends AsyncTask<String, String, String>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(context);
			pd.setTitle("Loggin in...");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		@Override
		protected String doInBackground(String... credentials) {
			
			HttpClient client=new DefaultHttpClient();
			String userName=credentials[0];
			String password=credentials[1];
			String loginRequest="http://monir.dreamchasersoft.com/mobile/index.php/api/login/name/"+
			userName+"/password/"+password+"/format/json/";
			HttpGet request=new HttpGet(loginRequest);
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
					intent.putExtra("userName", loginEditText.getText().toString());
					startActivity(intent);
				}
				else{
					
					Toast.makeText(getApplicationContext(), "Login failed, try again!", Toast.LENGTH_LONG).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
