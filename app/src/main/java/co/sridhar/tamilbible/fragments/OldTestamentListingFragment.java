package co.sridhar.tamilbible.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.activity.ChapterListingActivity;
import co.sridhar.tamilbible.utils.MyConstants;

public class OldTestamentListingFragment extends Fragment {

    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_listing, container, false);
        listView = (ListView) view.findViewById(R.id.book_list_view);

        ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_activated_1, MyConstants.getInstance(getContext()).getOldTestamentBooks());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChapterListingActivity.class);
                intent.putExtra("book_id", position + "");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return view;
    }

}
