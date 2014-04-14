package hk.ust.barternbargain.test;

import android.test.ActivityInstrumentationTestCase2; 
import android.test.TouchUtils; 
import android.test.suitebuilder.annotation.SmallTest; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.Toast;
import android.view.KeyEvent;
import hk.ust.barternbargain.ListItemsActivity;
import hk.ust.barternbargain.MainActivity;
import java.lang.InterruptedException;
 
// ActivityInstrumentationTestCase2 provides functional testing of a single activity 
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> { 
 private MainActivity mActivity; 
 Button buttonLogin; 
 EditText textUsername, textPassword;
 String currentState;
 private final String WrongCredential = "Wrong credentials";
 private final String Signin = "Signing in...";
 private final String LoginSuccess = "Redirecting...";
 //delta - the maximum delta between expected and actual for which both numbers are still considered equal.
 private static final double DELTA = 1e-15; // constant for comparing doubles values 
 
 public MainActivityTest() { 
 super(MainActivity.class); 
 
 } 
 
 @Override 
 protected void setUp() throws Exception { 
 //this method is called every time before any test execution 
 super.setUp(); 
 
 mActivity = (MainActivity) getActivity(); // get current activity 
 
 // link the objects with the activity objects 
 textUsername = (EditText) mActivity 
 .findViewById(hk.ust.barternbargain.R.id.editUsername); 
 textPassword = (EditText) mActivity 
 .findViewById(hk.ust.barternbargain.R.id.editPassword); 
 buttonLogin = (Button) mActivity 
 .findViewById(hk.ust.barternbargain.R.id.buttonLogin); 
 
 } 
 
 
 @Override 
 protected void tearDown() throws Exception { 
 //this method is called every time after any test execution 
 // we want to clean the texts 
 textUsername.clearComposingText(); 
 textPassword.clearComposingText(); 
 super.tearDown(); 
 } 
 
 @SmallTest // SmallTest: this test doesn't interact with any file system or network. 
 public void testView() { // checks if the activity is created 
 assertNotNull(getActivity()); 
 } 
 
 @SmallTest 
 public void testWrongCredential() { 
 
 /* INTERACTIONS */ 
 TouchUtils.tapView(this, textUsername); 
 sendKeys("A B C");
 TouchUtils.tapView(this, textPassword); 
 sendKeys("1 2 3");
 TouchUtils.clickView(this, buttonLogin);
 try{
	 Thread.sleep(3000);
 }
 catch (InterruptedException e) {
 }
 currentState = mActivity.currentState;
 assertTrue(currentState, currentState.equals(WrongCredential));
 } 
 
 @SmallTest 
 public void testSignin() { 
 
 /* INTERACTIONS */ 
 TouchUtils.tapView(this, textUsername); 
 sendKeys("A B C");
 TouchUtils.tapView(this, textPassword); 
 sendKeys("1 2 3");
 TouchUtils.clickView(this, buttonLogin);
 try{
	 Thread.sleep(100);
 }
 catch (InterruptedException e) {
 }
 currentState = mActivity.currentState;
 assertTrue(currentState, currentState.equals(Signin));
 } 
 

 @SmallTest 
 public void testLoginSuccess() { 
 
 /* INTERACTIONS */ 
 TouchUtils.clickView(this, textUsername); 
 sendKeys("D E B U G U S E R");
 TouchUtils.tapView(this, textPassword); 
 sendKeys("1 2 3");
 TouchUtils.clickView(this, buttonLogin);
 try{
	 Thread.sleep(3000);
 }
 catch (InterruptedException e) {
 }
 currentState = mActivity.currentState;
 assertTrue(currentState, currentState.equals(LoginSuccess));
 sendKeys(KeyEvent.KEYCODE_BACK);
 } 
} 
