package co.sridhar.tamilbible.fragments;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.VerseAdapter;
import co.sridhar.tamilbible.adapter.interfaces.VerseAdapterListener;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.model.ThreadBookmark;
import co.sridhar.tamilbible.model.Verse;
import co.sridhar.tamilbible.service.FavouritesService;
import co.sridhar.tamilbible.service.ThreadBookmarkService;
import co.sridhar.tamilbible.service.VerseService;
import co.sridhar.tamilbible.ui.dialog.FontSettings;
import co.sridhar.tamilbible.utils.MyConstants;

public class VerseListingFragment extends Fragment implements VerseAdapterListener {

    private RecyclerView recyclerView;
    private VerseAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    private String title;
    private String bookId;
    private String chapterId;
    private List<Verse> verses;

    private VerseService mVerseService;

    private String getBookName(String bookId) {
        return MyConstants.getInstance(getContext()).getBookNameById(bookId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mVerseService = new VerseService(getContext());
        actionModeCallback = new ActionModeCallback();

        this.bookId = getArguments().getString("book_id");
        this.chapterId = this.getArguments().getString("chapter_id");
        String rawHighlightId = this.getArguments().getString("search_verse_id");

        View view = inflater.inflate(R.layout.verse_listing, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_bg));

        recyclerView.addItemDecoration(dividerItemDecoration);
        Button nextChapterBtn = (Button) view.findViewById(R.id.next_button);
        nextChapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastVerse = verses.get(verses.size() - 1).getVerseId();
                int rowId = mVerseService.getRowId(bookId, chapterId, lastVerse);
                int nextRowId = computeNextRowId(rowId);
                Verse verse = mVerseService.getByRowId(nextRowId);
                if (verse != null) {
                    loadData(verse.getBookId(), verse.getChapterId(), null);
                    toggleFabs();
                }
            }
        });

        Button previousChapterBtn = (Button) view.findViewById(R.id.previous_button);
        previousChapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastVerse = verses.get(0).getVerseId();
                int rowId = mVerseService.getRowId(bookId, chapterId, lastVerse);
                int previousRowId = computePreviousRowId(rowId);
                Verse verse = mVerseService.getByRowId(previousRowId);
                if (verse != null) {
                    loadData(verse.getBookId(), verse.getChapterId(), null);
                    toggleFabs();
                }
            }
        });

        loadData(this.bookId, this.chapterId, rawHighlightId);

        return view;
    }

    private int computeNextRowId(int rowId) {
        int lastRowIdInDb = 31102;
        if (rowId < lastRowIdInDb) {
            rowId++;
        }
        return rowId;
    }

    private int computePreviousRowId(int rowId) {
        int firstRowIdInDb = 1;
        if (rowId != firstRowIdInDb) {
            rowId--;
        }
        return rowId;
    }

    private void loadData(String bookId, String chapterId, String rawHighlightId) {
        this.bookId = bookId;
        this.chapterId = chapterId;

        this.verses = mVerseService.getVerses(bookId, chapterId);

        Integer searchVerseId = getSearchVerseId(rawHighlightId);

        mAdapter = new VerseAdapter(getContext(), verses, this, searchVerseId);
        mAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        mAdapter.setMode(Attributes.Mode.Single);

        setTitle(bookId, chapterId);
        scrollToPosition(searchVerseId);
    }

    private void toggleFabs() {
        String bookId = this.verses.get(0).getBookId();
        String chapterId = this.verses.get(0).getChapterId();

        boolean isFirstChapterOfBible = bookId.equals("0") && chapterId.equals("1");
        if (isFirstChapterOfBible) {
            Toast.makeText(getContext(), "You're on the first chapter of the bible", Toast.LENGTH_SHORT).show();
        }

        boolean isLastChapterOfBible = bookId.equals("65") && chapterId.equals("22");
        if (isLastChapterOfBible) {
            Toast.makeText(getContext(), "You're on the last chapter of the bible", Toast.LENGTH_SHORT).show();
        }
        if (actionMode != null) {
            actionMode.finish();
        }

    }

    private void scrollToPosition(Integer searchVerseId) {
        if (searchVerseId != null) {
            recyclerView.smoothScrollToPosition(searchVerseId);
        }
    }

    private Integer getSearchVerseId(String rawHighlightId) {
        Integer searchVerseId = null;
        if (rawHighlightId != null) {
            searchVerseId = Integer.parseInt(rawHighlightId) - 1;
        }
        return searchVerseId;
    }

    private void setTitle(String bookId, String chapterId) {
        this.title = getBookName(bookId) + " " + chapterId;
        getActivity().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_verse_settings:
                FontSettings.buildDialog(getContext(), new FontSettings.Callback() {
                    @Override
                    public void onBeforeDismiss() {
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = getActivity().startActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        MenuItem yellowThread = menu.findItem(R.id.yellow_thread);
        yellowThread.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final DatabaseHelper dbh = new DatabaseHelper(getContext());
                ThreadBookmark rt = new ThreadBookmark(1, title, bookId, chapterId);
                if (ThreadBookmarkService.edit(dbh, rt)) {
                    Toast.makeText(getContext(), "Successfully moved yellow thread", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        MenuItem greyThread = menu.findItem(R.id.grey_thread);
        greyThread.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final DatabaseHelper dbh = new DatabaseHelper(getContext());
                ThreadBookmark rt = new ThreadBookmark(2, title, bookId, chapterId);
                if (ThreadBookmarkService.edit(dbh, rt)) {
                    Toast.makeText(getContext(), "Successfully moved grey thread", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_verse_listing, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRowClicked(int position) {
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    @Override
    public boolean onShareClicked(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String shareBodyText = mAdapter.getSelectedForShareByPosition(position);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        return true;
    }

    @Override
    public void addToNotes(int position) {
        Favourite favourite = mAdapter.getVerseForFavByPosition(position);

        FragmentManager fm = getActivity().getFragmentManager();

        IntegrateNoteFragment dialog = new IntegrateNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", favourite.toString());
        dialog.setArguments(bundle);

        dialog.show(fm, "intergrate notes");
    }

    @Override
    public boolean onFavouriteClicked(int position) {
        final DatabaseHelper dbh = new DatabaseHelper(getContext());
        Verse verse = mAdapter.getByPosition(position);

        if (verse.isFavourited()) {
            Toast.makeText(getContext(), "Click on the heart icon below the verse number to unfavourite", Toast.LENGTH_SHORT).show();
            return true;
        }

        Favourite fav = mAdapter.getVerseForFavByPosition(position);
        fav.setVerseDetails(verse);

        FavouritesService.add(dbh, fav);
        mVerseService.favourite(verse);

        mAdapter.refreshFavourite(position);
        Toast.makeText(getContext(), "Verse favourited", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public void unFavourite(int position) {
        final DatabaseHelper dbh = new DatabaseHelper(getContext());

        Verse verse = mAdapter.getByPosition(position);

        Favourite fav = mAdapter.getVerseForFavByPosition(position);
        fav.setVerseDetails(verse);

        mVerseService.unFavourite(fav);
        FavouritesService.remove(dbh, verse);
        mAdapter.refreshUnFavourite(position);

        Toast.makeText(getContext(), "Unfavourited", Toast.LENGTH_SHORT).show();
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = getActivity().startActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            MenuItem item = menu.findItem(R.id.menu_item_share);

            ShareActionProvider shareActionProvider = new ShareActionProvider(getContext());
            MenuItemCompat.setActionProvider(item, shareActionProvider);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_item_share:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String shareBodyText = mAdapter.getSelectedForShare();
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    mode.finish();
                    return true;

                case R.id.menu_item_favourite:
                    List<Verse> verses = mAdapter.getSelectedItems();
                    int noOfItems = verses.size();

                    if (noOfItems == 1) {
                        Toast.makeText(getContext(), noOfItems + " verse favourited", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), noOfItems + " verses favourited", Toast.LENGTH_LONG).show();
                    }

                    final DatabaseHelper dbh = new DatabaseHelper(getContext());
                    FavouritesService.add(dbh, mAdapter.getVerseForFav());

                    mode.finish();
                    return true;

                case R.id.menu_item_notes:
                    Favourite favourite = mAdapter.getVerseForNotes();
                    FragmentManager fm = getActivity().getFragmentManager();

                    IntegrateNoteFragment dialog = new IntegrateNoteFragment();
                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

                    Bundle bundle = new Bundle();
                    bundle.putString("content", favourite.toString());
                    dialog.setArguments(bundle);

                    dialog.show(fm, "intergrate notes");
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                }
            });
        }
    }

    @Override
    public void navigateToChapter(int position) {
    }

}
