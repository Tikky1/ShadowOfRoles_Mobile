package com.kankangames.shadowofroles.ui.fragments.fullscreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kankangames.shadowofroles.ui.fragments.IFullScreenFragment;

public abstract class FullScreenFragment extends DialogFragment implements IFullScreenFragment {

    protected OnClose onClose;
    public interface OnClose{
        void backClicked();
    }

    public FullScreenFragment(OnClose onClose) {
        this.onClose = onClose;
    }


    @Nullable
    @Override
    @CallSuper
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(getLayoutResource(), container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {

            hideSystemUIBackgroundBlack(this);
        }

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {

        if(onClose !=null){
            onClose.backClicked();
        }

        super.onDismiss(dialog);

    }


    protected abstract int getLayoutResource();
}
