package at.frikiteysch.repong.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.frikiteysch.repong.R;

/**
 * This class is used, to display all players in the WaitingRoom.
 * Furthermore it sets the color of each player.
 *
 */
public class WaitingRoomArrayAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private final String[] values;
	  private final int maxPlayerCnt;

	  public WaitingRoomArrayAdapter(Context context, String[] values, int maxPlayerCnt) {
	    super(context, R.layout.activity_waiting_room_list_item, values);
	    this.context = context;
	    this.values = values;
	    this.maxPlayerCnt = maxPlayerCnt;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.activity_waiting_room_list_item, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.playerName);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.color);
	    String playerName = values[position];
	    if (playerName == null || playerName.equals("")) // no name
	    	textView.setText("no player yet");
	    else
	    {
	    	textView.setText(playerName);
	    	textView.setTextColor(Color.WHITE);
	    }
	    
	    switch(position)
	    {
	    	case 0:
	    		imageView.setColorFilter(context.getResources().getColor(R.color.red));
	    		break;
	    		
	    	case 1:
	    		imageView.setColorFilter(context.getResources().getColor(R.color.blue));
	    		break;
	    		
	    	case 2:
	    		if (maxPlayerCnt <= 2)
	    		{
	    			imageView.setVisibility(View.GONE);	// set gone
	    			textView.setVisibility(View.GONE);
	    		}
	    		else
	    			imageView.setColorFilter(context.getResources().getColor(R.color.yellow));
	    		break;
	    		
			case 3:
				if (maxPlayerCnt <= 3)
				{
					imageView.setVisibility(View.GONE);	// set gone
					textView.setVisibility(View.GONE);
				}
	    		else
	    			imageView.setColorFilter(context.getResources().getColor(R.color.green));
				break;
				
			default:
				break;
	    }

	    return rowView;
	  }
	} 