package gh.com.zenithbank.ibank.CardAccess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class CardRedispatchFragment extends Fragment
{

    CheckBox newRequest;
    CheckBox RegenRequest;
    CheckBox _newCard;
    CheckBox _captured;

    Spinner account;

    EditText cardNumber;
    EditText mobileNo;
    EditText OldDestination;
    EditText NewDestination;

    DBEngine dbEngine;
    IbankEngine ibankEngine;

    Button RequestBtn;
    ProgressDialog progressDialog;

    ArrayList<String> accts;
    ArrayAdapter<String> AccountAdapter;
    String cardType;
    String accountNumber;
    String mobileNumber;
    String CardsState;
    String otherBankBranch;
    String otherBankName;
    String branch;
    String oldDestination;
    String newDestintion;


    CardRequests_Class.RootObject cardRequests_class;
    CardRequests_Class.ProcessVariable processVariable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.redispatch_fragment, container, false);

        try
        {
            newRequest = (CheckBox)v.findViewById(R.id.ChkBxRedisVisa);
            RegenRequest = (CheckBox)v.findViewById(R.id.ChcbxRedisEasypay);
            _newCard = (CheckBox)v.findViewById(R.id.ChkBxRedisNewCard);
            _captured = (CheckBox)v.findViewById(R.id.ChcbxRedisCapturedCard);


            account = (Spinner)v.findViewById(R.id.SpnRequestAccountNumber);
            cardNumber = (EditText)v.findViewById(R.id.EdtxtRequestCardNumber);
            mobileNo = (EditText)v.findViewById(R.id.EdtxtRequestPhoneNumber);
            OldDestination = (EditText)v.findViewById(R.id.EdtxtRedispatchOldDestinationBranch);
            NewDestination = (EditText)v.findViewById(R.id.EdtxtRedispatchDestinationBranch);
            RequestBtn = (Button)v.findViewById(R.id.BtnCardAccess);

            newRequest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(RegenRequest.isChecked())
                    {
                        RegenRequest.setChecked(false);
                        cardType = "Visa";
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
                        cardType = "EasyPay";
                    }
                }
            });

            _newCard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(_captured.isChecked())
                    {
                        _captured.setChecked(false);
                        CardsState = "New Card";
                    }
                }
            });

            _captured.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(_newCard.isChecked())
                    {
                        _newCard.setChecked(false);
                        CardsState = "Captured Card";
                        showCapturedPopUp();
                    }
                }
            });

            RequestBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    progressDialog = ProgressDialog.show(getActivity(),
                            "Processing...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    accountNumber = account.getSelectedItem().toString().trim();
                    mobileNumber = mobileNo.getText().toString().trim();
                    oldDestination = OldDestination.getText().toString().trim();

                    new MakeComplaint().execute();
                }
            });

        }
        catch (Exception ex)
        {
            ex.getMessage();
        }

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
                cardRequests_class.processStartRequest.key = Long.parseLong(accountNumber);
                cardRequests_class.processStartRequest.processName = "Card Redispatch";
                cardRequests_class.processStartRequest.startingTasks = false;


                processVariable.name="CARDTYPE";
                processVariable.type = "string";
                processVariable.stringValue = cardType;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="ACCOUNTNO";
                processVariable.type = "string";
                processVariable.stringValue = accountNumber;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="MOBILE";
                processVariable.type = "string";
                processVariable.stringValue = mobileNumber;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="CARDSTATE";
                processVariable.type = "string";
                processVariable.stringValue = CardsState;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="BANK";
                processVariable.type = "string";
                processVariable.stringValue = otherBankName;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="CAPBRANCH";
                processVariable.type = "string";
                processVariable.stringValue = otherBankBranch;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="OLDBRANCH";
                processVariable.type = "string";
                processVariable.stringValue = oldDestination;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="BRANCH";
                processVariable.type = "string";
                processVariable.stringValue = newDestintion;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                IbankEngine ibankEngine = new IbankEngine();
                if(ibankEngine.MakeCardRedispatchRequest(cardRequests_class))
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

    public void showCapturedPopUp()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());

        View promptsView = li.inflate(R.layout.catured_place_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);

        // set dialog message

        alertDialogBuilder.setTitle("Captured Venue");
        alertDialogBuilder.setIcon(R.drawable.logoz);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText _bankname= (EditText) promptsView
                .findViewById(R.id.Bank);
        final EditText _branchname= (EditText) promptsView
                .findViewById(R.id.Branch);
        final Button timesBtn = (Button)promptsView.findViewById(R.id.btnCardVenue);
        timesBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                otherBankBranch =_bankname.getText().toString().trim();
                otherBankName =_branchname.getText().toString().trim();
                alertDialog.dismiss();
            }
        });

        // reference UI elements from my_dialog_layout in similar fashion


        // show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }
}
