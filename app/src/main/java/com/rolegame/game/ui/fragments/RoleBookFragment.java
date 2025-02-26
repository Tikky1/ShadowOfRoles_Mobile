package com.rolegame.game.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class RoleBookFragment extends HidingNavigationFragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ViewPagerAdapter viewPagerAdapter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_role_book, null);
        builder.setView(view);

        openingAnimation(view);

        AlertDialog dialog = builder.create();
        backgroundTransparent(dialog);

        tabLayout = view.findViewById(R.id.tab_Layout);
        viewPager = view.findViewById(R.id.view_Pager);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
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


    @Override
    public int openingAnimationType() {
        return R.anim.book_open;
    }

    @Override
    public int closingAnimationType() {
        return R.anim.book_close;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_role_book;
    }
}
