package com.kankangames.shadowofroles.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

import java.util.List;

public class LanguageSelectAdapter extends ArrayAdapter<String> {
    private final List<String> languageList;

    private final Context context;

    public LanguageSelectAdapter(@NonNull Context context){
        super(context, R.layout.language_spinner_item);
        this.languageList = List.of("English","Türkçe");
        this.context = context;

    }

    @Override
    public int getCount() {
        return languageList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return languageList.get(position);
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
        textView.setText(languageList.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.language_spinner_item, parent, false);
        }


        String lang = languageList.get(position);


        TextView textView = convertView.findViewById(R.id.language_spinner_item_name);
        textView.setText(lang);




        return convertView;
    }

}
