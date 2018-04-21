package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.interfaces.VerseAdapterListener;
import co.sridhar.tamilbible.helper.FlipAnimator;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.model.Verse;
import co.sridhar.tamilbible.settings.AppPreferences;
import co.sridhar.tamilbible.settings.Defaults;

public class VerseAdapter extends RecyclerSwipeAdapter<VerseAdapter.MyViewHolder> {
    private static int currentSelectedIndex = -1;
    private Context mContext;
    private List<Verse> verses;
    private VerseAdapterListener listener;
    private SparseBooleanArray selectedItems;

    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private Integer highlightVerseId;

    public VerseAdapter(Context mContext, List<Verse> verses, VerseAdapterListener listener, Integer highlightVerseId) {
        this.mContext = mContext;
        this.verses = verses;
        this.listener = listener;
        this.highlightVerseId = highlightVerseId;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Verse message = verses.get(position);
        holder.verseTxtView.setText(message.getVerse());

        float fontSize = AppPreferences.getInstance().getFontSize(mContext);
        holder.verseTxtView.setTextSize(fontSize);

        int textColor = AppPreferences.getInstance().getTextColor(mContext);
        if (AppPreferences.getInstance().isNightMode(mContext)) {
            textColor = Defaults.getInstance().getDefaultFontColor();
        }

        holder.verseTxtView.setTextColor(ContextCompat.getColor(mContext, textColor));

        if (AppPreferences.getInstance().isLessSpaceView(mContext)) {
            holder.verseTxtView.setLineSpacing(1, 1.5f);
            holder.verseTxtView.setPadding(0, 0, 0, 0);
        } else {
            holder.verseTxtView.setLineSpacing(8, 1.5f);
            holder.verseTxtView.setPadding(0, 10, 0, 10);
        }

        holder.iconText.setText(message.getVerseId());
        holder.itemView.setActivated(selectedItems.get(position, false));
        applyIconAnimation(holder, position);
        applyProfilePicture(holder, message);
        applyClickEvents(holder, position);

        if (highlightVerseId != null) {
            if (highlightVerseId == position) {
                holder.rl.setBackgroundColor(Color.parseColor(Defaults.getInstance().getHighlightingColor()));
            }
        }

        if (message.isFavourited()) {
            holder.favContainer.setVisibility(View.VISIBLE);
        } else {
            holder.favContainer.setVisibility(View.GONE);
        }

        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    private void applyClickEvents(final MyViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });

