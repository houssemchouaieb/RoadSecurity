package com.example.houssem.adminroadsecurity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button map,refresh;
    EditText number;
    private FirebaseFirestore db;
     List<Accident> productList;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCritical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map=(Button)findViewById(R.id.map);
        map.setOnClickListener(this);
        refresh=(Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        productList = new ArrayList<>();
        number=(EditText)findViewById(R.id.number);
        mDatabase=FirebaseDatabase.getInstance();
        mReferenceCritical=mDatabase.getReference();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.map){
            startActivity(new Intent(MainActivity.this,MapsActivity.class));
        }
        else if(v.getId()==R.id.refresh){
            db = FirebaseFirestore.getInstance();
            db.collection("Accidents").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (!queryDocumentSnapshots.isEmpty()) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                for (DocumentSnapshot d : list) {

                                    Accident p = d.toObject(Accident.class);
                                    Log.e("score",score(p)+"");
                                    productList.add(p);

                                }
                            }
                            mReferenceCritical.setValue(null);
                            if(TextUtils.isEmpty(number.getText().toString())){
                                number.setError("Veuillez remplir ce champ");
                                number.requestFocus();
                            }
                            else if(Integer.parseInt(number.getText().toString())>productList.size()){
                                number.setError("Vous devez choisir un numbre infrieur a "+productList.size());
                                number.requestFocus();
                            }
                            else{
                                for(int i=0;i<Integer.parseInt(number.getText().toString());i++){
                                    int max=0;
                                    for(int j=0;j<productList.size();j++){
                                        if(score(productList.get(j))>score(productList.get(max))){
                                            max=j;
                                        }
                                    }
                                    mReferenceCritical.child("zone"+i).child("score").setValue(score(productList.get(max)));
                                    mReferenceCritical.child("zone"+i).child("latitude").setValue(productList.get(max).getLati());
                                    mReferenceCritical.child("zone"+i).child("longitude").setValue(productList.get(max).getLongi());
                                    productList.remove(max);
                                }
                            }
                        }
                    });
        }
    }

    public int score(Accident a){
        int res=0;
        res+=Integer.parseInt(a.getNb_inj());
        res+=Integer.parseInt(a.getNb_mort())*2;
        int h=a.getTime().indexOf(":")-1;
        String aux=a.getTime().substring(0,h);
        int h_act=Calendar.getInstance().getTime().getHours();
        if(Math.abs(Integer.parseInt(aux)-h_act)<1){
            res+=3;
        }
        else if(Math.abs(Integer.parseInt(aux)-h_act)<2){
            res+=2;
        }
        else if(Math.abs(Integer.parseInt(aux)-h_act)<3){
            res+=1;
        }
        return res;
    }
}
