package es.quatroges.qgestpv_v3.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import es.quatroges.qgestpv_v3.FragmentDialogExtras;
import es.quatroges.qgestpv_v3.FragmentDialogNotas;

public class VpAdapterTabPideExtras extends FragmentStatePagerAdapter {

    public VpAdapterTabPideExtras(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FragmentDialogExtras.newInstance("Extras");
        }
        return FragmentDialogNotas.newInstance("Notas");
    }

    @Override
    public int getCount() {
        return 2;
    }
}
