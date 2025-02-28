package com.rolegame.game.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class HidingNavigationFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(getLayoutID(), null);
        view.setVisibility(View.INVISIBLE);
        Animation openAnimation = AnimationUtils.loadAnimation(getContext(), openingAnimationType());
        view.startAnimation(openAnimation);
        view.setVisibility(View.VISIBLE);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();



        if (getDialog() != null) {
            Dialog dialog = getDialog();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(dialogInterface -> {
                View view = getView();
                if (view != null) {
                    closingAnimation(view);
                }
            });
        }

        hideSystemUI();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void hideSystemUI() {

        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

    }

    public abstract int openingAnimationType();
    public abstract int closingAnimationType();
    public abstract int getLayoutID();

    protected void closingAnimation(View view){

        int animationID = closingAnimationType();
        if (view != null && animationID!=-1) {

            Animation animation = AnimationUtils.loadAnimation(getContext(), animationID);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (getDialog() != null && getDialog().isShowing()) {
                        dismiss();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }
    }

    protected void openingAnimation(View view){
        view.setVisibility(View.INVISIBLE);
        Animation openAnimation = AnimationUtils.loadAnimation(getContext(), openingAnimationType());
        view.startAnimation(openAnimation);
        view.setVisibility(View.VISIBLE);
    }

    protected void backgroundTransparent(Dialog dialog){
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
