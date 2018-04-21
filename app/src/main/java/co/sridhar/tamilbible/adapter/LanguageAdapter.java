package co.sridhar.tamilbible.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.settings.AppPreferences;
import co.sridhar.tamilbible.settings.Defaults;

public class LanguageAdapter extends BaseAdapter {

    private ArrayList<Language> languages;
    private Context context;
    private Listener listener;

    private static LayoutInflater inflater = null;

    public LanguageAdapter(Context mContext, ArrayList<Language> languages, Listener listener) {
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
        TextView tv;
        ImageView deleteImg, downloadImg, cancelInstallationImg;
        LinearLayout langLayout, rowLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.language_list, null);

        holder.tv = (TextView) rowView.findViewById(R.id.lang_title);

        holder.deleteImg = (ImageView) rowView.findViewById(R.id.lang_action_delete);
        holder.downloadImg = (ImageView) rowView.findViewById(R.id.lang_action_download);
        holder.cancelInstallationImg = (ImageView) rowView.findViewById(R.id.lang_action_cancel);

        holder.langLayout = (LinearLayout) rowView.findViewById(R.id.lang_layout);
        holder.rowLayout = (LinearLayout) rowView.findViewById(R.id.language_row_layout);

        final Language language = languages.get(position);
        holder.tv.setText(language.getTitle());

        if (language.isInstalled()) {
            holder.deleteImg.setVisibility(View.VISIBLE);
            holder.downloadImg.setVisibility(View.GONE);
            holder.cancelInstallationImg.setVisibility(View.GONE);
        } else if (language.isProcessingLang(context)) {
            holder.deleteImg.setVisibility(View.GONE);
            holder.downloadImg.setVisibility(View.GONE);
            holder.cancelInstallationImg.setVisibility(View.VISIBLE);
            holder.rowLayout.setBackgroundColor(Color.parseColor(Defaults.getInstance().getHighlightingColor()));
        } else {
            holder.deleteImg.setVisibility(View.GONE);
            holder.downloadImg.setVisibility(View.VISIBLE);
            holder.cancelInstallationImg.setVisibility(View.GONE);
        }

        String defaultLanguage = AppPreferences.getInstance().getDefaultLanguage(context);
        if (defaultLanguage.equalsIgnoreCase(language.getTitle())) {
            holder.deleteImg.setVisibility(View.GONE);
        }

        if (languages.size() == 1) {
            holder.deleteImg.setVisibility(View.INVISIBLE);
        }

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(language);
            }
        });

        holder.downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDownloadClick(language);
            }
        });


        holder.cancelInstallationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClick(language);
            }
        });

        return rowView;
    }

    public interface Listener {
        void onDownloadClick(Language language);

        void onDeleteClick(Language language);

        void onCancelClick(Language language);
    }

}

