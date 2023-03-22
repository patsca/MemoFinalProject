package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private Memo currentMemo;
    String prioButtonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();
        initSettingsButton();
        initSaveButton();
        initToggleButton();
        initRadioButton();
        initTextChangedEvents();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initMemo(extras.getInt("memoId"));
        } else {
            currentMemo = new Memo();
        }
    }

    private void initListButton() {
        ImageButton imageListButton = findViewById(R.id.imageButtonList);
        imageListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MemoListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton () {
        ImageButton imageSettingsButton = findViewById(R.id.imageButtonSettings);
        imageSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MemoSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();
                MemoDataSource ds = new MemoDataSource(MainActivity.this);

                try {
                    ds.open();

                    if (currentMemo.getMemoId() == -1) {
                        wasSuccessful = ds.insertMemo(currentMemo);
                    }

                    else {
                        wasSuccessful = ds.updateMemo(currentMemo);
                    }

                    ds.close();
                }

                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }

                if (wasSuccessful) {
                    int newId = ds.getLastMemoID();
                    currentMemo.setMemoId(newId);
                }
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editMemo = findViewById(R.id.editMemo);
        EditText editMemoName = findViewById(R.id.editNoteName);
        Button buttonSave = findViewById(R.id.buttonSave);


        editMemo.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        editMemoName.setEnabled(enabled);
    }

    private void initMemo(int id) {

        MemoDataSource ds = new MemoDataSource(MainActivity.this);
        try {
            ds.open();
            currentMemo = ds.getSpecificMemo(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Memo Failed", Toast.LENGTH_LONG).show();
        }

        EditText editMemo = findViewById(R.id.editMemo);
        EditText editName = findViewById(R.id.editNoteName);


        editMemo.setText(currentMemo.getMemoContent());
        editName.setText(currentMemo.getMemoName());
    }

    private void initTextChangedEvents(){
        final EditText etMemoName = findViewById(R.id.editNoteName);
        etMemoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoName(etMemoName.getText().toString());

            }
        });

        final EditText etMemoContent = findViewById(R.id.editMemo);
        etMemoContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoContent(etMemoContent.getText().toString());

            }
        });
    }

    private void initRadioButton(){
        RadioGroup prioRadioGroup = findViewById(R.id.radioGroupPrio);
        prioRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbLow = findViewById(R.id.radioButtonLow);
                RadioButton rbMed = findViewById(R.id.radioButtonMed);
                RadioButton rbHigh = findViewById(R.id.radioButtonHigh);

                if (rbLow.isChecked()){
                    currentMemo.setMemoPrio("Low");
                }
                else if (rbMed.isChecked()){
                    currentMemo.setMemoPrio("Medium");
                }
                else {
                    currentMemo.setMemoPrio("High");
                }
            }
        });


    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        EditText editMemo = findViewById(R.id.editMemo);
        imm.hideSoftInputFromWindow(editMemo.getWindowToken(), 0);
    }
}