package com.rolegame.game.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rolegame.game.R;
import com.rolegame.game.ui.adapters.ViewPagerAdapter;

public class RoleBookFragment extends DialogFragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ViewPagerAdapter viewPagerAdapter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_role_book, null);
        builder.setView(view);

        view.setVisibility(View.INVISIBLE);
        Animation openAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.book_open);
        view.startAnimation(openAnimation);
        view.setVisibility(View.VISIBLE);

        AlertDialog dialog = builder.create();

        dialog.setOnDismissListener(dialogInterface -> {
            closingAnimation(view);
        });

        tabLayout = view.findViewById(R.id.tab_Layout);
        viewPager = view.findViewById(R.id.view_Pager);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Role Info");
                    break;
                case 1:
                    tab.setText("Role Guide");
                    break;
            }
        }).attach();

        Button closeButton = view.findViewById(R.id.close_book_button);
        closeButton.setOnClickListener(v -> closingAnimation(view));



        return dialog;
    }

    private void closingAnimation(View view){
        if (view != null) {

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.book_close);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (getDialog() != null && getDialog().isShowing()) {
                        getDialog().dismiss();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.book_open);
            view.startAnimation(animation);
        }
    }




}
