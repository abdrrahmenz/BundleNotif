package dev.abman.bundlenotification;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtSender, edtMessage;
    Button btnKirim;

    private final static String GROUP_KEY_EMAILS = "group_key_emails";
    private int idNotif = 0;
    private int maxNotif = 2;
    List<NotificationItem> stactNotif = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSender = (EditText) findViewById(R.id.edt_sender);
        edtMessage = (EditText) findViewById(R.id.edt_message);
        btnKirim = (Button) findViewById(R.id.btn_kirim);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sender = edtSender.getText().toString();
                String message = edtMessage.getText().toString();

                if (sender.isEmpty() || message.isEmpty()){
                    Toast.makeText(MainActivity.this, "Data harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    NotificationItem notificationItem = new NotificationItem(idNotif, sender, message);
                    stactNotif.add(new NotificationItem(idNotif, sender, message));
                    sendNotif();
                    idNotif++;

                    edtSender.setText("");
                    edtMessage.setText("");

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
    }

    private void sendNotif() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_white_24dp);

        Notification notification = null;
        if (idNotif < maxNotif){
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("New email from "+stactNotif.get(idNotif).getSender())
                    .setContentText(stactNotif.get(idNotif).getMessage())
                    .setSmallIcon(R.drawable.ic_mail_white_24dp)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setAutoCancel(true)
                    .build();
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine("New Email from "+stactNotif.get(idNotif).getSender())
                    .addLine("New Email from "+stactNotif.get(idNotif-1).getSender())
                    .setBigContentTitle(idNotif + " new emails")
                    .setSummaryText("mail@dicoding");

            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(idNotif + "new emails")
                    .setContentText("mail@dicoding")
                    .setSmallIcon(R.drawable.ic_mail_white_24dp)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true)
                    .build();
        }

        managerCompat.notify(idNotif, notification);

    }
}
