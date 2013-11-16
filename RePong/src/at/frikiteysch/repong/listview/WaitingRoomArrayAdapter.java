package at.frikiteysch.repong.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.frikiteysch.repong.R;

public class WaitingRoomArrayAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private final String[] values;

	  public WaitingRoomArrayAdapter(Context context, String[] values) {
	    super(context, R.layout.activity_waiting_room_list_item, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.activity_waiting_room_list_item, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.playerName);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.color);
	    textView.setText(values[position]);
	    
	    switch(position)
	    {
	    	case 0:
	    		imageView.setColorFilter(context.getResources().getColor(R.color.red));
	    		break;
	    		
	    	case 1:
	    		imageView.setColorFilter(context.getResources().getColor(R.color.blue));
	    		break;
	    		
	    	case 2:
	    		imageView.setColorFilter(context.getResources().getColor(R.color.yellow));
	    		break;
	    		
			case 3:
				imageView.setColorFilter(context.getResources().getColor(R.color.green));
				break;
				
			default:
				break;
	    }

	    return rowView;
	  }
	} 