package gh.com.zenithbank.ibank.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import gh.com.zenithbank.ibank.Classes.Customer;
import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.IbankEngine;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/4/2015.
 */
public class Full_Login extends Activity
{

    IbankEngine ibankEngine;
    DBEngine dbEngine;
    ArrayList _Security;
    ProgressDialog progressDialog;
    Intent hub;
    Start start;

    EditText passWord;
    Button _loginBtn;
    public static Activity LogIn;
    JsonParser parser;

    EditText _access;
    EditText _user;
    EditText _pass;

    PowerManager pm;
    PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_login);

        _access = (EditText)findViewById(R.id.logintxtUserEnter);
        _user = (EditText)findViewById(R.id.logintxtAccessEnter);
        _pass = (EditText)findViewById(R.id.logintxtpassEnter);
        _loginBtn = (Button) findViewById(R.id.AutoPayLogin);

        LogIn = this;
        ibankEngine = new IbankEngine();
        dbEngine = new DBEngine();
        dbEngine.Init(this);
        hub = new Intent(this, Hub.class);
        Start._start.finish();
        _Security = new ArrayList();

        _user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _user.setText(null);
            }
        });
        _pass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _pass.setText(null);
            }
        });
        _access.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _access.setText(null);
            }
        });


        _loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_access.getText().toString().equals(""))
                {
                    Error("Accesscode cannot be empty");
                }
                if(_user.getText().toString().equals(""))
                {
                    Error("Username cannot be empty");
                }
                if(_pass.getText().toString().equals(""))
                {
                    Error("Password cannot be empty");
                }
                else
                {
                    pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
                    wl.acquire();
                    progressDialog = ProgressDialog.show(Full_Login.this,
                            "Logging In", "Please Wait...");
                    new DoLogin2InBackground().execute();
                }


            }
        });

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
                        _user.getText().toString().trim(),
                        _pass.getText().toString().trim());
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
                wl.release();
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(),Hub.class));
            }
            else
            {
                wl.release();
                progressDialog.dismiss();
                Error("Login Error. Please try Again");
            }

        }
    }

    void Error(String message)
    {
        // Locate the TextView
        new AlertDialog.Builder(this)
                .setTitle("Error!!!")
                .setMessage(message)
                .setNegativeButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                    }
                }).show();
    }
}