        holder.favContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.unFavourite(position);
                notifyItemChanged(position);
            }
        });

        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRowClicked(position);
            }
        });

        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                mItemManger.closeAllExcept(layout);
            }
        });

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFavouriteClicked(position);
                notifyItemChanged(position);
                holder.swipeLayout.close();
            }
        });

        holder.favouriteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFavouriteClicked(position);
                notifyItemChanged(position);
                holder.swipeLayout.close();
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClicked(position);
                holder.swipeLayout.close();
            }
        });

        holder.shareRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClicked(position);
                holder.swipeLayout.close();
            }
        });

        holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addToNotes(position);
                holder.swipeLayout.close();
            }
        });

        holder.noteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addToNotes(position);
                holder.swipeLayout.close();
            }
        });

    }

    private void applyProfilePicture(MyViewHolder holder, Verse message) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        if (AppPreferences.getInstance().isNightMode(mContext)) {
            holder.imgProfile.setColorFilter(R.color.color_white);
        } else {
            holder.imgProfile.setColorFilter(message.getColor());
        }
        holder.iconText.setVisibility(View.VISIBLE);
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
            holder.rl.setBackgroundColor(Color.parseColor(Defaults.getInstance().getHighlightingColor()));
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            holder.rl.setBackgroundColor(mContext.getResources().getColor(R.color.row_item_background));
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    @Override
    public long getItemId(int position) {
        return verses.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return verses.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void refreshFavourite(int pos) {
        Verse verse = verses.get(pos);
        verse.setFavourited("1");
        notifyDataSetChanged();
    }

    public void refreshUnFavourite(int pos) {
        Verse verse = verses.get(pos);
        verse.setFavourited("0");
        notifyDataSetChanged();
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public Verse getByPosition(int position) {
        return verses.get(position);
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Verse> getSelectedItems() {
        List<Verse> verses = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            verses.add(this.verses.get(selectedItems.keyAt(i)));
        }
        return verses;
    }

    public Favourite getVerseForFav() {
        StringBuilder verses = new StringBuilder();
        List<Verse> selectedItems = getSelectedItems();

        for (Verse m : selectedItems) {
            verses.append(m.getVerse()).append("\r\n");
        }

        Verse verse = selectedItems.get(0);
        String chapterName = verse.getChapterName() + " " + verse.getChapterId() + " : " + getVerseReference(selectedItems);

        return new Favourite(verses.toString().trim(), chapterName);
    }

    public Favourite getVerseForNotes() {
        StringBuilder verses = new StringBuilder();
        List<Verse> selectedItems = getSelectedItems();

        for (Verse m : selectedItems) {
            verses.append(m.getVerse()).append("\r\n\r\n");
        }

        Verse verse = selectedItems.get(0);
        String chapterName = verse.getChapterName() + " " + verse.getChapterId() + " : " + getVerseReference(selectedItems);

        return new Favourite(verses.toString().trim(), chapterName);
    }

    public Favourite getVerseForFavByPosition(int position) {
        Verse verse = verses.get(position);
        String verses = verse.getVerse() + "\r\n";

        String chapterName = verse.getChapterName() + " " + verse.getChapterId() + " : " + verse.getVerseId();
        return new Favourite(verses, chapterName);
    }

    public String getSelectedForShare() {
        StringBuilder verses = new StringBuilder();
        List<Verse> selectedItems = getSelectedItems();
        for (Verse item : selectedItems) {
            verses.append("\r\n").append(item.getVerse()).append("\r\n");
        }

        Verse verse = selectedItems.get(0);
        String chapterName = verse.getChapterName() + " " + verse.getChapterId() + " : " + getVerseReference(selectedItems);
        verses.append("\r\n").append(chapterName);
        return verses.toString().trim();
    }

    public String getSelectedForShareByPosition(int position) {
        StringBuilder verses = new StringBuilder();
        Verse verse = this.verses.get(position);
        verses.append("\r\n").append(verse.getVerse()).append("\r\n");
        String chapterName = verse.getChapterName() + " " + verse.getChapterId() + " : " + verse.getVerseId();
        verses.append("\r\n").append(chapterName);
        return verses.toString().trim();
    }

    private String getVerseReference(List<Verse> selectedItems) {
        List<Integer> verseNumbers = new ArrayList<>();
        for (Verse selectedItem : selectedItems) {
            verseNumbers.add(Integer.parseInt(selectedItem.getVerseId()));
        }
        Collections.sort(verseNumbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        return TextUtils.join(", ", verseNumbers.toArray());
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView verseTxtView, iconText;
        private ImageView imgProfile, share, favourite, notes;
        private LinearLayout messageContainer;
        private RelativeLayout iconContainer, iconBack, iconFront, favContainer;
        private SwipeLayout swipeLayout;
        private RelativeLayout rl;

        private RelativeLayout favouriteRL, shareRL, noteRL;

        private MyViewHolder(final View view) {
            super(view);
            verseTxtView = (TextView) view.findViewById(R.id.txt_secondary);
            iconText = (TextView) view.findViewById(R.id.icon_text);

            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            favContainer = (RelativeLayout) view.findViewById(R.id.favourite_icon_container);

            favouriteRL = (RelativeLayout) view.findViewById(R.id.verse_fav_row_rl);
            shareRL = (RelativeLayout) view.findViewById(R.id.verse_share_row_rl);
            noteRL = (RelativeLayout) view.findViewById(R.id.verse_note_row_rl);

            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            favourite = (ImageView) view.findViewById(R.id.favrouite_row);
            share = (ImageView) view.findViewById(R.id.share_row);
            notes = (ImageView) view.findViewById(R.id.add_notes_row);

            messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    return true;
                }
            });

            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        }

    }

}
