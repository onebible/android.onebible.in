package co.sridhar.tamilbible.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.ViewPagerAdapter;

public class LanguageFragment extends Fragment {

    private TabLayout sectionTabs;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Languages");
        View view = inflater.inflate(R.layout.language_fragment, container, false);

        sectionTabs = (TabLayout) view.findViewById(R.id.language_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.lang_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFrag(new InstalledLanguageFragment(), "Installed");
        adapter.addFrag(new AvailableLanguageFragment(), "Available");

        mViewPager.setAdapter(adapter);
        sectionTabs.setupWithViewPager(mViewPager);

        return view;
    }

}