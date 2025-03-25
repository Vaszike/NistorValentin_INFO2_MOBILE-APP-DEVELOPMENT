package com.example.safealert;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView countdownTextView;
    private Button cancelSOSButton;
    private CountDownTimer countDownTimer;
    private boolean sosActivated = false;
    private static final String EMERGENCY_PHONE_NUMBER = "tel:0753402371";
    private static final int CALL_PHONE_REQUEST_CODE = 1;
    private PowerManager powerManager;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "battery_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifica permisiunile necesare
        checkPermissions();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        countdownTextView = findViewById(R.id.countdownTextView);
        cancelSOSButton = findViewById(R.id.cancelSOSButton);
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        cancelSOSButton.setVisibility(View.GONE);
        countdownTextView.setVisibility(View.GONE);

        findViewById(R.id.alertButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sosActivated) {
                    activateSOS();
                }
            }
        });

        cancelSOSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSOS();
            }
        });

        createNotificationChannel();

        registerBatteryReceiver();
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.POST_NOTIFICATIONS
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }


    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Battery Alerts";
            String description = "Channel for battery level notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void activateSOS() {
        sosActivated = true;
        showCancelButtonAndCountdown();
        getLocationAndSendSMS();
    }

    private void showCancelButtonAndCountdown() {
        cancelSOSButton.setVisibility(View.VISIBLE);
        countdownTextView.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                countdownTextView.setText("0");
                if (sosActivated) {
                    checkCallPermissionAndMakeCall();
                }
            }
        };
        countDownTimer.start();
    }

    private void cancelSOS() {
        sosActivated = false;
        countDownTimer.cancel();
        cancelSOSButton.setVisibility(View.GONE);
        countdownTextView.setVisibility(View.GONE);
        Toast.makeText(this, "Alerta SOS a fost anulata!", Toast.LENGTH_SHORT).show();
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void getLocationAndSendSMS() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            String message = "URGENT: Am nevoie de ajutor! Locatia mea este: https://maps.google.com/?q=" + latitude + "," + longitude;

                            String[] contacts = {"0753402371"};

                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                for (String contact : contacts) {
                                    smsManager.sendTextMessage(contact, null, message, null, null);
                                }
                                Toast.makeText(MainActivity.this, "Mesajul de urgenta a fost trimis!", Toast.LENGTH_SHORT).show();
                            } catch (SecurityException e) {
                                Toast.makeText(MainActivity.this, "Eroare la trimiterea SMS-ului: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Nu am putut obtine locatia.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkCallPermissionAndMakeCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            makeEmergencyCall();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
        }
    }

    private void makeEmergencyCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(EMERGENCY_PHONE_NUMBER));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Toast.makeText(this, "Toate permisiunile au fost acordate!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nu toate permisiunile au fost acordate. Funcționalitatea aplicației poate fi afectată.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (!sosActivated) {
                activateSOS();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerBatteryReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, filter);
    }

    private BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            if (level <= 100) {
                if (level <= 2) {
                    getLocationAndSendBatteryLowSMS(true); // Critic (<=2%)
                } else if (level <= 10) {
                    showBatteryLowNotification();
                    getLocationAndSendBatteryLowSMS(false); // Avertisment (<=10%)
                }
            }
        }
    };

    private void showBatteryLowNotification() {
        Intent intent = new Intent(android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert_button)
                .setContentTitle("Baterie scazuta")
                .setContentText("Nivelul bateriei este sub 10%. Acceseaza economizorul de baterie.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_alert_button, "Setari", pendingIntent)
                .build();

        notificationManager.notify(1, notification);
    }

    private void getLocationAndSendBatteryLowSMS(boolean isCritical) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            String message = "URGENT: Nivelul bateriei este scazut! Locatia mea este: https://maps.google.com/?q=" + latitude + "," + longitude;
                            if (isCritical) {
                                message += " Telefonul se va inchide!!!";
                            }

                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("0753402371", null, message, null, null);
                                Toast.makeText(MainActivity.this, "Mesajul de urgenta cu baterie scazuta a fost trimis!", Toast.LENGTH_SHORT).show();
                            } catch (SecurityException e) {
                                Toast.makeText(MainActivity.this, "Eroare la trimiterea SMS-ului: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
