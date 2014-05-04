package hk.ust.barternbargain;

import hk.ust.barternbargain.barternbargain.Barternbargain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Application;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ListItemsActivity extends ActionBarActivity implements OnClickListener {

	private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	private static final GsonFactory GSON_FACTORY = new GsonFactory();
	
	private Button search;
	private Button mostpopular;
	private Button price;
	private ListView list;
	private List<Item> items = new ArrayList<Item>();
	private List<Item> searcheditems = new ArrayList<Item>();
	private List<Item> sorteditems = new ArrayList<Item>();
	
	

	//flag for sorting by price
	public static int flag = 0;

	@Override 
	public void onClick(View v) { 
		onSearchRequested();
	}

	private static Barternbargain getApiServiceHandle() {
		// Use a builder to help formulate the API request.
		Barternbargain.Builder builder = new Barternbargain.Builder(HTTP_TRANSPORT,
				GSON_FACTORY,null);
		return builder.build();
	}

	private final Runnable loadItemRunnable = new Runnable () {
		public void run() {
			Barternbargain service = getApiServiceHandle();
			try {
				loadItemHandler.sendMessage(
						loadItemHandler.obtainMessage(0, 
								service.listItem().execute().getItems()));
			} catch (IOException e) {

			}
		}
	};

	private final Handler loadItemHandler = new Handler () {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				List<hk.ust.barternbargain.barternbargain.model.Item> items = 
						(List<hk.ust.barternbargain.barternbargain.model.Item>) msg.obj;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_items);
		list = (ListView) findViewById(R.id.itemsListView);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

		//ListItems
		populateItemList();
		populateListView();
		registerClickCallback();

		//Invoke MostPopular
		mostpopular = (Button)findViewById(R.id.button2);
		mostpopular.setOnClickListener(new MostPopularListener());

		//Invoke SortByPrice
		price = (Button)findViewById(R.id.button4);
		price.setOnClickListener(new PriceListener());
	}




	//Add Items
	private void populateItemList() {
		//more items to be added
		items.add(new Item(R.drawable.item1,"Textbook",999999,0,"New",0,5750085036015616L));
		items.add(new Item(R.drawable.item2,"Iphone 10",1,10,"New",0,5657382461898752L));
		items.add(new Item(R.drawable.item3,"Notebook",500,999,"New",1,5733935958982656L));
		items.add(new Item(R.drawable.item4,"Macbook",35,23,"New",0,5693417237512192L));
		items.add(new Item(R.drawable.item5,"Ball",9999,10,"New",1,5766466041282560L));
		items.add(new Item(R.drawable.item6,"Glasses",5,54,"Used",1,5634387206995968L));
		items.add(new Item(R.drawable.item7,"Coke",9998,9,"New",0,5700735861784576L));

	}

	//List All Items
	private void populateListView() {
		ArrayAdapter<Item> adapter = new MyListAdapter(items);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	//ClickItems
	private void registerClickCallback() {
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
					long id) {

				Item clickedItem = (Item)list.getAdapter().getItem(position);
				clickedItem.incrementViews();
				Intent intent = new Intent(getApplicationContext(),ShowItemActivity.class);
				intent.putExtra("ITEM_ID", clickedItem.getKey());
				startActivity(intent);


			}
		});

	}

	//ItemAdapter
	private class MyListAdapter extends ArrayAdapter<Item>{

		private List<Item> items;

		public MyListAdapter(List<Item> items){

			super(ListItemsActivity.this,R.layout.item_view,items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//Make sure there's a view to work with
			View itemView = convertView;


			if (itemView ==null){
				itemView = getLayoutInflater().inflate(R.layout.item_view, parent,false);

			}

			//find the item to work with

			Item currentItem = items.get(position);

			//fill the view
			ImageView imageView = (ImageView)itemView.findViewById(R.id.item1image);
			imageView.setImageResource(currentItem.getPictureID());

			//Name:
			TextView nameText = (TextView) itemView.findViewById(R.id.item1name);
			nameText.setText(currentItem.getName());

			//Price
			TextView priceText = (TextView) itemView.findViewById(R.id.item1price);
			priceText.setText(""+currentItem.getPrice());

			TextView viewText = (TextView) itemView.findViewById(R.id.item1views);
			viewText.setText(""+currentItem.getViews());

			return itemView;
		}
		
		public List<Item> getItems() {
			return items;
		}


	}

	//Generate Search query
	@Override
	public boolean onSearchRequested() {

		Bundle appDataBundle = new Bundle(); 
		startSearch("",false, appDataBundle, false); 
		return true; 
	}

	protected void onNewIntent(Intent intent){
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	//Do search and list searched items
	private void doMySearch(String query) {
		searcheditems.clear();
		for(int i=0;i<items.size();i++)
		{
			if(items.get(i).getName().equals(query))
			{
				searcheditems.add(items.get(i));
			}
		}

		ArrayAdapter<Item> adapter = new MyListAdapter(searcheditems);
		list.setAdapter(adapter);

	}

	//Do MostPopular and list sorted items

	public class MostPopularListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if (sorteditems.size() == 0) {
				sorteditems = items;
			}
			Collections.sort(sorteditems, new Comparator<Item>(){
				@Override
				public int compare(Item item1, Item item2){
					return item2.getViews() - item1.getViews();   				
				}
			});

			//Show sorted items
			ArrayAdapter<Item> adapter = new MyListAdapter(sorteditems);
			list.setAdapter(adapter);    	
		}
	}

	//Do SortByPrice and list sorted items
	public class PriceListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if (sorteditems.size() == 0) {
				sorteditems = items;
			}
			if (flag == 0) {
				Collections.sort(sorteditems, new Comparator<Item>() {
					@Override
					public int compare(Item item1, Item item2) {
						return item1.getPrice() - item2.getPrice();
					}
				});
				flag = 1;
			} else {
				Collections.sort(sorteditems, new Comparator<Item>() {
					@Override
					public int compare(Item item1, Item item2) {
						return item2.getPrice() - item1.getPrice();
					}
				});
				flag = 0;
			}
			ArrayAdapter<Item> adapter = new MyListAdapter(sorteditems);
			list.setAdapter(adapter);    
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_list_items_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// My Item function to be added here
		// Personal Info function to be added here
		int id = item.getItemId();
		if (id == R.id.action_personalinfo) {
			Intent myIntent = new Intent(ListItemsActivity.this, PersonalInfo.class);
			startActivity(myIntent);
		}
		if (id == R.id.action_search){
			onSearchRequested();
		}
		
		if (id == R.id.action_logout){
			Intent myIntent = new Intent(ListItemsActivity.this, MainActivity.class);
			startActivity(myIntent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
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
			View rootView = inflater.inflate(R.layout.fragment_list_items,
					container, false);
			return rootView;
		}
	}

}
