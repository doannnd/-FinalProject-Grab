package com.nguyendinhdoan.finalprojectgrab.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nguyendinhdoan.finalprojectgrab.R;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private View view;

    public CustomInfoWindow(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_marker_infor_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView pickupInfoTextView = view.findViewById(R.id.pickup_info_text_view);
        pickupInfoTextView.setText(marker.getTitle());

        TextView pickupSnippetTextView = view.findViewById(R.id.pickup_snippet_text_view);
        pickupSnippetTextView.setText(marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
