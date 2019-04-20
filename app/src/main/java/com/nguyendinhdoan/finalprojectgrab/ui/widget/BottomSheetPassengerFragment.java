package com.nguyendinhdoan.finalprojectgrab.ui.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyendinhdoan.finalprojectgrab.R;

public class BottomSheetPassengerFragment extends BottomSheetDialogFragment {

    public static final String TAG_KEY = "TAG_KEY";
    private String tag;

    public static BottomSheetPassengerFragment newInstance(String tag) {
        BottomSheetPassengerFragment fragment = new BottomSheetPassengerFragment();
        Bundle args = new Bundle();
        args.putString(TAG_KEY, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tag = getArguments().getString(TAG_KEY);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_passenger, container, false);
        return view;
    }
}
