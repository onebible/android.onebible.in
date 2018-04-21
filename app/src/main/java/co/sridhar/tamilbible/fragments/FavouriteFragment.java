package co.sridhar.tamilbible.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import co.sridhar.tamilbible.adapter.FavouritesAdapter;
import co.sridhar.tamilbible.adapter.interfaces.FavouriteAdapterListener;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.service.FavouritesService;
import co.sridhar.tamilbible.service.VerseService;

public class FavouriteFragment extends Fragment implements FavouriteAdapterListener {

    private FavouritesAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private VerseService mVerseService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Favourites");

        mVerseService = new VerseService(getContext());

        View view = inflater.inflate(R.layout.favourite_fragment, container, false);
        emptyTextView = (TextView) view.findViewById(R.id.empty_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.favourite_recycler_view);

        final DatabaseHelper dbh = new DatabaseHelper(getContext());
        List<Favourite> favourites = FavouritesService.getAllFavourites(dbh);
        loadData(favourites);
        return view;
    }

    private void loadData(List<Favourite> favourites) {
        if (favourites.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("Favourite a verse to see here");
            recyclerView.setVisibility(View.INVISIBLE);
            return;

        } else {
            emptyTextView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        mAdapter = new FavouritesAdapter(getContext(), favourites, this);
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
        String shareBodyText = mAdapter.getItemByPosition(position);
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
                Favourite favourite = mAdapter.getByPosition(position);
                final DatabaseHelper dbh = new DatabaseHelper(getContext());
                mAdapter.removeByPosition(position);
                boolean isSuccess = FavouritesService.remove(dbh, favourite.getId());
                if (isSuccess) {
                    Toast.makeText(getContext(), "Favourite removed! ", Toast.LENGTH_SHORT).show();
                    mVerseService.unFavourite(favourite);
                    if (mAdapter.getItemCount() == 0) {
                        loadData(Collections.<Favourite>emptyList());
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
