package at.frikiteysch.repong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ActivityFirstStartScreen extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start_screen);
    }
	
	public void btnOkOnClick(View v) {
		EditText userNameTxt = (EditText)findViewById(R.id.txtName);
		String userName = userNameTxt.getText().toString();
		
		if(userName != null && !userName.isEmpty()) {
			Intent myIntent = new Intent(this, ActivityStartScreen.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
