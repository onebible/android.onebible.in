package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.interfaces.NotesAdapterListener;
import co.sridhar.tamilbible.model.Note;
import co.sridhar.tamilbible.utils.MyConstants;
import co.sridhar.tamilbible.utils.TimeUtils;

public class NotesAdapter extends RecyclerSwipeAdapter<NotesAdapter.NotesViewHolder> {

    private Context mContext;
    private List<Note> notes;
    private NotesAdapterListener listener;

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.note_swipe_layout;
    }

    public NotesAdapter(Context mContext, List<Note> notes, NotesAdapterListener listener) {
        this.mContext = mContext;
        this.notes = notes;
        this.listener = listener;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTextView, noteTitleTxtview, timestampTxtView;
        private LinearLayout notesContainer;
        private ImageView notesShareRow, notesDeleteRow, notesEditRow;
        private SwipeLayout swipeLayout;
        private RelativeLayout noteContainerRL;

        private RelativeLayout editNoteRL, shareNoteRL, deleteNoteRL;

        private NotesViewHolder(final View view) {
            super(view);
            notesTextView = (TextView) view.findViewById(R.id.note_description_txt_view);
            noteTitleTxtview = (TextView) view.findViewById(R.id.note_title_text_view);
            timestampTxtView = (TextView) view.findViewById(R.id.timestamp);

            notesContainer = (LinearLayout) view.findViewById(R.id.note_container);
            noteContainerRL = (RelativeLayout) view.findViewById(R.id.rl_note_container);

            notesShareRow = (ImageView) view.findViewById(R.id.note_share_row);
            notesDeleteRow = (ImageView) view.findViewById(R.id.note_delete_row);
            notesEditRow = (ImageView) view.findViewById(R.id.note_edit_row);

            editNoteRL = (RelativeLayout) view.findViewById(R.id.note_edit_row_rl);
            shareNoteRL = (RelativeLayout) view.findViewById(R.id.note_share_row_rl);
            deleteNoteRL = (RelativeLayout) view.findViewById(R.id.note_delete_row_rl);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    return true;
                }
            });

            swipeLayout = (SwipeLayout) view.findViewById(R.id.note_swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }

    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotesViewHolder holder, final int position) {
        Note note = notes.get(position);
        holder.noteTitleTxtview.setText(note.getTitle());
        holder.notesTextView.setText(note.getDescription());

        long now = System.currentTimeMillis();

        try {
            String datetime1 = note.getUpdatedAt();
            SimpleDateFormat dateFormat = new SimpleDateFormat(TimeUtils.getFormat());
            Date convertedDate = dateFormat.parse(datetime1);

            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);

            holder.timestampTxtView.setText(relativeTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        applyClickEvents(holder, position);
        mItemManger.bindView(holder.itemView, position);
    }

    private void applyClickEvents(final NotesViewHolder holder, final int position) {
        holder.notesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRowClicked(position);
            }
        });

        holder.noteContainerRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRowClicked(position);
            }
        });

        holder.notesContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

        holder.notesShareRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.onShareClicked(position)) {
                    holder.swipeLayout.close();
                }
            }
        });

        holder.shareNoteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.onShareClicked(position)) {
                    holder.swipeLayout.close();
                }
            }
        });

        final Note note = notes.get(position);
        holder.notesEditRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditClicked(note.getId());
                holder.swipeLayout.close();
            }
        });

        holder.editNoteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditClicked(note.getId());
                holder.swipeLayout.close();
            }
        });

        holder.deleteNoteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClicked(note.getId());
                holder.swipeLayout.close();
            }
        });

        holder.notesDeleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClicked(note.getId());
                holder.swipeLayout.close();
            }
        });

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                mItemManger.closeAllExcept(layout);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public String getNoteByPosition(int position) {
        Note note = this.notes.get(position);
        return note.getTitle() + "\r\n\r\n" + note.getDescription();
    }

    public Note getItemByPosition(int position) {
        return this.notes.get(position);
    }

    public void removeByPosition(int position) {
        for (Note note : notes) {
            if (note.getId() == position) {
                this.notes.remove(note);
                break;
            }
        }
        notifyDataSetChanged();
    }

}
