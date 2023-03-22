package com.example.memo;

import android.provider.MediaStore;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Memo {

    private String memoName;
    private String memoContent;

    private String memoPrio;
    private Long memoTime;
    private int memoId;

    public Long getMemoTime() {
        return memoTime;
    }

    public void setMemoTime(Long memoTime) {
        this.memoTime = memoTime;
    }

    public String getMemoPrio() {
        return memoPrio;
    }

    public void setMemoPrio(String memoPrio) {
        this.memoPrio = memoPrio;
    }


    public String getMemoContent() {
        return memoContent;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(String memoName) {
        this.memoName = memoName;
    }



    public Memo() {
        memoId = -1;

    }

}
