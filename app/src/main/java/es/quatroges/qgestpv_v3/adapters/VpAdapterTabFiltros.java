package es.quatroges.qgestpv_v3.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import es.quatroges.qgestpv_v3.FragmentDialogFiltroAlergenos;
import es.quatroges.qgestpv_v3.FragmentDialogFiltroEtiquetas;
import es.quatroges.qgestpv_v3.FragmentDialogFiltroOrdenPlatos;


public class VpAdapterTabFiltros extends FragmentStatePagerAdapter {

    public VpAdapterTabFiltros(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return FragmentDialogFiltroEtiquetas.newInstance("Etiquetas");
        if (position == 1)
            return FragmentDialogFiltroOrdenPlatos.newInstance("Orden");
        else
            return FragmentDialogFiltroAlergenos.newInstance("Alergenos");
    }

    @Override
    public int getCount() {
        return 3;
    }

}