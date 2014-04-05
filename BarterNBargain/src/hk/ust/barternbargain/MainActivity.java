package hk.ust.barternbargain;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import hk.ust.barternbargain.barternbargain.Barternbargain;
import hk.ust.barternbargain.barternbargain.model.Item;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.extensions.android.http.AndroidHttp;

public class MainActivity extends Activity implements OnClickListener {

	private Barternbargain service;
	private TextView textView;
	private Button button;
	private final Runnable retrieveItemList = new Runnable () {
		public void run() {
			try {
				List<Item> response = service.listItem().execute().getItems();
				String strList = "";
				for (Item item : response) {
					strList += item.getName() + ": " + item.getDescription() + "\n";
				}
				showTextHandler.sendMessage(showTextHandler.obtainMessage(0, strList));
			} catch (Exception e) {
				showTextHandler.sendMessage(showTextHandler.obtainMessage(0, e.toString()));
			}
		}
	};
	private Handler showTextHandler = new Handler () {
		public void handleMessage(Message msg) {
			textView.setText((String)msg.obj);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        
        Barternbargain.Builder builder = new Barternbargain.Builder(
        		AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        service = builder.build();
    }

	@Override
	public void onClick(View v) { 
		new Thread(retrieveItemList).start();
	}
    
}
