package hk.ust.barternbargain;

import hk.ust.barternbargain.barternbargain.Barternbargain;
import hk.ust.barternbargain.barternbargain.model.Item;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowItemActivity extends Activity implements OnClickListener {

	private ProgressDialog progressDialog;
	private Button buyButton;
	private static final Long ITEM_ID = 5693417237512192L;
	private long myPrice = 0;
	
	private final Runnable loadImageRunnable = new Runnable () {
		public void run() {
			try {
				Barternbargain service = getApiServiceHandle();
				Item item = service.getItem(ITEM_ID).execute();
				List<String> imageUrls = item.getImageUrl();
				if (imageUrls.size() != 0) {
					URL url = new URL(imageUrls.get(0));
					Bitmap bmp = BitmapFactory.decodeStream(
							url.openConnection().getInputStream());
					loadImageHandler.sendMessage(
							loadImageHandler.obtainMessage(0, bmp));
				}
				loadImageHandler.sendMessage(
						loadImageHandler.obtainMessage(1, item));
			} catch (IOException e) {
				loadImageHandler.sendMessage(
						loadImageHandler.obtainMessage(2, e.getMessage()));
				
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
				Item item = (Item)msg.obj;
				TextView seller = (TextView)findViewById(R.id.textViewSeller);
				TextView name = (TextView)findViewById(R.id.textViewName);
				TextView description = (TextView)findViewById(R.id.textViewDescription);
				TextView price = (TextView)findViewById(R.id.textViewPrice);
				seller.setText(item.getUserId());
				if (item.getDescription() != null) {
					description.setText(item.getDescription());
				}
				name.setText(item.getName());
				price.setText("$" + Long.toString(item.getPrice()));
				myPrice = item.getPrice();
			} else if (msg.what == 2) {
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
		buyButton = (Button)findViewById(R.id.buttonLogin);
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
					Integer.toString((int)myPrice) + ".");
			builder.show();
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
