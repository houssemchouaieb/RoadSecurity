package com.example.houssem.roadsecurity;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, View.OnClickListener {

    private GestureDetector gestureDetector;
    EditText time,lat,longi,n_inj,n_mort,temp,wind,cause;
    Button b_time,b_pos,add;
    TimePickerDialog timePickerDialog;
    public LocationManager lManager;
    private String best;
    RadioGroup rg;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gestureDetector = new GestureDetector(this);
        time = (EditText) findViewById(R.id.edit_time);
        b_time = (Button) findViewById(R.id.button_time);
        b_time.setOnClickListener(this);
        time.setOnClickListener(this);
        b_pos=(Button)findViewById(R.id.button_pos);
        lat=(EditText)findViewById(R.id.lat);
        longi=(EditText)findViewById(R.id.longi);
        b_pos.setOnClickListener(this);
        n_inj=(EditText)findViewById(R.id.nbr_inj);
        n_mort=(EditText)findViewById(R.id.nbr_mort);
        temp=(EditText)findViewById(R.id.temperature);
        wind=(EditText)findViewById(R.id.wind);
        cause=(EditText)findViewById(R.id.cause);
        add=(Button)findViewById(R.id.add_acc);
        rg=(RadioGroup)findViewById(R.id.radioGroup);
        add.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.edit_time){
            timePickerDialog=new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    time.setText(hourOfDay+" : "+minute);
                }
            },0,0,false);
            timePickerDialog.show();
        }
        else if(v.getId()==R.id.button_time){
            if(Calendar.getInstance().getTime().getMinutes()>=10){
                time.setText(Calendar.getInstance().getTime().getHours()+" : "+Calendar.getInstance().getTime().getMinutes());
            }
            else{
                time.setText(Calendar.getInstance().getTime().getHours()+" : 0"+Calendar.getInstance().getTime().getMinutes());
            }
        }
        else if(v.getId()==R.id.button_pos){
            lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(true);
            best = lManager.getBestProvider(criteria, true);
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
            lManager.requestLocationUpdates(best, 10, (float) 0.01, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    lat.setText(location.getLatitude()+"");
                    longi.setText(location.getLongitude()+"");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

            });


        }
        else if(v.getId()==R.id.add_acc){
            addAcc();
        }

    }

    private void addAcc() {
        String choix ;
        if(rg.getCheckedRadioButtonId()==R.id.radioButton){
            choix="Snowing";
        }
        else if(rg.getCheckedRadioButtonId()==R.id.radioButton2){
            choix="Raining";
        }
        else if (rg.getCheckedRadioButtonId()==R.id.radioButton3){
            choix="fog";
        }
        else{
            choix="good";
        }
        CollectionReference dbAccidents = db.collection("Accidents");
        Accident accident=new Accident(time.getText().toString(),SharedPrefManager.getInstance(ProfileActivity.this).getUserEmail(),
                cause.getText().toString(),temp.getText().toString(),wind.getText().toString(),n_inj.getText().toString(),n_mort.getText().toString(),
                lat.getText().toString(),longi.getText().toString(),choix);
        dbAccidents.add(accident)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ProfileActivity.this, "Accident Added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY=e1.getY() - e2.getY();
        float diffX=e1.getX() - e2.getX();
        if(Math.abs(diffX)>Math.abs(diffY)){
            if(Math.abs(diffX)>100 && Math.abs(velocityX)>100){
                if(diffX<0){

                    return true;
                }
                else{

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}
