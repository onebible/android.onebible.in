package co.sridhar.tamilbible.adapter.interfaces;

public interface NotesAdapterListener {

    void onRowClicked(int position);

    void onRowLongClicked(int position);

    boolean onShareClicked(int position);

    void onDeleteClicked(int position);

    void onEditClicked(int noteId);

}

