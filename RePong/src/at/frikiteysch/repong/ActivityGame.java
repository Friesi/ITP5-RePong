package at.frikiteysch.repong;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ActivityGame extends Activity implements OnTouchListener {
	 
	ImageView paddle;
	float lastX = 0, lastY = 0;
	int displayWidth, displayHeight, paddleHalfWidth, lastLeftMargin;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.game_field);
	    
	    //Intent intent = getIntent();
	    //String value = intent.getStringExtra("key"); //if it's a string you stored.
	
	    paddle = (ImageView) findViewById(R.id.paddle);
	    
	    FrameLayout layout = (FrameLayout) findViewById(R.id.gameField);
        layout.setOnTouchListener(this);
        
        Display display = getWindowManager().getDefaultDisplay(); 
        
        if (android.os.Build.VERSION.SDK_INT >= 13) {
        	Point size = new Point();
        	display.getSize(size);
        	displayWidth = size.x;
        	displayHeight = size.y;
        }
        else {
        	displayWidth = display.getWidth();  // deprecated
        	displayHeight = display.getHeight();  // deprecated
        }
        
	}
	
	@Override 
	public void onWindowFocusChanged (boolean hasFocus) 
	{
		paddleHalfWidth = paddle.getWidth() / 2;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
	        {       
	            // Here u can write code which is executed after the user touch on the screen 
	        	lastX = event.getX();
	        	lastLeftMargin = ((FrameLayout.LayoutParams) paddle.getLayoutParams()).leftMargin;
	        	break; 
	        }
	        case MotionEvent.ACTION_UP:
	        {             
	            // Here u can write code which is executed after the user release the touch on the screen    
	        	
	            break;
	        }
	        case MotionEvent.ACTION_MOVE:
	        {  
	           // Here u can write code which is executed when user move the finger on the screen 
	        	
	        	FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) paddle.getLayoutParams();
	        	/*
	        	float tmpY = event.getY();
	        	float tmpX = event.getX();
	        	
	        	if (tmpX < lastX)
	        		params.leftMargin -= 10;
	        	else
	        		params.leftMargin += 10;*/
	        	
	        	params.gravity = Gravity.LEFT | Gravity.BOTTOM;
	        	
	        	//int tmpCalc = (int) (event.getX() - paddleHalfWidth);
	        	int tmpCalc = lastLeftMargin + (int) (event.getX() - lastX);
	        	
	        	//Toast.makeText(getApplicationContext(), lastLeftMargin + " " + event.getX() + " " + lastX, Toast.LENGTH_SHORT).show();
	        	
	        	if (tmpCalc < 0)
	        	{
	        		params.leftMargin = 0;
	        		lastX = event.getX();
	        		lastLeftMargin = ((FrameLayout.LayoutParams) paddle.getLayoutParams()).leftMargin;
	        	}
	        	else if (tmpCalc + paddleHalfWidth * 2 > displayWidth)
	        	{
	        		params.leftMargin = displayWidth - paddleHalfWidth * 2;
	        		lastX = event.getX();
	        		lastLeftMargin = ((FrameLayout.LayoutParams) paddle.getLayoutParams()).leftMargin;
	        	}
	        	else	        	
	        		params.leftMargin = tmpCalc;
	        	
	            //params.bottomMargin = (int) event.getY();
	            paddle.setLayoutParams(params);
	            paddle.invalidate();
	            break;
	        }
	    }
		
		
		
	    return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent myIntent = new Intent(this, ActivityStartScreen.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(myIntent);
	}
}
