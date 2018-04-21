package co.sridhar.tamilbible.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.activity.SearchInfoActivity;
import co.sridhar.tamilbible.activity.VerseListingActivity;
import co.sridhar.tamilbible.adapter.SearchResultAdapter;
import co.sridhar.tamilbible.adapter.interfaces.VerseAdapterListener;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.model.Verse;
import co.sridhar.tamilbible.service.FavouritesService;
import co.sridhar.tamilbible.service.VerseService;
import co.sridhar.tamilbible.settings.AppPreferences;

public class SearchFragment extends Fragment implements VerseAdapterListener {

    private TextView searchMsg;
    private Button searchButton;
    private AutoCompleteTextView searchAutoText;

    private RecyclerView recyclerView;
    private SearchResultAdapter mAdapter;

    private SharedPreferences settings;
    private Set<String> history;

    private VerseService mVerseService;

    public static final String PREFS_NAME = "PingBusPrefs";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";

    private void setAutoCompleteSource() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
        searchAutoText.setAdapter(adapter);
    }

    private void addSearchInput(String input) {
        if (!history.contains(input)) {
            history.add(input);
            setAutoCompleteSource();
            savePrefs();
        }
    }

    @Override
    public void unFavourite(int position) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_info) {
            Intent intent = new Intent(getActivity(), SearchInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToChapter(int position) {
        Verse verse = mAdapter.getResultByPosition(position);
        Toast.makeText(getContext(), "Navigating .. ", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), VerseListingActivity.class);
        intent.putExtra("chapter_id", verse.getChapterId());
        intent.putExtra("book_id", verse.getBookId());
        intent.putExtra("search_verse_id", verse.getVerseId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void savePrefs() {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (history.size() > 3) {
            List<String> list = new ArrayList<>(history);
            Set<String> subSet = new LinkedHashSet<>(list.subList(0, Math.min(list.size(), 3)));
            editor.putStringSet(PREFS_SEARCH_HISTORY, subSet).commit();
        } else {
            editor.putStringSet(PREFS_SEARCH_HISTORY, history).commit();
        }

    }

    @Override
    public void addToNotes(int position) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Search");
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        setHasOptionsMenu(true);

        mVerseService = new VerseService(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_view);
        searchMsg = (TextView) view.findViewById(R.id.search_msg);

        searchButton = (Button) view.findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        history = settings.getStringSet(PREFS_SEARCH_HISTORY, new HashSet<String>());

        searchAutoText = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView2);
        searchAutoText.setThreshold(0);//will start working from first character
        searchAutoText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        setHint();

        searchAutoText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchAutoText.showDropDown();
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        searchAutoText.addTextChangedListener(new TextWatcher() {
            boolean isOnTextChanged = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isOnTextChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("isOnTextChanged " + isOnTextChanged);
                if (s.toString().trim().length() == 0) {
                    //searchAutoText.showDropDown();
                }
            }
        });

        setAutoCompleteSource();

        return view;
    }

    private void setHint() {
        String lang = AppPreferences.getInstance().getDefaultLanguage(getContext());
        switch (lang) {
            case "tamil":
                searchAutoText.setHint("Search in Tamil Bible");
                break;
            case "english":
                searchAutoText.setHint("Search in English bible");
                break;
            case "hindi":
                searchAutoText.setHint("Search in Hindi bible");
                break;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void performSearch() {
        searchButton.setText("Searching...");
        searchMsg.setText("");
        recyclerView.setVisibility(View.INVISIBLE);

        final String searchTerm = searchAutoText.getText().toString().replace('\'', ' ').trim();

        addSearchInput(searchTerm);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                searchButton.setText("Search");
                if (searchTerm.trim().isEmpty()) {
                    int unicode = 0x1F61D;
                    searchMsg.setText("Please enter a word to search " + new String(Character.toChars(unicode)));
                    return;
                }

                List<Verse> searchResults = mVerseService.getSearchResultsByPhrase(searchTerm);
                if (searchResults.isEmpty()) {
                    int unicode = 0x1F616;
                    searchMsg.setText("Looks like no verse contains '" + searchTerm + "' in it. Try something else? " + new String(Character.toChars(unicode)));
                } else {
                    int unicode = 0x1F60A;
                    searchMsg.setText(searchResults.size() + " verses found " + new String(Character.toChars(unicode)));
                }

                mAdapter = new SearchResultAdapter(getContext(), searchResults, SearchFragment.this, searchTerm);
                mAdapter.setMode(Attributes.Mode.Single);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                        LinearLayoutManager.VERTICAL);
                dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider_bg));

                recyclerView.addItemDecoration(dividerItemDecoration);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    @Override
    public void onIconClicked(int position) {

    }

    @Override
    public void onRowClicked(int position) {

    }

    @Override
    public void onRowLongClicked(int position) {

    }

    @Override
    public boolean onShareClicked(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String shareBodyText = mAdapter.getItembyPosition(position);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        return true;
    }

    @Override
    public boolean onFavouriteClicked(int position) {
        final DatabaseHelper dbh = new DatabaseHelper(getContext());
        Favourite favourite = mAdapter.getVerseForFavByPosition(position);
        boolean isSuccess = FavouritesService.add(dbh, favourite);
        mVerseService.favourite(favourite.getVerse());
        if (isSuccess) {
            Toast.makeText(getContext(), "Verse favourited", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
