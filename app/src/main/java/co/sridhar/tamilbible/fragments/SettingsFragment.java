package co.sridhar.tamilbible.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.service.LanguageService;
import co.sridhar.tamilbible.settings.AppPreferences;
import co.sridhar.tamilbible.utils.MyConstants;

public class SettingsFragment extends Fragment {

    private Spinner mPrimaryLanguageDropDown;
    private TextView mPrimaryMsg;

    private LanguageService mLanguageService;
    private ArrayList<Language> installedLanguages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Settings");

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        mLanguageService = new LanguageService(getContext());

        mPrimaryMsg = (TextView) view.findViewById(R.id.primary_lang_msg);

        installedLanguages = mLanguageService.getInstalledLanguages();

        mPrimaryLanguageDropDown = (Spinner) view.findViewById(R.id.primary_language_choice);
        mPrimaryLanguageDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) mPrimaryLanguageDropDown.getSelectedView();
                if (textView == null) {
                    return;
                }
                String result = textView.getText().toString();

                Language selectedLang = null;
                for (Language language : installedLanguages) {
                    if (result.equalsIgnoreCase(language.getTitle())) {
                        selectedLang = language;
                        break;
                    }
                }
                if (selectedLang != null) {
                    AppPreferences.getInstance().saveDefaultLanguage(getContext(), selectedLang.getSuffix());
                }
                MyConstants.getInstance(getContext()).refresh(getContext());
                mPrimaryLanguageDropDown.setSelection(getIndex(AppPreferences.getInstance().getDefaultLanguage(getContext())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LinearLayout languageLayout = (LinearLayout) view.findViewById(R.id.lang_layout);
        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, new LanguageFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        refreshData();
        mPrimaryLanguageDropDown.setSelection(getIndex(AppPreferences.getInstance().getDefaultLanguage(getContext())));
        
        return view;
    }

    private void refreshData() {
        final ArrayList<String> primaryLanguages = new ArrayList<>();
        for (Language language : mLanguageService.getInstalledLanguages()) {
            primaryLanguages.add(language.getTitle());
        }

        mPrimaryMsg.setText(primaryLanguages.size() + " installed languages available");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, primaryLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPrimaryLanguageDropDown.setAdapter(adapter);
    }

    private int getIndex(String value) {
        int index = 0;
        for (int i = 0; i < mPrimaryLanguageDropDown.getCount(); i++) {
            if (mPrimaryLanguageDropDown.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }

}