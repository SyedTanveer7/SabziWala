package com.example.vendor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.vendor.Webservices.Models.Order;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.vendor.Adapters.OrdersAdapter.selectedOrder;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String type = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            type = "json";
            showNotification(remoteMessage.getData().get("message"));
        }
        if (remoteMessage.getNotification() != null) {
            type = "message";
            showNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void showNotification(String messagebody) {

        String id = "", message = "", title = "";

        if (type.equals("json")) {


            try {
                JSONObject jsonObject = new JSONObject(messagebody);
                id = jsonObject.getString("id");
                message = jsonObject.getString("message");
                title = jsonObject.getString("title");
                Order order = new Order();
                order.setId(Integer.parseInt(title));
                selectedOrder = order;


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (type.equals("message")) {
            message = messagebody;
        }

        Intent intent = new Intent(this, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Sabzi Wala");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);

        Uri soundURi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setSound(soundURi);
//        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(1000);
        builder.setContentIntent(pendingIntent);



        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }
}
