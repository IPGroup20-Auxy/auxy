package com.alexzamurca.auxy.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.alexzamurca.auxy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class SettingsFragment extends Fragment {
    public static boolean colourBlindState = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        String [] language =
                {"Language","English","French"};
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, language);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        String [] voice =
                {"Voice", "Jack", "Jill"};
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, voice);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter2);

        ColourBlindSwitch(v);
        return v;
    }

    private void ColourBlindSwitch(View view) {
        Switch colourBlindSwitch = (Switch) view.findViewById(R.id.ColourBlind);

        colourBlindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                colourBlindState = colourBlindSwitch.isChecked();
                Log.d("Blind", ""+isChecked);
            }
        });
    }

    public Boolean getColourBlindState(){
        return colourBlindState;
    }
}

