package co.sridhar.tamilbible.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.activity.VerseListingActivity;
import co.sridhar.tamilbible.adapter.ThreadAdapter;
import co.sridhar.tamilbible.adapter.interfaces.ThreadBookmarkListener;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.ThreadBookmark;
import co.sridhar.tamilbible.service.ThreadBookmarkService;

public class ThreadBookmarkFragment extends Fragment implements ThreadBookmarkListener {

    private ListView readingThreadListView;
    private List<ThreadBookmark> rt;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.thread_bookmark_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reading_thread_info) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Info");
            alert.setMessage("Bookmark thread helps you to bookmark the last read chapter. Tap them to jump to that chapter quickly");

            alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        final DatabaseHelper dbh = new DatabaseHelper(getContext());
        rt = ThreadBookmarkService.getThreads(dbh);

        readingThreadListView.setAdapter(new ThreadAdapter(getContext(), rt, this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Thread Bookmarks");
        View view = inflater.inflate(R.layout.thread_listing_fragment, container, false);

        readingThreadListView = (ListView) view.findViewById(R.id.thread_bookmark_list_view);

        final DatabaseHelper dbh = new DatabaseHelper(getContext());
        rt = ThreadBookmarkService.getThreads(dbh);

        readingThreadListView.setAdapter(new ThreadAdapter(getContext(), rt, this));

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onItemClicked(int position) {
        ThreadBookmark location = rt.get(position);
        Toast.makeText(getContext(), "Navigating to " + location.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), VerseListingActivity.class);
        intent.putExtra("chapter_id", location.getChapterId());
        intent.putExtra("book_id", location.getBookId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
