package hk.ust.barternbargain.test;

import android.test.ActivityInstrumentationTestCase2; 
import android.test.TouchUtils; 
import android.test.suitebuilder.annotation.SmallTest; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.ListView;
import android.view.KeyEvent;
import hk.ust.barternbargain.Item;
import hk.ust.barternbargain.ListItemsActivity;
import hk.ust.barternbargain.R;

import java.lang.InterruptedException;
 
// ActivityInstrumentationTestCase2 provides functional testing of a single activity 
public class ListItemsActivityTest extends ActivityInstrumentationTestCase2<ListItemsActivity> { 
 private ListItemsActivity mActivity; 
 Button mostPopular,buttonPrice; 
 EditText textSearch;
 ListView list;
 //delta - the maximum delta between expected and actual for which both numbers are still considered equal.
 private static final double DELTA = 1e-15; // constant for comparing doubles values 
 
 public ListItemsActivityTest() { 
 super(ListItemsActivity.class); 
 
 } 
 
 @Override 
 protected void setUp() throws Exception { 
 //this method is called every time before any test execution 
 super.setUp(); 
 
 mActivity = (ListItemsActivity) getActivity(); // get current activity 
 
 // link the objects with the activity objects 
 mostPopular = (Button) mActivity 
 .findViewById(hk.ust.barternbargain.R.id.button2); 
 buttonPrice = (Button) mActivity 
 .findViewById(hk.ust.barternbargain.R.id.button4); 
 list = (ListView) mActivity
 .findViewById(hk.ust.barternbargain.R.id.itemsListView);
 } 
 
 
 @Override 
 protected void tearDown() throws Exception { 
 //this method is called every time after any test execution 
 // we want to clean the texts 
 super.tearDown(); 
 } 
 
 @SmallTest // SmallTest: this test doesn't interact with any file system or network. 
 public void testView() { // checks if the activity is created 
 assertNotNull(getActivity()); 
 } 
 
 @SmallTest 
 public void testCount() { 
 
 /* INTERACTIONS */ 
 assertEquals("Count of item ", list.getAdapter().getCount(), 7);
 } 
 
 @SmallTest 
 public void testMostPopular() { 
 
 /* INTERACTIONS */ 
 TouchUtils.clickView(this, mostPopular); 
 Item firstItem = (Item)list.getAdapter().getItem(0);
 assertEquals("Name of most popular item", firstItem.getName(), "Notebook");
 } 
 
 @SmallTest 
 public void testHighPrice() { 
 
 /* INTERACTIONS */ 
 TouchUtils.clickView(this, buttonPrice); 
 TouchUtils.clickView(this, buttonPrice); 
 Item firstItem = (Item)list.getAdapter().getItem(0);
 assertEquals("Name of most expensive item", firstItem.getName(), "Textbook");
 } 
 
 @SmallTest 
 public void testLowPrice() { 
 
 /* INTERACTIONS */ 
 TouchUtils.clickView(this, buttonPrice); 
 Item firstItem = (Item)list.getAdapter().getItem(0);
 assertEquals("Name of cheapest item", firstItem.getName(), "Iphone 10");
 } 
 
 @SmallTest 
 public void testFailedSearch() { 
 
 /* INTERACTIONS */ 
 getInstrumentation().invokeMenuActionSync(mActivity, R.id.action_search, 0);
 try{
	 Thread.sleep(1000);
 }
 catch (InterruptedException e) {
 }
 sendKeys("I P A D");
 sendKeys("ENTER");
 assertEquals("Count of item ", list.getAdapter().getCount(), 0);
 } 
 
 @SmallTest 
 public void testSuccessSearch() { 
 
 /* INTERACTIONS */ 
 getInstrumentation().invokeMenuActionSync(mActivity, R.id.action_search, 0);
 try{
	 Thread.sleep(1000);
 }
 catch (InterruptedException e) {
 }
 sendKeys("M A C B O O K");
 sendKeys("ENTER");
 Item firstItem = (Item)list.getAdapter().getItem(0);
 assertEquals("Name of searched item", firstItem.getName(), "Macbook");
 } 

 
} 
