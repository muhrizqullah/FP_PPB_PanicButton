package com.muhrizqullah.listcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.SmsManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtLat, txtLon;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;
    private ArrayList<Contact> contacts;
    private FloatingActionButton btnAdd;
    private Button btnPanic;
    private RecyclerView contactRv;
    private ContactRepository contactRepository;
    private ContactAdapter contactAdapter;
    private LocationManager lm;
    private LocationListener ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new locationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 200, ll);

        this.contactRepository = new ContactRepository(this);

        this.btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        this.btnAdd.setOnClickListener(this);

        this.btnPanic = (Button) findViewById(R.id.btnPanic);
        this.btnPanic.setOnClickListener(this);

        this.contactRv = (RecyclerView) findViewById(R.id.contactRv);
        this.contactRv.setHasFixedSize(true);

        loadData();
    }

    private void loadData() {
        this.contactAdapter = new ContactAdapter(this, contactRepository.getAll());
        this.contactRv.setAdapter(this.contactAdapter);
    }

    private void sendMessage() {
        this.contacts = contactRepository.getAll();
        for (int i = 0; i < contacts.size(); i++){
            String phone = contacts.get(i).getPhone();
            String lat = txtLat.getText().toString();
            String lon = txtLon.getText().toString();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, "EMERGENCY, NEED HELP AT THIS LOCATION https://maps.google.com/maps?q=" + lat + "," + lon, null, null);
        }
        Toast.makeText(MainActivity.this, contacts.size() + " Message Sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                showAddContactDialog(this);
                break;
            case R.id.btnPanic:
                sendMessage();
                break;
        }
    }

    private void searchContact(String s) {
        contactAdapter = new ContactAdapter(this, this.contactRepository.getByName(s));
        this.contactRv.setAdapter(contactAdapter);
    }

    private void showAddContactDialog(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_add_contact, null);

        EditText alertDialogAddContactNameEt = view.findViewById(R.id.alertDialogAddContactNameEt);
        EditText alertDialogAddContactPhoneEt = view.findViewById(R.id.alertDialogAddContactPhoneEt);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();

        Button alertDialogAddContactSubmitBtn = view.findViewById(R.id.alertDialogAddContactSubmitBtn);
        alertDialogAddContactSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = alertDialogAddContactNameEt.getText().toString();
                String phone = alertDialogAddContactPhoneEt.getText().toString();
                contactRepository.insert(new Contact("", name, phone));
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Successfully added new user", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });
    }

    private class locationListener implements LocationListener{
        @Override
        public void onLocationChanged(@NonNull Location location) {
            txtLat = findViewById(R.id.txtLat);
            txtLon = findViewById(R.id.txtLon);
            txtLat.setText(String.valueOf(location.getLatitude()));
            txtLon.setText(String.valueOf(location.getLongitude()));

            Toast.makeText(getBaseContext(),"GPS capture", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }
}