package es.quatroges.qgestpv_v3.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import es.quatroges.qgestpv_v3.FragmentDialogFacturaCabecera;
import es.quatroges.qgestpv_v3.FragmentDialogFacturaLineas;


public class VpAdapterTabFactura extends FragmentStatePagerAdapter {

    public  VpAdapterTabFactura(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return FragmentDialogFacturaLineas.newInstance("Filas");
        else
            return FragmentDialogFacturaCabecera.newInstance("Resumen");
    }

    @Override
    public int getCount() {
        return 2;
    }

}