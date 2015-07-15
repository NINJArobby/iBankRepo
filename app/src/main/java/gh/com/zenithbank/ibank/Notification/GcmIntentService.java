package gh.com.zenithbank.ibank.Notification;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Calendar;
import java.util.Random;
import java.util.StringTokenizer;

import gh.com.zenithbank.ibank.Core.Token;
import gh.com.zenithbank.ibank.Engines.DBEngine;
import gh.com.zenithbank.ibank.Engines.DBUpdate;
import gh.com.zenithbank.ibank.R;

/**
 * Created by Robby on 6/20/2014.
 */
public class GcmIntentService extends IntentService
{
    Context context;
     int NOTIFICATION_ID = randomBox();
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "Zap Push";
    Uri noteSound;
    long[] vibrate = {0, 100, 200, 300};
    String TableName = "NotificationLogsDB";
    SQLiteDatabase MyDb;
    String[] splitStr;
    DBEngine dbEngine;

    //Time time = new Time();
    long time= System.currentTimeMillis();

    public GcmIntentService()
    {
        super("GcmIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        //String msg = intent.getStringExtra("message");
        //time.setToNow();
        Calendar c = Calendar.getInstance();
        NOTIFICATION_ID = c.get(Calendar.SECOND);
        String msg = "ZPrompt";
        String payLoad = "";
        payLoad = intent.getStringExtra("message");
        Intent myIntent2 = new Intent(getApplication().getBaseContext(), DBUpdate.class);
        myIntent2.putExtra("message",payLoad);
        //myIntent2.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);*/

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        noteSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //determine message type
        String delimit = "||";
        StringTokenizer ress = new StringTokenizer(payLoad, delimit);
        splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements())
        {
            splitStr[index++] = ress.nextToken();
        }
        payLoad = splitStr[1];
        myIntent2.putExtra("message",payLoad);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification("Send error: " + extras.toString(), "");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(), "");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e)
                    {

                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }

        if(splitStr[0].equals("TOKEN"))
        {
            Intent _token = new Intent(this,Token.class);
            _token.putExtra("TOKEN",splitStr[1]);
            _token.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(_token);

        }
        if(splitStr[0].equals("ACTIVITY"))
        {
            //startActivity(myIntent2);
            save2DB(payLoad);
            sendNotification("Account Activity",payLoad);
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    private void alertToken(String token)
    {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Alert...")
                .setMessage("Your Token is: "+token)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.logo3)
                .show();
    }

    public static int randomBox() {

        Random rand = new Random();
        int pickedNumber = rand.nextInt(100);
        return pickedNumber;

    }

    private void sendNotification(String msg, String Payload) {
        //**add this line**
        int requestID = (int)time;
        mNotificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        Intent myintent = new Intent(getApplicationContext(), ReceiveActivity.class);

        // **add this line**
        myintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        myintent.putExtra("message", Payload);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                requestID, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setFullScreenIntent(contentIntent, true)
                        .setSmallIcon(R.drawable.zenith)
                        .setContentTitle("Account Activity")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setSound(noteSound)
                        .setVibrate(vibrate)
                        .setAutoCancel(true)
                        .setFullScreenIntent(contentIntent, true);


        mBuilder.setContentIntent(contentIntent);
        // mBuilder.setContentIntent(contentIntent2);
        mNotificationManager.notify(++requestID, mBuilder.build());


    }

    void save2DB(String data)
    {
        try
        {
        //String message = intent.getExtras().getString("message");
        String message = data;
        String delimit = ";";
        StringTokenizer ress = new StringTokenizer(message, delimit);
        String[] splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements()) {

            splitStr[index++] = ress.nextToken();

        }

        dbEngine = new DBEngine();
        dbEngine.Init(context);
        dbEngine.UPDATE_PROMPTS_TABLE(
                splitStr[0],
                splitStr[1],
                splitStr[2],
                splitStr[3],
                splitStr[4],
                splitStr[5],
                splitStr[6]);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}