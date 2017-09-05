package br.com.jordan.whatsapp.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jordan.whatsapp.Adapter.ViewPagerAdapter;
import br.com.jordan.whatsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {


    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        return view;
    }

}
