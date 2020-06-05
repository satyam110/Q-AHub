package com.example.qa_hub;


import java.io.Serializable;

public class Questions implements Serializable {

    String askQue,quesDate,quesId,ans;


    public Questions() {

    }

    public Questions(String askQue,String quesDate,String quesId,String ans) {
        this.askQue = askQue;
        this.quesDate = quesDate;
        this.quesId= quesId;
        this.ans = ans;
    }



    public String getAskQue() {
        return askQue;
    }

    public void setAskQue(String askQue) {
        this.askQue = askQue;
    }

    public String getQuesDate() {
        return quesDate;
    }

    public void setQuesDate(String quesDate) {
        this.quesDate = quesDate;
    }

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getAns() { return ans; }

    public void setAns(String ans) { this.ans = ans; }
}
