package com.example.qa_hub;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuesHolder> {

   Context context;
   ArrayList<Questions> dataList = new ArrayList<>();
   AnswerInterface answerInterface;
    public QuestionsAdapter(Context con, ArrayList<Questions> list){
        context = con;
        dataList = list;
        answerInterface = (AnswerInterface) context;
    }
    @NonNull
    @Override
    public QuesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler_view,parent,false);
        QuesHolder quesHolder = new QuesHolder(view);
        return quesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuesHolder holder, int position) {
        Questions questions = dataList.get(position);
        String ques = questions.getAskQue();
        String date = questions.getQuesDate();

        holder.rowQues.setText(ques);
        holder.rowDate.setText(date);

        holder.imageRowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Questions questions1 = dataList.get(position);
                showDialog(questions1);
            }
        });

        holder.rowQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Questions questions2 = dataList.get(position);
                showAnsDialog(questions2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class QuesHolder extends RecyclerView.ViewHolder{
        TextView rowQues;
        TextView rowDate;
        ImageView imageRowAnswer;

        public QuesHolder(@NonNull View itemView) {
            super(itemView);

            rowQues = itemView.findViewById(R.id.row_question);
            rowDate = itemView.findViewById(R.id.row_date);
            imageRowAnswer = itemView.findViewById(R.id.image_row_answer);
        }
    }

    public void showDialog(Questions quesObj){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_answer);
        dialog.show();

        final TextView queTitle = (TextView) dialog.findViewById(R.id.queView);
        final EditText ansEdit = (EditText) dialog.findViewById(R.id.ansQue);

        queTitle.setText(quesObj.getAskQue());

        Button ansBtn = (Button) dialog.findViewById(R.id.btnAnswer);

        ansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();
                String quesDate = simpleDateFormat.format(calendar.getTime());
                String answer = ansEdit.getText().toString();
                String quesId = quesObj.getQuesId();
                String queTitle = quesObj.getAskQue();
                Questions questionsObj=new Questions(queTitle,quesDate,quesId,answer);
                answerInterface.answerQuestion(questionsObj);
                dialog.dismiss();
            }
        });
    }

    public void showAnsDialog(Questions ques){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qna);
        dialog.show();

        String noansmsg = "No answers yet for this question, kindly check back soon!";
        String ansText ="Answer :";
        final TextView question = (TextView) dialog.findViewById(R.id.tv_ques);
        final TextView answer = (TextView) dialog.findViewById(R.id.tv_answer);
        Button btnBack = (Button) dialog.findViewById(R.id.btnBack);
        question.setText("Q. ".concat(ques.getAskQue()));
        if(ques.getAns()==null || ques.getAns().equals("")) {
            answer.setText(noansmsg);
        }
        else{
            answer.setText(ansText.concat(ques.getAns()));
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog.dismiss();
            }
        });
    }

}
