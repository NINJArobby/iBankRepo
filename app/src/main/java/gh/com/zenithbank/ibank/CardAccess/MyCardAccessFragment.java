package gh.com.zenithbank.ibank.CardAccess;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import gh.com.zenithbank.ibank.Classes.CardRequests_Class;
import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/7/2015.
 */
public class MyCardAccessFragment extends Fragment
{

    CheckBox newRequest;
    CheckBox RegenRequest;

    Spinner account;

    EditText cardNumber;
    EditText mobileNo;
    EditText email;

    Button RequestBtn;

    String _type;
    String accountNo;
    String cardNo;
    String Mobile;
    String mail;
    DBEngine dbEngine;
    IbankEngine ibankEngine;

    ProgressDialog progressDialog;
    ArrayList<String> accts;
    ArrayAdapter<String> AccountAdapter;
    CardRequests_Class.RootObject cardRequests_class;
    CardRequests_Class.ProcessVariable processVariable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.my_card_access_fragment, container, false);

        newRequest = (CheckBox)v.findViewById(R.id.ChkBxNewRequest);
        RegenRequest = (CheckBox)v.findViewById(R.id.ChcbxRegeneration);
        account = (Spinner)v.findViewById(R.id.SpnRequestAccountNumber);
        cardNumber = (EditText)v.findViewById(R.id.EdtxtRequestCardNumber);
        mobileNo = (EditText)v.findViewById(R.id.EdtxtRequestPhoneNumber);
        email = (EditText)v.findViewById(R.id.EdtxtRequestEmail);
        RequestBtn = (Button)v.findViewById(R.id.BtnCardAccess);

        newRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(RegenRequest.isChecked())
                {
                    RegenRequest.setChecked(false);
                    _type = "New";
                }
            }
        });

        RegenRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(newRequest.isChecked())
                {
                    newRequest.setChecked(false);
                    _type = "Regeneration";
                }
            }
        });

        RequestBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog = ProgressDialog.show(getActivity().getApplicationContext(),
                        "Sending request...", "Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                 accountNo = account.getSelectedItem().toString().trim();
                 cardNo = cardNumber.getText().toString();
                 Mobile = mobileNo.getText().toString().trim();
                 mail = email.getText().toString().trim();
                new MakeComplaint().execute();
            }
        });

        accts = new ArrayList<String>();
        dbEngine = new DBEngine();
        dbEngine.Init(getActivity().getApplicationContext());
        accts = dbEngine.getAccounts();

        AccountAdapter = new ArrayAdapter<String>
                (getActivity().getApplicationContext(),
                        android.R.layout.simple_spinner_item,accts);
        AccountAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        account.setAdapter(AccountAdapter);
        return v;
    }

    class MakeComplaint extends AsyncTask<String,String,String>
    {
        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                cardRequests_class.processStartRequest.key = Long.parseLong(accountNo);
                cardRequests_class.processStartRequest.processName = "My Card Access";
                cardRequests_class.processStartRequest.startingTasks = false;

                processVariable.name="TYPE";
                processVariable.type = "string";
                processVariable.stringValue = _type;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="ACCOUNTNO";
                processVariable.type = "string";
                processVariable.stringValue = accountNo;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="PANNUM";
                processVariable.type = "string";
                processVariable.stringValue = cardNo;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="MOBILE";
                processVariable.type = "string";
                processVariable.stringValue = Mobile;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="EMAIL";
                processVariable.type = "string";
                processVariable.stringValue = mail;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);


                // processVariable.stringValue = transactionCode.getText().toString().trim();
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                IbankEngine ibankEngine = new IbankEngine();
                if(ibankEngine.MakeMyCardAccessRequest(cardRequests_class))
                {
                    done = true;
                }
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(done)
            {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Complaint Submitted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Error..Please try again.",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }
}
