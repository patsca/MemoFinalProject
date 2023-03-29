package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MemoSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_settings);
        initSettings();
        initSortOrderClick();
        initSortByClick();
        initListButton();
        initSettingsButton();

    }
    private void initSettings() {
        String sortBy = getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).getString("sortfield","memoname");
        String sortOrder = getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton rbSubject = findViewById(R.id.radioSubject);
        RadioButton rbImportance = findViewById(R.id.radioImportance);
        RadioButton rbDate = findViewById(R.id.radioDate);

        if (sortBy.equalsIgnoreCase("memoname")) {
            rbSubject.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("prio")) {
            rbImportance.setChecked(true);
        } else {
            rbDate.setChecked(true);
        }


        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);

        }
    }
    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbSubject = findViewById(R.id.radioSubject);
                RadioButton rbImportance = findViewById(R.id.radioImportance);
                RadioButton rbDate = findViewById(R.id.radioDate);
                if (rbSubject.isChecked()) {
                    getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).edit().putString("sortfield","memoname").apply();
                }
                else if(rbImportance.isChecked()) {
                    getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).edit().putString("sortfield","prio").apply();
                } else {
                    getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).edit().putString("sorfield", "date").apply();
                }


            }

        });
    }
    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder) ;
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener () {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MemoPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();

                }
                else {
                    getSharedPreferences("MemoPreferences",Context.MODE_PRIVATE).edit()
                            .putString("sortorder","DESC").apply();
                }
            }
        });
    }

    private void initListButton() {
        ImageButton imageListButton = findViewById(R.id.imageButtonList);
        imageListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoSettingsActivity.this, MemoListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton () {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);

    }

}
