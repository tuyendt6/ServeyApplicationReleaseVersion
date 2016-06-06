package com.samsung.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.samsung.object.QuestionObject;

import java.util.ArrayList;

import samsung.com.suveyapplication.R;

/**
 * Created by SamSunger on 5/14/2015.
 */
public class QuestionAdapter extends ArrayAdapter<QuestionObject> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private Context mContext;
    private ArrayList<QuestionObject> mListDealer;

    public QuestionAdapter(Context context, int resource, ArrayList<QuestionObject> objects) {
        super(context, resource, objects);
        mListDealer = objects;
        mContext = context;
    }

    class ViewHolder {
        TextView QuestionName;
        RadioButton rd1;
        RadioButton rd2;
        RadioButton rd3;
        RadioButton rd4;
        RadioGroup radioGroup;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.awser_item, parent, false);
            holder = new ViewHolder();
            holder.QuestionName = (TextView) v.findViewById(R.id.txtQuestion);
            holder.radioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);
            holder.rd1 = (RadioButton) v.findViewById(R.id.rd1);
            holder.rd1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    mListDealer.get(position).setSelected(1);
                    notifyDataSetChanged();
                    String[] questionoption = mListDealer.get(position).getQuestionType().split("@@");
                    String[] s = questionoption[0].split(";");
                    mListDealer.get(position).setQuestionAnswer(s[1]);
                    Log.e("tuyenpx", "tuyenpx : anwser " + s[1]);
                }
            });
            holder.rd2 = (RadioButton) v.findViewById(R.id.rd2);
            holder.rd2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    String[] questionoption = mListDealer.get(position).getQuestionType().split("@@");

                    mListDealer.get(position).setSelected(2);
                    notifyDataSetChanged();
                    String[] s = questionoption[1].split(";");
                    mListDealer.get(position).setQuestionAnswer(s[1]);
                    Log.e("tuyenpx", "tuyenpx : anwser " + s[1]);
                }
            });
            holder.rd4 = (RadioButton) v.findViewById(R.id.rd4);
            holder.rd4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] questionoption = mListDealer.get(position).getQuestionType().split("@@");
                    int getPosition = (Integer) v.getTag();
                    mListDealer.get(position).setSelected(4);
                    String[] s = questionoption[3].split(";");
                    mListDealer.get(position).setQuestionAnswer(s[1]);
                    Log.e("tuyenpx", "tuyenpx : anwser " + s[1]);
                    notifyDataSetChanged();
                }
            });
            holder.rd3 = (RadioButton) v.findViewById(R.id.rd3);
            holder.rd3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] questionoption = mListDealer.get(position).getQuestionType().split("@@");
                    String[] s = questionoption[2].split(";");
                    mListDealer.get(position).setQuestionAnswer(s[1]);
                    int getPosition = (Integer) v.getTag();
                    mListDealer.get(position).setSelected(3);
                    Log.e("tuyenpx", "tuyenpx : anwser " + s[1]);
                    notifyDataSetChanged();
                }
            });
            v.setTag(holder);
            v.setTag(R.id.txtQuestion, holder.QuestionName);
            v.setTag(R.id.radiogroup, holder.radioGroup);
            v.setTag(R.id.rd1, holder.rd1);
            v.setTag(R.id.rd2, holder.rd2);
            v.setTag(R.id.rd3, holder.rd3);
            v.setTag(R.id.rd4, holder.rd4);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.radioGroup.setTag(position);
        holder.rd1.setTag(position);
        holder.rd2.setTag(position);
        holder.rd3.setTag(position);
        holder.rd4.setTag(position);
        final QuestionObject serveyOject = mListDealer.get(position);
        holder.QuestionName.setText(serveyOject.getTEXTO_PREGUNTA());
        String[] questionoption = serveyOject.getQuestionType().split("@@");
        if (serveyOject.getQuestionAnswer().equals("0")) {
            String[] su = questionoption[0].split(";");
            serveyOject.setQuestionAnswer(su[1]);
        }


        switch (questionoption.length) {
            case 2:
                holder.rd1.setVisibility(View.VISIBLE);
                String[] s = questionoption[0].split(";");
                String[] s1 = questionoption[1].split(";");
                holder.rd1.setText(s[0]);
                holder.rd2.setText(s1[0]);
                holder.rd2.setVisibility(View.VISIBLE);
                holder.rd3.setVisibility(View.GONE);
                holder.rd4.setVisibility(View.GONE);
                break;
            case 3:
                holder.rd1.setVisibility(View.VISIBLE);
                String[] ss = questionoption[0].split(";");
                String[] ss1 = questionoption[1].split(";");
                String[] ss2 = questionoption[2].split(";");
                holder.rd1.setText(ss[0]);
                holder.rd2.setText(ss1[0]);
                holder.rd3.setText(ss2[0]);
                holder.rd2.setVisibility(View.VISIBLE);
                holder.rd3.setVisibility(View.VISIBLE);
                holder.rd4.setVisibility(View.GONE);
                break;
            case 4:
                String[] sss = questionoption[0].split(";");
                String[] sss1 = questionoption[1].split(";");
                String[] sss2 = questionoption[2].split(";");
                String[] sss3 = questionoption[3].split(";");
                holder.rd1.setText(sss[0]);
                holder.rd2.setText(sss1[0]);
                holder.rd3.setText(sss2[0]);
                holder.rd4.setText(sss3[0]);
                holder.rd1.setVisibility(View.VISIBLE);
                holder.rd2.setVisibility(View.VISIBLE);
                holder.rd3.setVisibility(View.VISIBLE);
                holder.rd4.setVisibility(View.VISIBLE);
                break;
        }

        int a = serveyOject.getSelected();
        switch (a) {
            case 1:
                holder.rd1.setChecked(true);
                break;
            case 2:
                holder.rd2.setChecked(true);
                break;
            case 3:
                holder.rd3.setChecked(true);
                break;
            case 4:
                holder.rd4.setChecked(true);
                break;
        }
        return v;
    }
}
