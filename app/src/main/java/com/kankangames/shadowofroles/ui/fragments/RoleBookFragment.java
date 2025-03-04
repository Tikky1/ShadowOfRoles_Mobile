package com.kankangames.shadowofroles.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.ui.adapters.ViewPagerAdapter;

public class RoleBookFragment extends HidingNavigationFragment implements IFullScreenFragment{

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
                    tab.setText(R.string.role_info);
                    break;
                case 1:
                    tab.setText(R.string.role_guide);
                    break;
            }
        }).attach();

        Button closeButton = view.findViewById(R.id.close_book_button);
        closeButton.setOnClickListener(v -> closingAnimation(view));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        hideSystemUI(this);
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
