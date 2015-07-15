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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import gh.com.zenithbank.ibank.Classes.CardRequests_Class;
import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/8/2015.
 */
public class PinRegenerationFragment extends Fragment
{

    String cardType;

    CardRequests_Class.RootObject cardRequests_class;
    CardRequests_Class.ProcessVariable processVariable;

    RadioButton VisaSelect;
    RadioButton EazyPaySelect;
    EditText cardNumber;
    EditText phoneNumber;
    EditText destinationBranch;
    Spinner cardAccountSpinner;
    Button submitButton;
    ProgressDialog progressDialog;

    ArrayAdapter<String> AccountAdapter;
    ArrayList<String> accts;
    DBEngine dbEngine;
    IbankEngine ibankEngine;

    String _acct;
    String _cardType;
    String _cardNo;
    String _mobileNo;
    String _desBranch;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.pin_regen_frag,container,false);

        try
        {
            ibankEngine = new IbankEngine();
            cardRequests_class = new CardRequests_Class.RootObject();
            cardRequests_class.processStartRequest = new CardRequests_Class.ProcessStartRequest();
            cardRequests_class.processStartRequest.processVariables =
                    new CardRequests_Class.ProcessVariables();

            VisaSelect = (RadioButton)v.findViewById(R.id.ChkBxNewRequest);
            EazyPaySelect = (RadioButton)v.findViewById(R.id.ChcbxRegeneration);
            cardAccountSpinner = (Spinner)v.findViewById(R.id.CardAccountSpinner);
            cardNumber = (EditText)v.findViewById(R.id.EdTxtCardNo);
            phoneNumber = (EditText)v.findViewById(R.id.EdTxtCardMobileNo);
            destinationBranch = (EditText)v.findViewById(R.id.EdtxtRequestEmail);
            submitButton = (Button)v.findViewById(R.id.BtnCardAccess);

            accts = new ArrayList<String>();
            dbEngine = new DBEngine();
            dbEngine.Init(getActivity().getApplicationContext());
            accts = dbEngine.getAccounts();

            AccountAdapter = new ArrayAdapter<String>
                    (getActivity().getApplicationContext(),
                            android.R.layout.simple_spinner_item,accts);
            AccountAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            cardAccountSpinner.setAdapter(AccountAdapter);

            VisaSelect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (EazyPaySelect.isChecked())
                    {
                        EazyPaySelect.setChecked(false);
                        cardType = "Visa";
                    }
                }
            });
            EazyPaySelect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (VisaSelect.isChecked())
                    {
                        VisaSelect.setChecked(false);
                        cardType = "EasyPay";
                    }
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    progressDialog = ProgressDialog.show(getActivity(),
                            "Processing...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                     _acct = cardAccountSpinner.getSelectedItem().toString().trim();
                     _cardType = cardType;
                     _cardNo = cardNumber.getText().toString().trim();
                     _mobileNo = phoneNumber.getText().toString().trim();
                     _desBranch = destinationBranch.getText().toString().trim();
                    new MakeComplaint().execute();
                }
            });
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }



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
                cardRequests_class.processStartRequest.key = Long.parseLong(_acct);
                cardRequests_class.processStartRequest.processName = "PIN Regeneration";
                cardRequests_class.processStartRequest.startingTasks = false;

                processVariable.name="CARDTYPE";
                processVariable.type = "string";
                processVariable.stringValue = _cardType;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="ACCOUNTNO";
                processVariable.type = "string";
                processVariable.stringValue = _acct;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="PANNUM";
                processVariable.type = "string";
                processVariable.stringValue = _cardNo;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="MOBILE";
                processVariable.type = "string";
                processVariable.stringValue = _mobileNo;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="BRANCH";
                processVariable.type = "string";
                processVariable.stringValue = _desBranch;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                IbankEngine ibankEngine = new IbankEngine();
                if(ibankEngine.MakePinRegenerationRequest(cardRequests_class))
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
