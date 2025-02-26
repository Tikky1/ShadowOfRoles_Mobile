package com.rolegame.game.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.rolegame.game.R;
import com.rolegame.game.models.roles.templates.RoleTemplate;

import java.util.ArrayList;
import java.util.List;

public class LoreSpinnerHelper{
    private View spinnerView;
    private Spinner spinner;
    private Context context;
    public LoreSpinnerHelper(Context context){
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        spinnerView = inflater.inflate(R.layout.lorekeeper_spinner,null);

        spinner = spinnerView.findViewById(R.id.lorekeeper_spinner);
    }
    public void setData(List<RoleTemplate> data){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.lorekeeper_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, "Choosen" + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
    }

    public View getSpinnerView() {
        return spinnerView;
    }
}
