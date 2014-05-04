package hk.ust.barternbargain;

import hk.ust.barternbargain.barternbargain.Barternbargain;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;


public class PersonalInfo extends Activity {
	
	
	private EditText editName;
	private EditText editEmailaddress;
	private EditText editPhone;
	private Button buttonEdit;
	private Button buttonSave;
	
	private User CurrentUser = new User("Sung Kim","88888888","abc@ust.hk",0);
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		

		editName = (EditText)findViewById(R.id.editName);
		editPhone = (EditText)findViewById(R.id.editPhone);
		editEmailaddress = (EditText)findViewById(R.id.editEmailaddress);
		
		editName.setText(CurrentUser.getUsername());
		editPhone.setText(CurrentUser.getPhone_number());
		editEmailaddress.setText(CurrentUser.getEmail());
		
	    editName.setEnabled(false);
	    editEmailaddress.setEnabled(false);
	    editPhone.setEnabled(false);
		
		buttonEdit = (Button)findViewById(R.id.buttonEdit);
		buttonEdit.setOnClickListener(new EditListener());
		
		buttonSave = (Button)findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new SaveListener());
		
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_personal_info,
					container, false);
			return rootView;
		}
	}
	

	
	public class EditListener implements OnClickListener{

		@Override
		public void onClick(View view) {
		    editName.setEnabled(true);
		    editEmailaddress.setEnabled(true);
		    editPhone.setEnabled(true);		    			
		}
		
	}
	
	public class SaveListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			CurrentUser.setUsername(editName.getText().toString());
			CurrentUser.setPhone_number(editPhone.getText().toString());
			CurrentUser.setEmail(editEmailaddress.getText().toString());
			
		    editName.setEnabled(false);
		    editEmailaddress.setEnabled(false);
		    editPhone.setEnabled(false);
			
		}
		
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
