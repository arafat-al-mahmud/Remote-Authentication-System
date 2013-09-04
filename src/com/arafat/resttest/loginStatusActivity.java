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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class loginStatusActivity extends Activity {
	TextView loginStatusTextView;
	Button viewDetails;
	ProgressDialog pd;
	Context context;
	LinearLayout detailsLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_status_activity);
		
		context=this;
		
		detailsLayout=(LinearLayout)findViewById(R.id.detailsLayout);
		detailsLayout.setVisibility(LinearLayout.GONE);
		
		loginStatusTextView=(TextView)findViewById(R.id.loginStatusTextView);
		
		loginStatusTextView.setText("Successful !");
		
		viewDetails=(Button)findViewById(R.id.viewDetailsButton);
		viewDetails.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=getIntent();
				String[] userName={intent.getStringExtra("userName")};
				ViewDetailsAsyncTask viewDetailsAsyncTask=new ViewDetailsAsyncTask();
				viewDetailsAsyncTask.execute(userName);
			}
		});
	}
	
	class ViewDetailsAsyncTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(context);
			pd.setTitle("Retrieving Data...");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		@Override
		protected String doInBackground(String... userName) {
			HttpClient client=new DefaultHttpClient();
			String UserName=userName[0];
			String detailsRequest="http://monir.dreamchasersoft.com/mobile/index.php/api/user/name/"+
			UserName+"/format/json/";
			HttpGet request=new HttpGet(detailsRequest);
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
				JSONArray jsonArray=new JSONArray(response);
				
				JSONObject jsonObject=jsonArray.getJSONObject(0);
				
				TextView userNametv=(TextView) findViewById(R.id.userNameTextView);
				TextView emailtv=(TextView)findViewById(R.id.emailTextView);
				TextView passwordtv=(TextView)findViewById(R.id.passwordTextView);
				
				userNametv.setText("User Name: "+jsonObject.getString("name"));
				emailtv.setText("Email: "+jsonObject.getString("email"));
				passwordtv.setText("Password: "+jsonObject.getString("password"));
				
				detailsLayout.setVisibility(LinearLayout.VISIBLE);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
