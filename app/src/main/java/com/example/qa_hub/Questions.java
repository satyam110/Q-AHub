package com.example.qa_hub;

import android.widget.EditText;

public class Questions {

    private String askQue;


    public Questions() {

    }

    public Questions(String askQue) {
        this.askQue = askQue;
    }

    public String getAskQue() {
        return askQue;
    }

    public void setAskQue(String askQue) {
        this.askQue = askQue;
    }
}
