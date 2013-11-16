package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableSocket extends Socket implements Parcelable {
	private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ParcelableSocket> CREATOR = new Parcelable.Creator<ParcelableSocket>() {
        public ParcelableSocket createFromParcel(Parcel in) {
            return new ParcelableSocket(in);
        }

        public ParcelableSocket[] newArray(int size) {
            return new ParcelableSocket[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ParcelableSocket(Parcel in) {
        mData = in.readInt();
    }
    
    public ParcelableSocket(String dstName, int dstPort) throws UnknownHostException, IOException {
    	super(dstName, dstPort);
    }
}
