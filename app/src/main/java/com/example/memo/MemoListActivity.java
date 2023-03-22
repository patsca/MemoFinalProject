package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoListActivity extends AppCompatActivity {

    RecyclerView memoList;
    MemoAdapter memoAdapter;
    ArrayList<Memo> memo;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int memoId = memo.get(position).getMemoId();
            Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
            intent.putExtra("memoId", memoId);
            startActivity(intent);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        initSettingsButton();
        initListButton();
        initAddMemoButton();
        initDeleteSwitch();

    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).getString("sortfield", "memoname");
        String sortOrder = getSharedPreferences("MemoPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        MemoDataSource ds = new MemoDataSource(this);
        try {
            ds.open();
            memo = ds.getMemos(sortBy, sortOrder);
            ds.close();
            if (memo.size() > 0) {
                memoList = findViewById(R.id.rvMemo);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                memoList.setLayoutManager(layoutManager);
                memoAdapter = new MemoAdapter(memo, this);
                memoAdapter.setOnItemClickListener(onItemClickListener);
                memoList.setAdapter(memoAdapter);
            } else {
                Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving memo", Toast.LENGTH_LONG).show();
        }
    }

    private void initSettingsButton() {
        ImageButton imageSettingsButton = findViewById(R.id.imageButtonSettings);
        imageSettingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MemoListActivity.this, MemoSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);
    }

    private void initAddMemoButton() {
        Button newMemo = findViewById(R.id.buttonAddMemo);
        newMemo.setOnClickListener(view -> {
            Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener((compoundButton, b) -> {
            Boolean status = compoundButton.isChecked();
            memoAdapter.setDelete(status);
            memoAdapter.notifyDataSetChanged();
        });
    }
}