package com.rahul.calllistener.broadcastlistener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.rahul.calllistener.R;
import com.rahul.calllistener.activity.DashboardActivity;
import com.rahul.calllistener.database.CallEntryDataSource;

/**
 * BroadcastReceiver which listens for incoming calls.
 *
 * Created by rahul on 3/3/16.
 */
public class IncomingCallListener extends BroadcastReceiver {

    private Context mContext;
    private int mNotificationId = 0;
    private CallEntryDataSource mCallEntryDataSource;

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        mCallEntryDataSource = new CallEntryDataSource(context);
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
        telephonyManager.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                mCallEntryDataSource.open();
                mCallEntryDataSource.createCallEntry(incomingNumber);
                displayNotification(mContext, incomingNumber);
                String msg = "Incoming call from "+incomingNumber;
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Show a notification to the user
     */
    private void displayNotification(Context context, String incomingNumber) {
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(context, DashboardActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(mContext.getString(R.string.app_name))
                .setContentText("Incoming call from "+ incomingNumber);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(++mNotificationId, mBuilder.build());
    }
}

