package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import java.util.ArrayList;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.model.Language;

public class DbInstallerAdapter extends BaseAdapter {

    private ArrayList<Language> languages;
    private Context context;
    private Listener listener;

    private static LayoutInflater inflater = null;

    public DbInstallerAdapter(Context mContext, ArrayList<Language> languages, Listener listener) {
        this.languages = languages;
        this.context = mContext;
        this.listener = listener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public Object getItem(int position) {
        return languages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder {
        RadioButton rbn;
    }

    private RadioButton mCurrentlyCheckedRB;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.language_installer_list, null);

        final Language language = languages.get(position);

        holder.rbn = (RadioButton) rowView.findViewById(R.id.title_options);
        holder.rbn.setText(language.getTitle());
        holder.rbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentlyCheckedRB == null) {
                    mCurrentlyCheckedRB = (RadioButton) v;
                    mCurrentlyCheckedRB.setChecked(true);
                    listener.onClick(language);
                    return;
                }

                if (mCurrentlyCheckedRB == v) {
                    listener.onClick(language);
                    return;
                }

                mCurrentlyCheckedRB.setChecked(false);
                ((RadioButton) v).setChecked(true);
                mCurrentlyCheckedRB = (RadioButton) v;

                listener.onClick(language);
            }
        });

        return rowView;
    }

    public interface Listener {
        void onClick(Language language);
    }

}

