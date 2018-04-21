package co.sridhar.tamilbible.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.LanguageAdapter;
import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.service.LanguageService;
import co.sridhar.tamilbible.ui.dialog.ConfirmDeleteLanguage;

public class InstalledLanguageFragment extends Fragment implements ConfirmDeleteLanguage.Callback {

    private ListView listView;
    private LanguageService mLanguageService;
    private LanguageAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language_view_pager, container, false);
        listView = (ListView) view.findViewById(R.id.lang_list_view);
        mLanguageService = new LanguageService(getContext());
        refresh();
        return view;
    }

    private void refresh() {
        ArrayList<Language> languages = mLanguageService.getInstalledLanguages();
        mAdapter = new LanguageAdapter(getContext(), languages, new LanguageAdapter.Listener() {
            @Override
            public void onDownloadClick(Language language) {
            }

            @Override
            public void onDeleteClick(Language language) {
                ConfirmDeleteLanguage.buildDialog(getContext(), language, InstalledLanguageFragment.this).show();
            }

            @Override
            public void onCancelClick(Language language) {

            }
        });

        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onComplete() {
        refresh();
    }
}
