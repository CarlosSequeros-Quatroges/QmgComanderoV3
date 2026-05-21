package es.quatroges.qgestpv_v3;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * Created by carlos on 07/07/2015.
 */
public class FragmentConfigInfoTablas extends Fragment {


//    private EstadoVersiones estadoVersiones = new EstadoVersiones (false,null);
  //  private ArrayList<dbVersiones> Versiones;
  //  private ListView lvVersiones;
  //  AdapterInfoVersion adaptador;
    private static Context context = null;

    public static FragmentConfigInfoTablas newInstance() {
        FragmentConfigInfoTablas fragment= new FragmentConfigInfoTablas();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getBaseContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_infotablas, container, false);

        //lvVersiones = rootView.findViewById(R.id.lvVersiones);

        //RellenaLista();
        return rootView;
    }


    private void RellenaLista(){
/*
        LinkedHashMap<String,EstadoVersiones.Datos> lista = estadoVersiones.listaVersiones();

        Versiones= new ArrayList<dbVersiones>();

        for (String key : lista.keySet())
        {
            EstadoVersiones.Datos val = lista.get(key);
            boolean insertado = false;

            for (int pos = 0; pos < Versiones.size(); pos++) {

                if (Versiones.get(pos).getTABLA().compareToIgnoreCase(key) > 0) {
                    Versiones.add(pos, new dbVersiones(key, String.valueOf(val.EstadoAnterior), String.valueOf(val.Registros)));
                    insertado = true;
                    break;
                }
            }

            if (insertado == false) Versiones.add(new dbVersiones( key, String.valueOf(val.EstadoAnterior),String.valueOf(val.Registros)));
            android.util.Log.e("activityInfo","Añade version." +key);
        }

        adaptador = new AdapterInfoVersion(getActivity(),Versiones);
        lvVersiones.setAdapter(adaptador);
*/
    }

}
