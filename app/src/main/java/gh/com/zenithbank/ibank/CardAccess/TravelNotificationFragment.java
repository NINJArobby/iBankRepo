package gh.com.zenithbank.ibank.CardAccess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 7/8/2015.
 */
public class TravelNotificationFragment extends Fragment
{
    Spinner _account;
    Spinner destination;

    EditText cardNumber;
    EditText mobileNumber;
    EditText travelDate;
    EditText returnDate;

    Button submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.travel_notiy_frag,container,false);

        _account = (Spinner)v.findViewById(R.id.SpnRequestAccountNumber);
        destination =(Spinner)v.findViewById(R.id.SpnTravelDestination);

        cardNumber =(EditText)v.findViewById(R.id.EdtxtRequestCardNumber);
        mobileNumber = (EditText)v.findViewById(R.id.EdtxtRequestPhoneNumber);
        travelDate = (EditText)v.findViewById(R.id.EdtxtTravelDate);
        returnDate  = (EditText)v.findViewById(R.id.EdtxtRetuenDate);

        submit = (Button)v.findViewById(R.id.BtnCardAccess);
        return v;
    }
}
