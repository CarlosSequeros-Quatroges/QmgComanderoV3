package es.quatroges.qgestpv_v3.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class ClaseNFC  implements NfcAdapter.ReaderCallback {

    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };


    private static Context context;
    private static Activity activity;
    private static NfcAdapter nfcAdapter;
    private static PendingIntent nfcPendingIntent;
    private static  boolean nfcReading;
    private static String tagID;

    public static int NO_DISPONIBLE = 0;
    public static int DESACTIVADO = 1;
    public static int ACTIVO = 2;
    public static final String NFC_TAG_READED="es.quatroges.qgestpv_v1.nfc_action.TAG_READED";

    public static NfcAdapter getNfcAdapter() {
        return nfcAdapter;
    }

    public static void setNfcAdapter(NfcAdapter nfcAdapter) {
        ClaseNFC.nfcAdapter = nfcAdapter;
    }

    public static boolean isNfcReading() {
        return nfcReading;
    }

    public void setNfcReading(boolean nfcReading) {
        ClaseNFC.nfcReading = nfcReading;
        if (nfcReading)
            tagID = "";
    }

    public static void setTagID(String tagID) {
        ClaseNFC.tagID = tagID;
    }

    public String getTagID() {
        return tagID;
    }
    public void clearTagID(){tagID = "";};

    public ClaseNFC(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        nfcReading = false;
        tagID = "";

    }

    public int hasNFC(){
        try {
            if (nfcAdapter == null){
                return NO_DISPONIBLE;
            }
            else if (! nfcAdapter.isEnabled()) {
                return DESACTIVADO;
            }
            else {
                return ACTIVO;
            }

        }
        catch (Exception e){
            return 1;
        }

    }

    public boolean hasMifareClassic() {
        return context.getPackageManager().hasSystemFeature("com.npx.mifare");
    }

    public void enableRead(){
        if (nfcAdapter != null) {
            /*
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                nfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, NfcReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
            }
            else {
                nfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, NfcReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
            }

            IntentFilter[] filters = new IntentFilter[] {
                    new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                    new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                    new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
            };



            nfcAdapter.enableForegroundDispatch(activity,nfcPendingIntent,filters,null);
            */

            tagID = "";

            //pruebas nfc

            nfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
            // creating intent receiver for NFC events:
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            // enabling foreground dispatch for getting intent from NFC event:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);

            nfcAdapter.enableReaderMode(activity,this,NfcAdapter.FLAG_READER_NFC_A |
                    NfcAdapter.FLAG_READER_NFC_B |
                    NfcAdapter.FLAG_READER_NFC_F |
                    NfcAdapter.FLAG_READER_NFC_V |
                    NfcAdapter.FLAG_READER_NFC_BARCODE |
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK ,null        );



        }

    }


    public void disableRead(){
        if (nfcAdapter != null){
            //nfcAdapter.disableForegroundDispatch(activity);

            nfcAdapter.disableReaderMode( activity);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        String uid= ClaseUtils.ByteArrayToHexString(tag.getId());
        ClaseNFC.setTagID(uid);
        Intent bcIntent = new Intent();
        bcIntent.setPackage(context.getPackageName());
        bcIntent.setAction( ClaseNFC.NFC_TAG_READED);
        if (ClaseNFC.isNfcReading()) {
           activity.sendBroadcast(bcIntent);
        }
    }
}
