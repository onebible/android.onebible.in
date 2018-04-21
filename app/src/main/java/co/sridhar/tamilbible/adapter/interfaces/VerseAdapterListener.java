package co.sridhar.tamilbible.adapter.interfaces;

public interface VerseAdapterListener {

    void onIconClicked(int position);

    void onRowClicked(int position);

    void onRowLongClicked(int position);

    boolean onShareClicked(int position);

    boolean onFavouriteClicked(int position);

    void navigateToChapter(int position);

    void addToNotes(int position);

    void unFavourite(int position);

}
