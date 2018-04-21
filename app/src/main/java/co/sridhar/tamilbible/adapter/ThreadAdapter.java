package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.interfaces.ThreadBookmarkListener;
import co.sridhar.tamilbible.model.ThreadBookmark;

public class ThreadAdapter extends BaseAdapter {

    private List<ThreadBookmark> threadBookmarks;
    private ThreadBookmarkListener listener;
    private Context context;

    private static LayoutInflater inflater = null;

    public ThreadAdapter(Context mContext, List<ThreadBookmark> threadBookmarks, ThreadBookmarkListener listener) {
        this.threadBookmarks = threadBookmarks;
        this.context = mContext;
        this.listener = listener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return threadBookmarks.size();
    }

    @Override
    public Object getItem(int position) {
        return threadBookmarks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.thread_list_row, null);

        holder.tv = (TextView) rowView.findViewById(R.id.color_name);
        holder.img = (ImageView) rowView.findViewById(R.id.thread_view);

        ThreadBookmark thread = threadBookmarks.get(position);
        holder.tv.setText(thread.getTitle());

        holder.img.setImageResource(R.drawable.ic_bookmark_white_24dp);
        holder.img.setColorFilter(Color.parseColor(thread.getHexCode()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(position);
            }
        });
        return rowView;
    }

}
