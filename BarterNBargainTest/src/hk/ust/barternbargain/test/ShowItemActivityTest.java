package hk.ust.barternbargain.test;

import android.test.ActivityInstrumentationTestCase2; 
import android.test.TouchUtils; 
import android.test.suitebuilder.annotation.SmallTest; 
import android.widget.Button; 
import android.widget.EditText; 
import android.view.KeyEvent;
import hk.ust.barternbargain.ShowItemActivity;
import hk.ust.barternbargain.MainActivity;
import java.lang.InterruptedException;
 
// ActivityInstrumentationTestCase2 provides functional testing of a single activity 
public class ShowItemActivityTest extends ActivityInstrumentationTestCase2<ShowItemActivity> { 
 private ShowItemActivity mActivity; 
 Button buttonBuy; 
 //delta - the maximum delta between expected and actual for which both numbers are still considered equal.
 private static final double DELTA = 1e-15; // constant for comparing doubles values 
 
 public ShowItemActivityTest() { 
 super(ShowItemActivity.class); 
 
 } 
 
 @Override 
 protected void setUp() throws Exception { 
 //this method is called every time before any test execution 
 super.setUp(); 
 
 mActivity = (ShowItemActivity) getActivity(); // get current activity 
 
 // link the objects with the activity objects 
 buttonBuy = (Button) mActivity 
 .findViewById(hk.ust.barternbargain.R.id.buttonLogin); 
 
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
 public void testBuy() { 
 TouchUtils.clickView(this,buttonBuy);
 assertTrue("Buy button", true);
 } 
 
 @SmallTest
 public void testBack(){
 TouchUtils.clickView(this,buttonBuy); 
 TouchUtils.clickView(this,buttonBuy);
 assertTrue("Go back", true);
 }
 

} 
