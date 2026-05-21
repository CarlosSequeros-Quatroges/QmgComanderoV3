package es.quatroges.qgestpv_v3.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class NfcReceiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nfc_receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( this.getIntent().getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Tag tag = this.getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

            String uid= ClaseUtils.ByteArrayToHexString(tag.getId());
            ClaseNFC.setTagID(uid);
            Intent bcIntent = new Intent();
            bcIntent.setAction( ClaseNFC.NFC_TAG_READED);
            if (ClaseNFC.isNfcReading())
                sendBroadcast(bcIntent);
        }

        Handler h = new Handler( );
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, 1000);

    }



}
