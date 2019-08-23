package com.example.moviesdbdemo.utils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviesdbdemo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    // <editor-fold desc="Vars">

    private View view;
    public BottomSheetDialog.OnShowListener listener;
    public BottomSheetDialog.OnCompleteClick completeListener;
    private Boolean isShowing = false;

    // </editor-fold>


    // <editor-fold desc="Overrides">


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int LayoutResource = R.layout.layout_bottom_dialog_movie;
        if (args != null) {
            LayoutResource = args.getInt("LayoutResource");
        }
        this.view = inflater.inflate(LayoutResource, container, false);
        if(listener != null) {
            listener.OnShowListenerFn(this.view);
        }
        if(completeListener != null) {
            completeListener.OnClick(this.view);
        }
        isShowing = true;
        return this.view;
    }

    @Override
    public void onDestroyView() {
        isShowing = false;
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isShowing = false;
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        isShowing = false;
        super.onCancel(dialog);
    }

    // </editor-fold>


    // <editor-fold desc="Public functions (setters, getters, etc)">

    public Boolean isShowing() {
        return isShowing;
    }

    public void setOnShowListener(BottomSheetDialog.OnShowListener listener){
        this.listener = listener;
    }

    public void setCompleteListener(BottomSheetDialog.OnCompleteClick completeListener) {
        this.completeListener = completeListener;
    }

    // </editor-fold>


    // <editor-fold desc="OnClick Interface">

    public interface OnShowListener {
        void OnShowListenerFn(View view);
    }

    public interface OnCompleteClick {
        void OnClick(View view);
    }

    // </editor-fold>

}

