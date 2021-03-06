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
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowItemActivity extends Activity implements OnClickListener {

	private ProgressDialog progressDialog;
	private Bitmap bmp;
	private TextView textSeller;
	private TextView textName;
	private TextView textDescription;
	private TextView textPrice;
	private ImageView imageView;
	private Button buyButton;
	private Long itemId;
	private Item item;
	
	private final Runnable loadImageRunnable = new Runnable () {
		public void run() {
			try {
				Barternbargain service = getApiServiceHandle();
				item = service.getItem(itemId).execute();
				List<String> imageUrls = item.getImageUrl();
				if (imageUrls.size() != 0) {
					URL url = new URL(imageUrls.get(0));
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					bmp = BitmapFactory.decodeStream(
							url.openConnection().getInputStream());
					loadImageHandler.sendEmptyMessage(0);
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
				imageView.setImageBitmap((Bitmap)bmp);
			} else if (msg.what == 1) {
				Item item = (Item)msg.obj;
				textSeller.setText(item.getUserId());
				if (item.getDescription() != null) {
					textDescription.setText(item.getDescription());
				}
				textName.setText(item.getName());
				textPrice.setText("$" + Long.toString(item.getPrice()));
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
		itemId = getIntent().getLongExtra("ITEM_ID", 5693417237512192L);
		buyButton = (Button)findViewById(R.id.buttonLogin);

		textSeller = (TextView)findViewById(R.id.textViewSeller);
		textName = (TextView)findViewById(R.id.textViewName);
		textDescription = (TextView)findViewById(R.id.textViewDescription);
		textPrice = (TextView)findViewById(R.id.textViewPrice);

		imageView = (ImageView)findViewById(R.id.imageView1);
		
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
					item.getPrice().toString() + ".");
			builder.show();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Drawable background = imageView.getBackground();
		if (background != null) {
			background.setCallback(null);
		}
		if (bmp != null && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
		System.gc();
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
