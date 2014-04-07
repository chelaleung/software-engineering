package hk.ust.barternbargain;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ListItemsActivity extends ActionBarActivity implements OnClickListener {

	private Button search;
	private List<Item> items = new ArrayList<Item>();

	@Override 
	public void onClick(View v) { 
		onSearchRequested();
		startActivity(new Intent(ListItemsActivity.this, ShowItemActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_items);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		search = (Button)findViewById(R.id.button3);
		search.setOnClickListener(this);
		
		//ListItems
				populateItemList();
				populateListView();
	}

	@Override
	public boolean onSearchRequested() {

		Bundle appDataBundle = new Bundle(); 
		startSearch("",false, appDataBundle, false); 
		return true; 
	}
	
	//Add Items
	private void populateItemList() {
		//more items to be added
		items.add(new Item(R.drawable.item1,"Textbook",999999,999,"New","Cash"));
		items.add(new Item(R.drawable.item2,"Iphone 10",1,23,"New","Cash"));
		items.add(new Item(R.drawable.item3,"Notebook",500,34,"New","Barter"));
		items.add(new Item(R.drawable.item4,"Macbook",35,43,"New","Cash"));
		items.add(new Item(R.drawable.item5,"Ball",9999,23,"New","Barter"));
		items.add(new Item(R.drawable.item6,"Glasses",5,23,"Used","Barter"));
		items.add(new Item(R.drawable.item7,"Coke",9998,234,"New","Cash"));
		
	}
	
	//List Items
	private void populateListView() {
		ArrayAdapter<Item> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.itemsListView);
		list.setAdapter(adapter);
	}
	
	private class MyListAdapter extends ArrayAdapter<Item>{
		public MyListAdapter(){
			super(ListItemsActivity.this,R.layout.item_view,items);
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
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
