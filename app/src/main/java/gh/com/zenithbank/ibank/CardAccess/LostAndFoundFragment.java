package gh.com.zenithbank.ibank.CardAccess;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import gh.com.zenithbank.ibank.Classes.CardRequests_Class;
import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/7/2015.
 */
public class LostAndFoundFragment extends Fragment
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
    String date;

    ProgressDialog progressDialog;
    private DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;
    ArrayAdapter<String> AccountAdapter;
    IbankEngine ibankEngine;
    ArrayList accts;
    DBEngine dbEngine;

    CardRequests_Class.RootObject cardRequests_class;
    CardRequests_Class.ProcessVariable processVariable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.losta_and_found_fragment, container, false);


        try
        {
            newRequest = (CheckBox)v.findViewById(R.id.ChkBxNewRequest);
            RegenRequest = (CheckBox)v.findViewById(R.id.ChcbxRegeneration);
            account = (Spinner)v.findViewById(R.id.SpnRequestAccountNumber);
            cardNumber = (EditText)v.findViewById(R.id.EdtxtRequestCardNumber);
            mobileNo = (EditText)v.findViewById(R.id.EdtxtRequestPhoneNumber);
            email = (EditText)v.findViewById(R.id.EdtxtRequestEmail);
            RequestBtn = (Button)v.findViewById(R.id.BtnCardAccess);
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            ibankEngine = new IbankEngine();
            dbEngine = new DBEngine();
            dbEngine.Init(getActivity().getApplicationContext());
            accts = dbEngine.getAccounts();

            Calendar newCalendar = Calendar.getInstance();


            DatePickerDialog = new DatePickerDialog(getActivity().getApplicationContext(),
                    new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    email.setText(dateFormatter.format(newDate.getTime()));
                    YoYo.with(Techniques.Bounce).duration(1000)
                            .playOn(email);

                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


            newRequest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(RegenRequest.isChecked())
                    {
                        RegenRequest.setChecked(false);
                        _type = "Visa";
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
                        _type = "EazyPay";
                    }
                }
            });

            email.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DatePickerDialog.show();
                }
            });
            account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    accountNo = account.getSelectedItem().toString().trim();
                    ((TextView) parent.getChildAt(0)).setTextSize(20);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.RED);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            RequestBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    cardNo = cardNumber.getText().toString().trim();
                    Mobile = mobileNo.getText().toString().trim();
                    date = email.getText().toString().trim();

                    progressDialog = ProgressDialog.show(getActivity(),
                            "Processing...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);

                    new MakeComplaint().execute();
                    //todo complete here
                }
            });
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }

        AccountAdapter = new ArrayAdapter<String>
                (getActivity().getApplicationContext(),
                        android.R.layout.simple_spinner_item,accts);
        AccountAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

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
                cardRequests_class.processStartRequest.processName = "Reactivation";
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

                processVariable.name="LOSTDATE";
                processVariable.type = "date";
                processVariable.stringValue = date;
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);


                // processVariable.stringValue = transactionCode.getText().toString().trim();
                cardRequests_class.processStartRequest.processVariables.
                        processVariable.add(processVariable);

                IbankEngine ibankEngine = new IbankEngine();
                if(ibankEngine.MakeLostAndFoundRequest(cardRequests_class))
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
