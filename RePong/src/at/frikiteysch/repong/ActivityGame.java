package at.frikiteysch.repong;

import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import at.frikiteysch.repong.communication.AsyncTaskSend;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.defines.Position;
import at.frikiteysch.repong.services.GamePlayService;
import at.frikiteysch.repong.services.WaitingRoomGetComWaitInfo;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityGame extends Activity implements OnTouchListener, AsyncTaskStateReceiver<ComGameData> {
	 
	ImageView paddle;
	ImageView ball;
	float lastX = 0, lastY = 0;
	int displayWidth, displayHeight, paddleHalfWidth, lastLeftMargin;
	private int screenWidth, screenHeight;
	private int gameId;
	
	private Intent gamePlayIntent = new Intent(this, GamePlayService.class);
	
	private static final Logger LOGGER = Logger.getLogger(ActivityGame.class.getName());
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.game_field);
	    
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
	    wm.getDefaultDisplay().getMetrics(displayMetrics);
	    screenWidth = displayMetrics.widthPixels;
	    screenHeight = displayMetrics.heightPixels;
	    //Intent intent = getIntent();
	    //String value = intent.getStringExtra("key"); //if it's a string you stored.
	
	    paddle = (ImageView) findViewById(R.id.paddle);
	    ball = (ImageView) findViewById(R.id.ball);
	    
	    gameId = getIntent().getIntExtra("gameId", -1);
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
        
        Intent intent = new Intent(this, GamePlayService.class);
        startService(intent);
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
	protected void onStop() {
		super.onStop();
		Intent intent = new Intent(this, GamePlayService.class);
		stopService(intent);
	};
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, GamePlayService.class);
		stopService(intent);
		
		ComLeaveGame leaveGame = new ComLeaveGame();
		leaveGame.setGameId(gameId);
		leaveGame.setUserId(ProfileManager.getInstance().getProfile().getUserId());
		
		AsyncTaskSend<ComLeaveGame> task = new AsyncTaskSend<ComLeaveGame>(leaveGame);
		task.execute();
		
		Intent myIntent = new Intent(this, ActivityStartScreen.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(myIntent);
	}
	
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context ctx, Intent intent) {
			sendPaddlePosition();
		}
	};
	
	private void sendPaddlePosition()
	{
		ComPaddlePosition position = new ComPaddlePosition();
		position.setGameId(gameId);
		position.setUserId(ProfileManager.getInstance().getProfile().getUserId());
		position.setPositionNorm(10); // TODO change to right position
		AsyncTaskSendReceive<ComPaddlePosition, ComGameData> task = new AsyncTaskSendReceive<ComPaddlePosition, ComGameData>(ComGameData.class, this, position);
		task.execute();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		IntentFilter filter = new IntentFilter();
	    filter.addAction(GamePlayService.updateRequestAction);
	    LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}

	@Override
	public void receivedOkResult(ComGameData resultObject) {
		//TODO update ui ball and other paddles
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ball.getLayoutParams();
		Position p = resultObject.getBall().getPosition();
		
		int leftMargin = (int) (p.getX() * (((double)screenWidth)/1000D));
		int topMargin = (int) (p.getY() * (((double)screenHeight)/1000D));
		
		params.leftMargin = leftMargin;
		params.topMargin = topMargin;
		
		ball.setLayoutParams(params);
		ball.invalidate();
		
		LOGGER.info("received game data successfully");
		LOGGER.info("ball position:" + leftMargin + "/" + topMargin);
	}

	@Override
	public void receivedError(ComError errorObject) {
		//TODO end game? or just do nothing
	}
}
