package gh.com.zenithbank.ibank.Notification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Robby on 6/20/2014.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}