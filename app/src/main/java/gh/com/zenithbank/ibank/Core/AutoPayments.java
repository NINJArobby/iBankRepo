package gh.com.zenithbank.ibank.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import gh.com.zenithbank.ibank.Classes.Bill_Schedule_Class;
import gh.com.zenithbank.ibank.Classes.Customer;
import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 3/25/2015.
 */
public class AutoPayments extends Activity
{

    private PendingIntent pendingIntent;
    DBEngine dbEngine;
    IbankEngine ibankEngine;
    String _pass;
    ProgressDialog progressDialog;
    ArrayList _Security;
    JsonParser parser;

    LinearLayout hook;
    Button Login;
    EditText _access;
    EditText _username;
    EditText _password;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbEngine = new DBEngine();
        dbEngine.Init(this);
        ibankEngine = new IbankEngine();

        if(dbEngine.EasyLogin())
        {
            //ask for password
            ShowPass();
        }
        else
        {
            setContentView(R.layout.full_login);
            Login = (Button)findViewById(R.id.AutoPayLogin);
            _access = (EditText)findViewById(R.id.logintxtAccessEnter);
            _username = (EditText)findViewById(R.id.logintxtUserEnter);
            _password = (EditText)findViewById(R.id.logintxtpassEnter);

            Login.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    progressDialog = ProgressDialog.show(AutoPayments.this,
                            "Logging In", "Please Wait...");
                    new DoLogin2InBackground().execute();
                }
            });

        }
    }


    void ShowPass()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Internet Banking Password");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example,
        // sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setIcon(R.drawable.icon);

        // Set up the buttons
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                try
                {
                    _pass = input.getText().toString().trim();
                    progressDialog = ProgressDialog.show(AutoPayments.this,
                            "Logging In", "Please Wait...");
                   new DoLoginInBackground().execute();
                }
                catch (Exception ex)
                {
                    ex.getMessage();
                }
            }
        });
        builder.show();
    }

    class DoLoginInBackground extends AsyncTask<String, String, String>
    {

        boolean done = false;

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                _Security = dbEngine.getSecurityValues();
                ArrayList logData = ibankEngine.Login(_Security.get(0).toString().trim(),
                        _Security.get(1).toString().trim(),_pass);
                if (logData.get(1).toString().trim().equals("error"))
                {
                    throw new Exception("LoginError");
                }
                else
                {
                    dbEngine.saveCookie(logData.get(0).toString().trim());
                    parser = new JsonParser();
                    String res4 = logData.get(1).toString().trim();
                    if (res4.equalsIgnoreCase("error"))
                    {
                        throw new Exception("error");
                    }
                    JsonArray jArray = parser.parse(res4).getAsJsonArray();
                    for (JsonElement obj : jArray)
                    {
                        Customer.AccountBalance cse =
                                new Gson().fromJson(obj, Customer.AccountBalance.class);
                        dbEngine.updateBalancesTable(
                                cse.acctType,
                                cse.acctNo,
                                cse.acctDesc,
                                cse.isoCurrency,
                                Double.toString(cse.curBal),
                                Double.toString(cse.acctAvail),
                                Integer.toString(cse.holdBal),
                                cse.title1
                                                    );
                    }
                }

                done = true;
            }

            catch (Exception ex)
            {
                ex.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute (String s)
        {
            if (done)
            {
                progressDialog.dismiss();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setContentView(R.layout.pending_payments);
                        hook = (LinearLayout)findViewById(R.id.BillsHook);
                        laodSchedules();
                    }
                });

            }
            else
            {
                progressDialog.dismiss();
                Error();
            }

        }
    }

    class DoLogin2InBackground extends AsyncTask<String, String, String>
    {
        boolean done = false;

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                _Security = dbEngine.getSecurityValues();
                ArrayList logData = ibankEngine.Login(_access.getText().toString().trim(),
                        _username.getText().toString().trim(),
                        _password.getText().toString().trim());
                if (logData.get(1).toString().trim().equals("error"))
                {
                    throw new Exception("LoginError");
                }
                else
                {
                    dbEngine.saveCookie(logData.get(0).toString().trim());
                    parser = new JsonParser();
                    String res4 = logData.get(1).toString().trim();
                    if (res4.equalsIgnoreCase("error"))
                    {
                        throw new Exception("error");
                    }
                    JsonArray jArray = parser.parse(res4).getAsJsonArray();
                    for (JsonElement obj : jArray)
                    {
                        Customer.AccountBalance cse =
                                new Gson().fromJson(obj, Customer.AccountBalance.class);
                        dbEngine.updateBalancesTable(
                                cse.acctType,
                                cse.acctNo,
                                cse.acctDesc,
                                cse.isoCurrency,
                                Double.toString(cse.curBal),
                                Double.toString(cse.acctAvail),
                                Integer.toString(cse.holdBal),
                                cse.title1
                                                    );
                    }
                }

                done = true;
            }

            catch (Exception ex)
            {
                ex.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute (String s)
        {
            if (done)
            {
                progressDialog.dismiss();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setContentView(R.layout.pending_payments);
                        hook = (LinearLayout)findViewById(R.id.BillsHook);
                        laodSchedules();
                    }
                });

            }
            else
            {
                progressDialog.dismiss();
                Error();
            }

        }
    }


    void Error()
    {
        // Locate the TextView
        new AlertDialog.Builder(this)
                .setTitle("Error!!!")
                .setMessage("Please check password")
                .setNegativeButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        ShowPass();
                    }
                }).show();
    }

    void laodSchedules()
    {
        try
        {
            TableLayout tableLayout = new TableLayout(getApplicationContext());
            ArrayList<Bill_Schedule_Class.RootObject> _Schedule = dbEngine.fetchBillsSchedule();
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(getResources().getColor(R.color.zenithred));

            TextView dueDate = new TextView(this);
            dueDate.setText("Due Date");
            dueDate.setTextColor(getResources().getColor(R.color.White));

            TextView BillType = new TextView(this);
            dueDate.setText("Bill Type");
            dueDate.setTextColor(getResources().getColor(R.color.White));

            TextView Amount = new TextView(this);
            dueDate.setText("Amount");
            dueDate.setTextColor(getResources().getColor(R.color.White));

            TextView Status = new TextView(this);
            dueDate.setText("Status");
            dueDate.setTextColor(getResources().getColor(R.color.White));

            tableRow.addView(dueDate);
            tableRow.addView(BillType);
            tableRow.addView(Amount);
            tableRow.addView(Status);

            tableLayout.addView(tableRow);

            for(Bill_Schedule_Class.RootObject _Schedule1 : _Schedule)
            {
                TableRow tableRow1 = new TableRow(this);

                TextView dueDate1 = new TextView(this);
                dueDate.setText(_Schedule1.billPayment.schedule_dt);
                dueDate.setTextColor(getResources().getColor(R.color.zenithred));
                tableRow.addView(dueDate);


                TextView BillType1 = new TextView(this);
                dueDate.setText(_Schedule1.billPayment.productName);
                dueDate.setTextColor(getResources().getColor(R.color.zenithred));

                TextView Amount1 = new TextView(this);
                dueDate.setText("GHS " + _Schedule1.billPayment.amount);
                dueDate.setTextColor(getResources().getColor(R.color.zenithred));

               if(_Schedule1.billPayment.status == 1)
               {
                   Button Status1 = new Button(this);
                   dueDate.setText("Payment Active");
                   dueDate.setTextColor(getResources().getColor(R.color.zenithred));
                   tableRow1.addView(Status1);
               }
                else
               {
                   Button Status1 = new Button(this);
                   dueDate.setText("Pay");
                   dueDate.setTextColor(getResources().getColor(R.color.zenithred));

                   Status1.setOnClickListener(new View.OnClickListener()
                   {
                       @Override
                       public void onClick(View v)
                       {
                           startActivity(new Intent(getApplicationContext(),Payment.class));
                       }
                   });

                   tableRow1.addView(Status1);
               }

                tableRow1.addView(dueDate1);
                tableRow1.addView(BillType1);
                tableRow1.addView(Amount1);


                tableLayout.addView(tableRow1);
            }

            hook.addView(tableLayout);
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }

    }

}
