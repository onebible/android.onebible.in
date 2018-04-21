package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import co.sridhar.tamilbible.adapter.interfaces.FavouriteAdapterListener;
import co.sridhar.tamilbible.model.Favourite;

public class FavouritesAdapter extends RecyclerSwipeAdapter<FavouritesAdapter.FavouriteViewHolder> {

    private Context mContext;
    private List<Favourite> favourites;
    private FavouriteAdapterListener listener;

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.fav_swipe_layout;
    }

    public FavouritesAdapter(Context context, List<Favourite> favourites, FavouriteAdapterListener listener) {
        this.mContext = context;
        this.favourites = favourites;
        this.listener = listener;
    }

    static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        private TextView favouriteTextView, location;
        private LinearLayout favouriteContainer;
        private ImageView favShareRow, favDeleteRow;
        private SwipeLayout swipeLayout;

        private FavouriteViewHolder(final View view) {
            super(view);
            favouriteTextView = (TextView) view.findViewById(R.id.favourite);
            location = (TextView) view.findViewById(R.id.location);
            favouriteContainer = (LinearLayout) view.findViewById(R.id.favourite_container);
            favShareRow = (ImageView) view.findViewById(R.id.fav_share_row);
            favDeleteRow = (ImageView) view.findViewById(R.id.fav_delete_row);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    return true;
                }
            });

            swipeLayout = (SwipeLayout) view.findViewById(R.id.fav_swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }

    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_list_row, parent, false);
        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, final int position) {
        final Favourite favourite = favourites.get(position);
        holder.favouriteTextView.setText(Html.fromHtml(favourite.getPassage()));
        holder.location.setText(Html.fromHtml("<br>" + favourite.getLocation()));
        applyClickEvents(holder, position);

        holder.favDeleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClicked(position);
                holder.swipeLayout.close();
            }
        });

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                mItemManger.closeAllExcept(layout);
            }
        });
        mItemManger.bindView(holder.itemView, position);
    }

    private void applyClickEvents(final FavouriteViewHolder holder, final int position) {
        holder.favouriteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRowClicked(position);
            }
        });

        holder.favouriteContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

        holder.favShareRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.onShareClicked(position)) {
                    holder.swipeLayout.close();
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return favourites.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public String getItemByPosition(int position) {
        return this.favourites.get(position).getPassageForShare();
    }

    public Favourite getByPosition(int position) {
        return this.favourites.get(position);
    }

    public void removeByPosition(int position) {
        this.favourites.remove(position);
        notifyDataSetChanged();
    }

}
