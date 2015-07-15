package gh.com.zenithbank.ibank.CardAccess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/8/2015.
 */
public class CardReplacementFragment extends Fragment
{
    CheckBox Visa;
    CheckBox EasyPay;

    Spinner accountSpinner;
    Spinner _reason;

    EditText cardnumber;
    EditText mobilenumber;
    EditText Destination;

    Button MakeRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.card_replacement_frag,container,false);

        try
        {
            Visa = (CheckBox)v.findViewById(R.id.ChkBxNewRequest);
            EasyPay = (CheckBox)v.findViewById(R.id.ChcbxRegeneration);

            accountSpinner = (Spinner)v.findViewById(R.id.SpnRequestAccountNumber);
            _reason = (Spinner)v.findViewById(R.id.SpnReplacementReason);

            cardnumber = (EditText)v.findViewById(R.id.EdtxtRequestCardNumber);
            mobilenumber = (EditText)v.findViewById(R.id.EdtxtRequestPhoneNumber);
            Destination = (EditText)v.findViewById(R.id.EdtxtRequestEmail);

            MakeRequest = (Button)v.findViewById(R.id.BtnCardAccess);



        }
        catch (Exception ex)
        {
            ex.getMessage();
        }


        return v;
    }
}
