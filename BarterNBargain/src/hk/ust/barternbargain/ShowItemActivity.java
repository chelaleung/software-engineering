package hk.ust.barternbargain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ShowItemActivity extends Activity implements OnClickListener {

	private static final String imageUrl = "http://ihome.ust.hk/~mfptai/rubik.jpg";
	private ProgressDialog progressDialog;
	private Button buyButton;
	
	private final Runnable loadImageRunnable = new Runnable () {
		public void run() {
			try {
				URL url = new URL(imageUrl);
				Bitmap bmp = BitmapFactory.decodeStream(
						url.openConnection().getInputStream());
				loadImageHandler.sendMessage(
						loadImageHandler.obtainMessage(0, bmp));
			} catch (IOException e) {
				loadImageHandler.sendMessage(
						loadImageHandler.obtainMessage(1, e.getMessage()));
				
			}
		}
	};
	
	private final Handler loadImageHandler = new Handler () {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if (msg.what == 0) {
				ImageView imageView = (ImageView)findViewById(R.id.imageView1);
				imageView.setImageBitmap((Bitmap)msg.obj);
			} else if (msg.what == 1) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemActivity.this);
				builder.setTitle("Error");
				builder.setMessage((String)msg.obj);
				builder.show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_item);
		buyButton = (Button)findViewById(R.id.button1);
		buyButton.setOnClickListener(this);
		progressDialog = ProgressDialog.show(this, "Loading", "Retrieving item details...");
		new Thread(loadImageRunnable).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_item, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == buyButton) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemActivity.this);
			builder.setTitle("Item bought");
			builder.setMessage("You have bought the item for $" + 
					Integer.toString(50) + ".");
			builder.show();
		}
	}

}
