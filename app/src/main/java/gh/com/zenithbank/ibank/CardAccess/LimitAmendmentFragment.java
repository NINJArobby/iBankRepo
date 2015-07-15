package gh.com.zenithbank.ibank.CardAccess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import gh.com.zenithbank.ibank.Classes.CardRequests_Class;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/7/2015.
 */
public class LimitAmendmentFragment extends Fragment
{
    RadioButton Visa;
    RadioButton EasyPay;
    RadioButton OutetATM;
    RadioButton OutletOther;
    RadioButton typePerm;
    RadioButton typeTemp;
    Spinner accountSpinner;
    EditText cardnumber;
    EditText mobilenumber;
    EditText currentlimit;
    EditText newlimit;

    ArrayList<String> accts;
    String cardType;
    String accountNumber;
    String cardPan;
    String mobileNumber;
    String TransType;
    String ATMDetailString;
    String OtherDetailsStirng;
    String TimesCharged;
    String terminal;
    boolean accept;
    String transDate;
    String _amount;
    String country;
    String TransCode;

    String fromAmount;
    String toAmount;
    String outletString;
    String tempString;
    String datefrom;
    String dateto;

    int typp;
    String Ramount;
    String Damount;
    String ATMLocationString;
    String OtherbankName;

    ProgressDialog progressDialog;
    Button MakeRequest;

    ArrayAdapter<String> AccountAdapter;
    ArrayAdapter<String> transAdapter;
    ArrayAdapter<String> AtmOptionAdapter;
    ArrayAdapter<String> OtherOptionAdopter;

    CardRequests_Class.RootObject cardRequests_class;
    CardRequests_Class.ProcessVariable processVariable;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.limit_amendment_fragment, container, false);
        try
        {
            //region Initialising group

            Visa = (RadioButton)v.findViewById(R.id.RdoBtnVisa);
            EasyPay = (RadioButton)v.findViewById(R.id.RdoBtnEazyPay);
             OutetATM= (RadioButton)v.findViewById(R.id.RdoBtnOutletATM);
             OutletOther = (RadioButton)v.findViewById(R.id.RdoBtnOutletOther);
             typePerm = (RadioButton)v.findViewById(R.id.RdoBtnTypePermanent);
             typeTemp = (RadioButton)v.findViewById(R.id.RdoBtTypeTemp);

            accountSpinner = (Spinner)v.findViewById(R.id.CardAccountSpinner);


            cardnumber = (EditText)v.findViewById(R.id.EdTxtCardNo);
            mobilenumber = (EditText)v.findViewById(R.id.EdTxtCardMobileNo);
            currentlimit = (EditText)v.findViewById(R.id.EdtxtLimitAmendmentCurrentLimit);
            newlimit = (EditText)v.findViewById(R.id.EdtxtLimitAmendmentNewLimit);
            MakeRequest = (Button)v.findViewById(R.id.btnLimitRequest);
            //endregion


            //region Listeners group

            typeTemp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showTempPopUp();
                }
            });
            Visa.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(EasyPay.isChecked())
                    {
                        EasyPay.setChecked(false);
                        cardType="Visa";
                    }
                }
            });
            EasyPay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(Visa.isChecked())
                    {
                        Visa.setChecked(false);
                        cardType="EazyPay";
                    }
                }
            });

            OutetATM.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(OutletOther.isChecked())
                    {
                        OutletOther.setChecked(false);
                        outletString = "ATM";
                    }
                }
            });

            OutletOther.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(OutetATM.isChecked())
                    {
                        OutetATM.setChecked(false);
                        outletString = "POS/ONLINE/MOTO";
                    }
                }
            });

            typePerm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(typeTemp.isChecked())
                    {
                        typeTemp.setChecked(false);
                        tempString = "Permanent";
                    }
                }
            });

            typeTemp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(typePerm.isChecked())
                    {
                        typePerm.setChecked(false);
                        tempString = "Temporary";
                    }
                }
            });




            accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            cardnumber.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    cardnumber.setText("");
                }
            });
            mobilenumber.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mobilenumber.setText("");
                }
            });

            MakeRequest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    progressDialog = ProgressDialog.show(getActivity().getApplicationContext(),
                            "Sending request...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    fromAmount = currentlimit.getText().toString().trim();
                    toAmount = newlimit.getText().toString().trim();

                    new MakeComplaint().execute();
                }
            });

            //endregion



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
                cardRequests_class.processStartRequest.key = Long.parseLong(accountNumber);
                cardRequests_class.processStartRequest.processName = "Limit Amendment";
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

                processVariable.name="PANNUM";
                processVariable.type = "string";
                processVariable.stringValue = cardPan;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="MOBILE";
                processVariable.type = "string";
                processVariable.stringValue = mobileNumber;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="FROM";
                processVariable.type = "string";
                processVariable.stringValue = _amount;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="TO";
                processVariable.type = "string";
                processVariable.stringValue = _amount;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="OUTLET";
                processVariable.type = "string";
                processVariable.stringValue = TransType;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="TEMPORARY";
                processVariable.type = "string";
                processVariable.stringValue = tempString;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                processVariable.name="DATEFROM";
                processVariable.type = "date";
                processVariable.stringValue = datefrom;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);


                processVariable.name="DATETO";
                processVariable.type = "date";
                processVariable.stringValue = dateto;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

               // processVariable.stringValue = transactionCode.getText().toString().trim();
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                IbankEngine ibankEngine = new IbankEngine();
                if(ibankEngine.MakeLimitAmendmentRequest(cardRequests_class))
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

    public void showTempPopUp()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());

        View promptsView = li.inflate(R.layout.my_dialog_layout_temp, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // set dialog message

        alertDialogBuilder.setTitle("Set Dates");
        alertDialogBuilder.setIcon(R.drawable.logoz);
        // create alert dialog
        final EditText fromText = (EditText)promptsView.findViewById(R.id.fromDAte);
        final EditText toText = (EditText)promptsView.findViewById(R.id.toDate);
        final  Button setDate = (Button)promptsView.findViewById(R.id.setDate);

        setDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datefrom = fromText.getText().toString().trim();
                dateto = toText.getText().toString().trim();
                alertDialog.dismiss();
            }
        });


        // show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }
}
