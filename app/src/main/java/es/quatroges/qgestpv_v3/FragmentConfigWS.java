package es.quatroges.qgestpv_v3;


import android.app.Activity;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * Created by carlos on 07/07/2015.
 */
public class FragmentConfigWS extends Fragment {
    private static final String TAG = "FragmentConfigWs";

    private static EditText etUri,etApi,etCon, etRW1, etRW2, etRW3, etRW4,etRW5;
    private static TextView tvUUID;


    private static Context context = null;

    private static boolean iniciado;
    public interface ActivityCommunicator{
        public Bundle wsGetDatos();
    }

    ActivityCommunicator activityCommunicator;


    public Bundle MandaDatos() {
        if (! iniciado)  {
            return null;
        }
        Bundle bundle = new Bundle();

        bundle.putString("url", etUri.getText().toString().trim());
        bundle.putString("api", etApi.getText().toString().trim());

        bundle.putInt("tocon", Integer.valueOf(etCon.getText().toString()) );
        bundle.putInt("torw1", Integer.valueOf(etRW1.getText().toString()) );
        bundle.putInt("torw2", Integer.valueOf(etRW2.getText().toString()) );
        bundle.putInt("torw3", Integer.valueOf(etRW3.getText().toString()) );
        bundle.putInt("torw4", Integer.valueOf(etRW4.getText().toString()) );
        bundle.putInt("torw5", Integer.valueOf(etRW5.getText().toString()) );

        return bundle;
    }

    public static FragmentConfigWS newInstance() {
        FragmentConfigWS fragment= new FragmentConfigWS();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
        context = activity.getBaseContext();
        iniciado = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_config_ws, container, false);
        etUri=rootView.findViewById(R.id.etURL);
        etApi =rootView.findViewById(R.id.etApi);
        tvUUID = rootView.findViewById(R.id.tvUUID);

        etCon =rootView.findViewById(R.id.etConTO);
        etRW1 =rootView.findViewById(R.id.etRW1);
        etRW2 =rootView.findViewById(R.id.etRW2);
        etRW3 =rootView.findViewById(R.id.etRW3);
        etRW4 =rootView.findViewById(R.id.etRW4);
        etRW5 =rootView.findViewById(R.id.etRW5);


        Bundle bdatos = activityCommunicator.wsGetDatos();

        etUri.setText(bdatos.getString("url"));
        etApi.setText(String.valueOf(bdatos.getString("api")));

        etCon.setText(String.valueOf(bdatos.getInt("tocon")));
        etRW1.setText(String.valueOf(bdatos.getInt("torw1")));
        etRW2.setText(String.valueOf(bdatos.getInt("torw2")));
        etRW3.setText(String.valueOf(bdatos.getInt("torw3")));
        etRW4.setText(String.valueOf(bdatos.getInt("torw4")));
        etRW5.setText(String.valueOf(bdatos.getInt("torw5")));

        tvUUID.setText(bdatos.getString("deviceid"));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        iniciado = true;
        return rootView;
    }

    public Point getScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            int x =  windowMetrics.getBounds().width() - insets.left - insets.right;
            int y =  windowMetrics.getBounds().height() - insets.top - insets.bottom;
            return  new Point(x,y);

        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int x =  displayMetrics.widthPixels;
            int y =  displayMetrics.heightPixels;
            return new Point(x,y);

        }
    }

}
