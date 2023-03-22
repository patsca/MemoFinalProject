package com.example.memo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter {

    private ArrayList<Memo> memoData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;
    private MemoAdapter adapter;

    public class MemoViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMemo;
        public Button deleteButton;
        TextView textViewTime;


        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMemo = itemView.findViewById(R.id.textMemoName);
            deleteButton = itemView.findViewById(R.id.buttonDeleteMemo);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);


        }

        public TextView getTextViewMemo() {
            return textViewMemo;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
        public TextView getTextViewTime() {
            return textViewTime;
        }
    }


    public MemoAdapter(ArrayList<Memo> arrayList, Context context) {
        memoData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MemoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MemoViewHolder mvh = (MemoViewHolder) holder;
        mvh.getTextViewMemo().setText(memoData.get(position).getMemoName());

        if (isDeleting) {
            mvh.getDeleteButton().setVisibility(View.VISIBLE);
            mvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            mvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return memoData.size();
    }

    private void deleteItem (int position) {
        Memo memo = memoData.get(position);
        MemoDataSource ds = new MemoDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteMemo(memo.getMemoId());
            ds.close();
            if (didDelete) {
                memoData.remove(position);
                notifyDataSetChanged();
                Toast.makeText(parentContext, "Delete Successful", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
        }
    }

    public void setDelete (boolean b) {
        isDeleting = b;
    }
}