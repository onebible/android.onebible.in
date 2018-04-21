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
import co.sridhar.tamilbible.utils.MyConstants;

public class SectionListingFragment extends Fragment {

    private TabLayout sectionTabs;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(MyConstants.getInstance(getContext()).getHeader());
        View view = inflater.inflate(R.layout.section_listing, container, false);

        sectionTabs = (TabLayout) view.findViewById(R.id.section_tabs);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(null);
        sectionTabs.setupWithViewPager(mViewPager);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFrag(new OldTestamentListingFragment(), MyConstants.getInstance(getContext()).getOldTestamentTitle());
        adapter.addFrag(new NewTestamentListingFragment(), MyConstants.getInstance(getContext()).getNewTestamentTitle());

        mViewPager.setAdapter(adapter);
        sectionTabs.setupWithViewPager(mViewPager);

        return view;
    }

}