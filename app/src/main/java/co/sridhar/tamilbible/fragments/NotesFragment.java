package co.sridhar.tamilbible.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.util.Collections;
import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.activity.Mode;
import co.sridhar.tamilbible.activity.NoteCreationActivity;
import co.sridhar.tamilbible.adapter.NotesAdapter;
import co.sridhar.tamilbible.adapter.interfaces.NotesAdapterListener;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Note;
import co.sridhar.tamilbible.service.NoteService;

public class NotesFragment extends Fragment implements NotesAdapterListener {

    private NotesAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Notes");
        View view = inflater.inflate(R.layout.notes_fragment, container, false);

        setHasOptionsMenu(true);

        emptyTextView = (TextView) view.findViewById(R.id.empty_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.notes_recycler_view);

        final DatabaseHelper dbh = new DatabaseHelper(getContext());
        List<Note> notes = NoteService.getAllNotes(dbh);
        loadData(notes);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoteCreationActivity.class);
                intent.putExtra("mode", Mode.CREATE.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 10001);
            }
        });

        return view;
    }

    private void loadData(List<Note> notes) {
        if (notes.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("Create your first note");
            recyclerView.setVisibility(View.INVISIBLE);
            return;
        } else {
            emptyTextView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        mAdapter = new NotesAdapter(getContext(), notes, this);
        mAdapter.setMode(Attributes.Mode.Single);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider_bg));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setTitle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {
            final DatabaseHelper dbh = new DatabaseHelper(getContext());
            List<Note> notes = NoteService.getAllNotes(dbh);
            loadData(notes);
        }
    }

    private void setTitle() {
        int size = mAdapter.getItemCount();
        if (size == 0) {
            getActivity().setTitle("Notes");
        } else {
            getActivity().setTitle("Notes (" + size + ")");
        }
    }

    @Override
    public void onRowClicked(int position) {
        Note note = mAdapter.getItemByPosition(position);
        Intent intent = new Intent(getActivity(), NoteCreationActivity.class);
        intent.putExtra("note_id", note.getId() + "");
        intent.putExtra("mode", Mode.VIEW.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 10001);
    }

    @Override
    public void onRowLongClicked(int position) {
    }

    @Override
    public void onEditClicked(int noteId) {
        mAdapter.notifyDataSetChanged();
        Intent intent = new Intent(getActivity(), NoteCreationActivity.class);
        intent.putExtra("note_id", noteId + "");
        intent.putExtra("mode", Mode.EDIT.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 10001);
    }

    @Override
    public boolean onShareClicked(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String shareBodyText = mAdapter.getNoteByPosition(position);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        return true;
    }

    @Override
    public void onDeleteClicked(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final DatabaseHelper dbh = new DatabaseHelper(getContext());
                mAdapter.removeByPosition(position);
                setTitle();
                boolean isSuccess = NoteService.remove(dbh, position);
                if (isSuccess) {
                    Toast.makeText(getContext(), "Note removed! ", Toast.LENGTH_SHORT).show();
                    if (mAdapter.getItemCount() == 0) {
                        loadData(Collections.<Note>emptyList());
                    }
                }
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

}
