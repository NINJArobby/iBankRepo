package gh.com.zenithbank.ibank.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/4/2015.
 */
public class Settings extends Activity
{
    CheckBox _easyLogin;
    CheckBox _prompt;
    CheckBox _ATM;
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.settings);

        _easyLogin = (CheckBox)findViewById(R.id.chkBxEasyLogin);
        _prompt = (CheckBox)findViewById(R.id.chkBxPrompt);
        _ATM = (CheckBox)findViewById(R.id.chkBxATM_Locator);

        _easyLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _easyLogin.setChecked(true);
            }
        });

        getSettings();

    }

    void getSettings()
    {

    }
}
