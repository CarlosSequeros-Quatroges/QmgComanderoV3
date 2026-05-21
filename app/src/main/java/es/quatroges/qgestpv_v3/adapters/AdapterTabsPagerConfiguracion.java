package es.quatroges.qgestpv_v3.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 07/07/2015.
 */
public class AdapterTabsPagerConfiguracion extends FragmentStateAdapter {
    List<Fragment> fragments;
    public void addFragment(Fragment fragment) {
        if ( this.fragments == null) {
            this.fragments = new ArrayList<>();
        }
        this.fragments.add(fragment);
    }

    public Fragment getFragment(int pos){
        if (fragments != null && fragments.size() >= pos) {
            return fragments.get(pos);
        }
        return null;
    }

    public AdapterTabsPagerConfiguracion(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }


}
