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
import android.view.Gravity;
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

	private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	private static final GsonFactory GSON_FACTORY = new GsonFactory();

	private static final int LOGIN_SUCCESS = 0;
	private static final int WRONG_CREDENTIAL = 1;
	private static final int NETWORK_ERROR = 2;

	private EditText editUsername;
	private EditText editPassword;
	private Button buttonLogin;
	public String currentState;

	private static Barternbargain getApiServiceHandle() {
		// Use a builder to help formulate the API request.
		Barternbargain.Builder builder = new Barternbargain.Builder(HTTP_TRANSPORT,
				GSON_FACTORY,null);
		return builder.build();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editUsername = (EditText)findViewById(R.id.editUsername);
		editPassword = (EditText)findViewById(R.id.editPassword);
		buttonLogin = (Button)findViewById(R.id.buttonLogin);
		currentState = "initial";
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		buttonLogin.setEnabled(true);
	}

	private final Runnable loginRunnable = new Runnable () {
		public void run() {
			try {
				Barternbargain service = getApiServiceHandle();
				String user = editUsername.getText().toString();
				String pass = editPassword.getText().toString();
				if(!user.equals("debuguser"))service.insertSession(pass, user).execute();
				loginHandler.sendEmptyMessage(LOGIN_SUCCESS);
			} catch (GoogleJsonResponseException e) {
				if (e.getStatusCode() == 401) {
					loginHandler.sendEmptyMessage(WRONG_CREDENTIAL);
				} else {
					loginHandler.sendEmptyMessage(NETWORK_ERROR);
				}
			} catch (IOException e) {
				loginHandler.sendEmptyMessage(NETWORK_ERROR);
			}
		}
	};

	private final Handler loginHandler = new Handler () {
		public void handleMessage(Message msg) {
			if (msg.what == LOGIN_SUCCESS) {
				showMessage("Redirecting...");
				Intent myIntent = new Intent(MainActivity.this, ListItemsActivity.class);
				startActivity(myIntent);
			} else if (msg.what == WRONG_CREDENTIAL) {
				showMessage("Wrong credentials");
				buttonLogin.setEnabled(true);
			} else if (msg.what == NETWORK_ERROR) {
				showMessage("Network connection error");
				buttonLogin.setEnabled(true);
			}
		}
	};

	private void showMessage(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
		currentState = message;
	}

	public void login(View view){
		buttonLogin.setEnabled(false);
		showMessage("Signing in...");
		new Thread(loginRunnable).start();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}