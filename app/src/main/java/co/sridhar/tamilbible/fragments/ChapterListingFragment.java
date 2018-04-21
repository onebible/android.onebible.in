package co.sridhar.tamilbible.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.activity.VerseListingActivity;
import co.sridhar.tamilbible.settings.Defaults;
import co.sridhar.tamilbible.utils.MyConstants;

public class ChapterListingFragment extends Fragment {

    private FlexboxLayout flexboxLayout;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chapter_listing, container, false);

        flexboxLayout = (FlexboxLayout) view.findViewById(R.id.flexbox_layout);
        flexboxLayout.setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);

        final String bookId = this.getArguments().getString("book_id");

        int chaptersCount = MyConstants.getInstance(getContext()).getBookNumberToChapterCounts(bookId);

        if (chaptersCount == 1) {
            // for jude and other books that have only one chapter
            Intent intent = new Intent(getActivity(), VerseListingActivity.class);
            intent.putExtra("chapter_id", 1 + "");
            intent.putExtra("book_id", bookId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        for (int chapter = 0; chapter < chaptersCount; chapter++) {
            int usuableSeq = chapter + 1;
            Button button = new Button(new ContextThemeWrapper(getActivity(), R.style.PrimaryButton));
            button.setId(usuableSeq);
            button.setMinimumHeight(100);
            button.setText(usuableSeq + "");
            button.setMinimumWidth(100);

            button.setTextColor(Defaults.getInstance().getDefaultFontColor());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), VerseListingActivity.class);
                    intent.putExtra("chapter_id", v.getId() + "");
                    intent.putExtra("book_id", bookId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            flexboxLayout.addView(button);
        }

        String title = MyConstants.getInstance(getContext()).getBookNameById(bookId);
        getActivity().setTitle(title);

        return view;
    }
}
