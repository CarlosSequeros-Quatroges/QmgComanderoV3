package es.quatroges.qgestpv_v3;


import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * Created by carlos on 07/07/2015.
 */
public class ActivityFirma extends AppCompatActivity {
    int a = 0;
    private SignaturePad mSignaturePad;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pidefirma);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSignaturePad = findViewById(R.id.signature_pad);
        mSignaturePad.setPenColor(Color.BLACK);
        mSignaturePad.setVelocityFilterWeight((float) 0.9);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

            }
        });


    }



}


