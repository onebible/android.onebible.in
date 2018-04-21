package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.interfaces.VerseAdapterListener;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.model.Verse;
import co.sridhar.tamilbible.utils.ColorUtils;

public class SearchResultAdapter extends RecyclerSwipeAdapter<SearchResultAdapter.MyViewHolder> {

    private Context mContext;
    private List<Verse> verses;
    private VerseAdapterListener listener;
    private SparseBooleanArray selectedItems;

    private String searchTerm;

    public SearchResultAdapter(Context mContext, List<Verse> verses, VerseAdapterListener listener, String searchTerm) {
        this.mContext = mContext;
        this.verses = verses;
        this.listener = listener;
        this.selectedItems = new SparseBooleanArray();
        this.searchTerm = searchTerm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Verse verse = verses.get(position);

        String tvt = verse.getVerse();
        holder.verse.setText(verse.getVerse());
        holder.location.setText(Html.fromHtml("<br>" + verse.getLocation()));

        Spannable WordtoSpan = new SpannableString(verse.getVerse());
        String ett = searchTerm;
        int ofe = tvt.indexOf(ett, 0);
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(ett, ofs);
            if (ofe == -1) {
                break;
            } else {
                WordtoSpan.setSpan(new BackgroundColorSpan(Color.parseColor(ColorUtils.getSearchHighlightingColor())), ofe, ofe + ett.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.verse.setText(WordtoSpan, TextView.BufferType.SPANNABLE);
            }
        }

        applyClickEvents(holder, position);
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.search_swipe_layout;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView verse, location;
        private ImageView favourite, share, viewChapter;
        private LinearLayout searchContainer;
        private SwipeLayout swipeLayout;

        private MyViewHolder(final View view) {
            super(view);
            verse = (TextView) view.findViewById(R.id.search_view);
            location = (TextView) view.findViewById(R.id.location);

            favourite = (ImageView) view.findViewById(R.id.search_fav_row);
            share = (ImageView) view.findViewById(R.id.search_share_row);
            viewChapter = (ImageView) view.findViewById(R.id.search_view_chapter);
            searchContainer = (LinearLayout) view.findViewById(R.id.search_container);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    return true;
                }
            });

            swipeLayout = (SwipeLayout) view.findViewById(R.id.search_swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        }
    }

    private void applyClickEvents(final MyViewHolder holder, final int position) {
        holder.verse.setHapticFeedbackEnabled(true);
        holder.searchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRowClicked(position);
            }
        });

        holder.searchContainer.setOnLongClickListener(new View.OnLongClickListener() {
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
                if (listener.onFavouriteClicked(position)) {
                    holder.swipeLayout.close();
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.onShareClicked(position)) {
                    holder.swipeLayout.close();
                }
            }
        });

        holder.viewChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.navigateToChapter(position);
                holder.swipeLayout.close();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return verses.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return verses.size();
    }

    public String getItembyPosition(int position) {
        return this.verses.get(position).getVerse() + "\r\n" + this.verses.get(position).getLocation();
    }

    public Favourite getVerseForFavByPosition(int position) {
        Verse verse = verses.get(position);

        String verses = verse.getVerse() + "\r\n";
        String chapterName = verse.getChapterName() + " " + verse.getChapterId() + " : " + verse.getVerseId();
        Favourite favourite = new Favourite(verses, chapterName);
        favourite.setVerseDetails(verse);

        return favourite;
    }

    public Verse getResultByPosition(int position) {
        return this.verses.get(position);
    }

}
