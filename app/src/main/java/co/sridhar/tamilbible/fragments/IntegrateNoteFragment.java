package co.sridhar.tamilbible.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Note;
import co.sridhar.tamilbible.service.NoteService;

public class IntegrateNoteFragment extends DialogFragment {

    private Button addToNotes, cancel;
    private AutoCompleteTextView titleAutoComplete;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.integrate_notes, null);
        builder.setTitle("Select a note");

        addToNotes = (Button) view.findViewById(R.id.integrate_note);
        cancel = (Button) view.findViewById(R.id.cancel);

        final DatabaseHelper dbh = new DatabaseHelper(getActivity());
        final List<Note> notes = NoteService.getAllNotes(dbh);

        List<String> titles = new ArrayList<>();

        for (Note note : notes) {
            titles.add(note.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, titles);

        titleAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        titleAutoComplete.setThreshold(1);//will start working from first character
        titleAutoComplete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        addToNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleAutoComplete.getText().toString().trim();
                String content = getArguments().getString("content", "");

                if (title.length() == 0) {
                    Toast.makeText(getActivity(), "Select a note to add", Toast.LENGTH_SHORT).show();
                } else {
                    Note editNote = null;
                    for (Note note : notes) {
                        if (note.getTitle().equalsIgnoreCase(title)) {
                            editNote = note;
                            break;
                        }
                    }

                    if (editNote == null) {
                        createNoteAndAdd(title, content);
                    } else {
                        appendToNote(editNote, content);
                    }

                    dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NORMAL, R.style.CustomDialog);
        }

        builder.setView(view);
        return builder.create();
    }

    private void createNoteAndAdd(String title, String content) {
        Note note = new Note(title, content);
        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        NoteService.add(dbh, note);
        Toast.makeText(getActivity(), "Created a note '" + title + "' and added to it", Toast.LENGTH_LONG).show();
    }

    private void appendToNote(Note note, String content) {
        String finalDescription = note.getDescription() + "\r\n \r\n" + content;
        note.setDescription(finalDescription.trim());
        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        NoteService.edit(dbh, note);
        Toast.makeText(getActivity(), "Added to " + note.getTitle(), Toast.LENGTH_LONG).show();
    }

}
