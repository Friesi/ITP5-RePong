package at.frikiteysch.repong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityFirstStartScreen extends Activity {

	private boolean invalidUserName;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start_screen);
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		invalidUserName = getIntent().getExtras().getBoolean("Error");
		if (invalidUserName)
			Toast.makeText(this, R.string.invalidUserName, Toast.LENGTH_LONG).show();
	}
	
	public void btnOkOnClick(View v) {
		EditText userNameTxt = (EditText)findViewById(R.id.txtName);
		String userName = userNameTxt.getText().toString();
		
		if(userName != null && !userName.isEmpty()) {
			Intent myIntent = new Intent(this, ActivityStartScreen.class);
			myIntent.putExtra("userName", userName);
			this.startActivity(myIntent);
			finish();
		}
    }
	
	public void btnExitOnClick(View v) {
		onBackPressed();
    }
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
