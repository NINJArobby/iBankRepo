package gh.com.zenithbank.ibank.Notification;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Robby on 7/4/2015.
 */
public class Records_Fragment extends Fragment
{
    LinearLayout _balanceHook;
    DBEngine dbEngine;
    IbankEngine ibankEngine;
    PtrFrameLayout mPtrFrame;
    ProgressDialog progressDialog;
    ListView balList;
    RecordsAdapter _adapter;

    private SwipeRefreshLayout mSwipeLayout;
    static final String KEY_TITLE1 = "song"; // parent node
    static final String KEY_ACCOUNT_NUMBER = "id";
    static final String KEY_TYPE = "title";
    static final String KEY_CRNCY = "artist";
    static final String KEY_AMOUNT = "duration";

    ArrayList<HashMap<String, String>> Balance_List;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.records, container, false);
        try
        {
            //_balanceHook = (LinearLayout)v.findViewById(R.id.balanceHook);
            mPtrFrame = (PtrFrameLayout) v.findViewById(R.id.ptr_frame);
            balList = (ListView) v.findViewById(R.id.PromptsHook);
            Balance_List = new ArrayList<HashMap<String, String>>();

            dbEngine = new DBEngine();
            dbEngine.Init(getActivity().getApplicationContext());
            ibankEngine = new IbankEngine();

            mSwipeLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_container);
            mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    progressDialog = ProgressDialog.show(getActivity(),
                            "Refreshing...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);

                }
            });

            mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light, android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            balList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {

                    TextView accountTitle = (TextView) view.findViewById(R.id.accTitle);
                    TextView accountNumber = (TextView) view.findViewById(R.id.txtListAmount);
                    TextView accountType = (TextView) view.findViewById(R.id.acctDesc);
                    TextView accountCrncy = (TextView) view.findViewById(R.id.acctCurrncy);
                    TextView accountBalance = (TextView) view.findViewById(R.id._balance);

                    makePopUp2(accountTitle.getText().toString().trim(),
                            accountNumber.getText().toString().trim(),
                            accountType.getText().toString().trim(),
                            accountCrncy.getText().toString().trim(),
                            accountBalance.getText().toString().trim());
                }
            });

            LoadRecords();
            _adapter = new RecordsAdapter(getActivity(), Balance_List);
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    balList.setAdapter(_adapter);
                }
            });
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }

        return v;

    }

    void makePopUp2(
            String accountTitle, String accountNumber, String accountType,
            String accountCrncy, String accountBalance)
    {
        try
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Balance")
                    .setMessage(
                            "Account Name: "+ accountTitle + "\n" +
                                    "Account Number: "+accountNumber + "\n" +
                                    "Account Type: "+accountType + "\n" +
                                    "Balance: "    +accountCrncy+ " " +accountBalance + "\n"

                               )
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //LoginActivity.home.finish();
                            //Records.this.finish();

                        }
                    })
                    .setIcon(R.drawable.zenith)
                    .show();
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }

    }


    void LoadRecords()
    {
        ArrayList<String> bals = dbEngine.FetchPrompts();
        String[] splitStr;
        for (int x = 0; x < bals.size(); x++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            String text = bals.get(x);
            String delimit = ";";
            StringTokenizer ress = new StringTokenizer(text, delimit);
            splitStr = new String[ress.countTokens()];
            int index = 0;
            while (ress.hasMoreElements())
            {
                splitStr[index++] = ress.nextToken();
            }
            map.put(KEY_TITLE1, splitStr[0].trim());
            map.put(KEY_ACCOUNT_NUMBER, splitStr[1].trim());
            map.put(KEY_TYPE, splitStr[2].trim());
            map.put(KEY_CRNCY, splitStr[3].trim());
            map.put(KEY_AMOUNT, splitStr[4].trim());
            Balance_List.add(map);


        }
    }
}
