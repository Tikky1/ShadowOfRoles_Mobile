package com.kankangames.shadowofroles.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.settings.Language;

public class LanguageSelectAdapter extends ArrayAdapter<Language> {
    private final Language[] languageList = Language.values();

    private final Context context;

    public LanguageSelectAdapter(@NonNull Context context){
        super(context, R.layout.language_spinner_item);
        this.context = context;

    }

    @Override
    public int getCount() {
        return languageList.length;
    }

    @Nullable
    @Override
    public Language getItem(int position) {
        return languageList[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.language_spinner_item, parent, false);
        }
        ConstraintLayout constraintLayout = convertView.findViewById(R.id.langspinner_container);
        TextView textView = constraintLayout.findViewById(R.id.language_spinner_item_name);
        textView.setText(languageList[position].text());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.language_spinner_item, parent, false);
        }

        String lang = languageList[position].text();

        TextView textView = convertView.findViewById(R.id.language_spinner_item_name);
        textView.setText(lang);


        return convertView;
    }

}
