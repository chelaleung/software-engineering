package hk.ust.barternbargain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import hk.ust.barternbargain.barternbargain.Barternbargain;
import hk.ust.barternbargain.barternbargain.model.Session;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private EditText username=null;
	private EditText password=null;
	private Button login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		username = (EditText)findViewById(R.id.editText1);
		password = (EditText)findViewById(R.id.editText2);
		login = (Button)findViewById(R.id.button1);
		
	}
	


	private final Runnable loginRunnable = new Runnable () {
		public void run() {
			try {
				Barternbargain service = getApiServiceHandle();
				String user = username.getText().toString();
				String pass = password.getText().toString();
				Session session = service.insertSession(pass, user).execute();
				loginHandler.sendMessage(
						loginHandler.obtainMessage(0, null));
			} catch (GoogleJsonResponseException e) {
				if (e.getStatusCode() == 401) {
					loginHandler.sendMessage(
							loginHandler.obtainMessage(1, null));
				} else {
					loginHandler.sendMessage(
							loginHandler.obtainMessage(2, null));
				}
			} catch (IOException e) {
				loginHandler.sendMessage(
						loginHandler.obtainMessage(2, null));
			}
		}
	};
	
	private final Handler loginHandler = new Handler () {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Toast.makeText(getApplicationContext(), "Redirecting...", 
						Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(MainActivity.this, ListItemsActivity.class);
				startActivity(myIntent);
			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "Wrong credentials",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "Network connection error",
						Toast.LENGTH_SHORT).show();
			}
			login.setEnabled(true);
		}
	};
	
	public void login(View view){
		Toast.makeText(getApplicationContext(), "Logging in...", 
				Toast.LENGTH_SHORT).show();
		new Thread(loginRunnable).start();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	private static Barternbargain getApiServiceHandle() {
		// Use a builder to help formulate the API request.
		Barternbargain.Builder builder = new Barternbargain.Builder(HTTP_TRANSPORT,
				GSON_FACTORY,null);
		return builder.build();
	}

	private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	private static final GsonFactory GSON_FACTORY = new GsonFactory();

}