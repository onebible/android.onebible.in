package co.sridhar.tamilbible.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Note;
import co.sridhar.tamilbible.service.NoteService;

public class NoteCreationActivity extends AppCompatActivity {

    private EditText titleEditView, descriptionEditView;
    private FloatingActionButton editNote;

    Mode mode;
    String noteId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.mode.equals(Mode.VIEW)) {
            menu.findItem(R.id.create_note).setVisible(false);
        } else if (this.mode.equals(Mode.EDIT)) {
            menu.findItem(R.id.create_note).setVisible(true);
        } else {
            menu.findItem(R.id.create_note).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_creation_activity);

        titleEditView = (EditText) findViewById(R.id.title_et);
        descriptionEditView = (EditText) findViewById(R.id.description_e);
        editNote = (FloatingActionButton) findViewById(R.id.edit_note);

        if (getIntent().getExtras() != null) {
            noteId = getIntent().getExtras().getString("note_id");
            mode = Mode.valueOf(getIntent().getExtras().getString("mode"));
        }

        switchMode(mode);
        loadData(mode);

        descriptionEditView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode(Mode.EDIT);
                loadData(mode);
            }
        });
    }

    private void switchMode(Mode mode) {
        this.mode = mode;
        invalidateOptionsMenu();
        switch (mode) {
            case EDIT:
                setTitle("Edit note");
                titleEditView.setFocusableInTouchMode(true);
                descriptionEditView.setFocusableInTouchMode(true);
                descriptionEditView.requestFocus();
                editNote.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(descriptionEditView, InputMethodManager.SHOW_IMPLICIT);
                descriptionEditView.setSelection(descriptionEditView.getText().length());
                break;
            case VIEW:
                titleEditView.setFocusable(false);
                descriptionEditView.setFocusable(false);
                editNote.setVisibility(View.VISIBLE);
                break;
            case CREATE:
            default:
                setTitle("New note");
                titleEditView.setText("");
                descriptionEditView.setText("");
                titleEditView.setSelection(titleEditView.getText().length());
                editNote.setVisibility(View.GONE);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View vi = getWindow().getCurrentFocus();
        if (vi != null) {
            imm.hideSoftInputFromWindow(vi.getWindowToken(), 0);
        }
    }

    private void loadData(Mode mode) {
        if (mode.equals(Mode.CREATE)) {
            return;
        }
        final DatabaseHelper dbh = new DatabaseHelper(NoteCreationActivity.this);
        Note note = NoteService.getById(dbh, noteId);
        if (note == null) {
            setTitle(titleEditView.getText());
            return; //incase of new note creation; noteId will be null
        }
        titleEditView.setText(note.getTitle());
        setTitle(note.getTitle());
        descriptionEditView.setText(note.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_note:
                String title = titleEditView.getText().toString();
                if (title.length() == 0) {
                    Toast.makeText(this, "Title is required to save notes", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Note note = new Note(title, descriptionEditView.getText().toString());
                final DatabaseHelper dbh = new DatabaseHelper(NoteCreationActivity.this);

                if (noteId != null) {
                    note.setId(Integer.parseInt(noteId));
                    NoteService.edit(dbh, note);
                } else {
                    NoteService.add(dbh, note);
                }
                setResult(Activity.RESULT_OK);
                Toast.makeText(NoteCreationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                //finish();
                hideKeyboard();
                switchMode(Mode.VIEW);
                loadData(Mode.VIEW);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
