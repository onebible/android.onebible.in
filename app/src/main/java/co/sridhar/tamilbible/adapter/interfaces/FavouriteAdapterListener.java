package co.sridhar.tamilbible.adapter.interfaces;

public interface FavouriteAdapterListener {

    void onRowClicked(int position);

    void onRowLongClicked(int position);

    boolean onShareClicked(int position);

    void onDeleteClicked(int position);

}
