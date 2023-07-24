package co.sridhar.tamilbible.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.sridhar.tamilbible.BuildConfig;
import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.utils.FbUtils;
import co.sridhar.tamilbible.utils.InstagramUtils;

public class AboutUsFragment extends Fragment {

    private TextView appVersionTxtView;

    private LinearLayout rateUsLayout;
    private LinearLayout emailUsLayout;
    private LinearLayout fbLayout;
    private LinearLayout instaLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("About us");
        View view = inflater.inflate(R.layout.about_us, container, false);

        appVersionTxtView = (TextView) view.findViewById(R.id.app_version_txtview);
        appVersionTxtView.setText("Version " + discardHypen(BuildConfig.VERSION_NAME));

        rateUsLayout = (LinearLayout) view.findViewById(R.id.rate_us_layout);
        rateUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Opening play store", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        emailUsLayout = (LinearLayout) view.findViewById(R.id.email_us_layout);
        emailUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Opening email", Toast.LENGTH_LONG).show();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "sridhar@sridhar.co", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Onebible : Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        fbLayout = (LinearLayout) view.findViewById(R.id.fb_layout);
        fbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Opening Facebook", Toast.LENGTH_LONG).show();
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = FbUtils.getFacebookPageURL(getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });

        instaLayout = (LinearLayout) view.findViewById(R.id.instagram_layout);
        instaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Opening Instagram", Toast.LENGTH_LONG).show();
                Intent instaIntent = InstagramUtils.getIntent(getActivity().getPackageManager());
                startActivity(instaIntent);
            }
        });

        return view;
    }

    private String discardHypen(String version) {
        if (version.contains("-")) {
            return version.split("-")[0];
        }
        return version;
    }

}
