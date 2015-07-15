package gh.com.zenithbank.ibank.Notification;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import gh.com.zenithbank.ibank.R;

import static gh.com.zenithbank.ibank.Core.BalanceFragment.KEY_ACCOUNT_NUMBER;
import static gh.com.zenithbank.ibank.Core.BalanceFragment.KEY_AMOUNT;
import static gh.com.zenithbank.ibank.Core.BalanceFragment.KEY_CRNCY;
import static gh.com.zenithbank.ibank.Core.BalanceFragment.KEY_TITLE1;
import static gh.com.zenithbank.ibank.Core.BalanceFragment.KEY_TYPE;

/**
 * Created by Robby on 7/4/2015.
 */
public class RecordsAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    Random rand = new Random();
    int r = rand.nextInt(255);
    int _g = rand.nextInt(255);
    int b = rand.nextInt(255);


    public RecordsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.balance_row, null);

        TextView title = (TextView)vi.findViewById(R.id.accTitle); // title
        TextView number = (TextView)vi.findViewById(R.id.txtListAmount); // artist name
        TextView _type = (TextView)vi.findViewById(R.id.acctDesc); // duration
        TextView crncy = (TextView)vi.findViewById(R.id.acctCurrncy);
        TextView bal=(TextView)vi.findViewById(R.id._balance); // thumb image

        HashMap<String,String> song = new HashMap<String, String>();
        song = data.get(position);

        // Setting all values in listview
        title.setText(song.get(KEY_TITLE1));
        number.setText(song.get(KEY_ACCOUNT_NUMBER));
        _type.setText(song.get(KEY_TYPE));
        crncy.setText(song.get(KEY_CRNCY));
        bal.setText(song.get(KEY_AMOUNT));



        return vi;
    }
}
