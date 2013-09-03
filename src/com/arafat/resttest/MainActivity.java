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
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button readDataButton;
	ProgressDialog pd;
	Context context;
	TextView dataView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		context=this;
		
		dataView=(TextView)findViewById(R.id.textView2);
		readDataButton=(Button)findViewById(R.id.button1);
		readDataButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ApiAsyncTask apitask=new ApiAsyncTask();
				apitask.execute(new String());
			}
		});
	}

	class ApiAsyncTask extends AsyncTask<String, String, String>
	{

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
		protected String doInBackground(String... arg0) {
			
			HttpClient client=new DefaultHttpClient();
			HttpGet request=new HttpGet("http://www.cheesejedi.com/rest_services/get_big_cheese?level=1");
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
				
					dataView.setText(jsonArray.getString(1));
				
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
