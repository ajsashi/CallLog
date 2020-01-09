package com.chooloo.www.callmanager.proof.aitech.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.model.CallLog;

import java.util.ArrayList;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private ArrayList<CallLog> callLogArrayList;

    public CallLogAdapter(ArrayList<CallLog> callLogArrayList) {
        this.callLogArrayList = callLogArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.call_log_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLog log = callLogArrayList.get(position);
        if (!TextUtils.isEmpty(log.getName())) {
            holder.txt_name.setText(log.getName());
        }
        holder.img_call_type.setImageResource(getCallType(log.getCallType()));
        holder.txt_number.setText(log.getNumber());
        holder.txt_time.setText("Duration: "+log.getCallDuration());
        holder.txt_call_time.setText(log.getCallTime());
        holder.txt_call_date.setText(log.getCallDayTime());
    }

    @Override
    public int getItemCount() {
        return callLogArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_call_type;
        TextView txt_name, txt_number, txt_time, txt_call_time, txt_call_date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_call_type = itemView.findViewById(R.id.img_call_type);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_number = itemView.findViewById(R.id.txt_number);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_call_time = itemView.findViewById(R.id.txt_call_time);
            txt_call_date = itemView.findViewById(R.id.txt_call_date);

        }
    }

    public int getCallType(String type) {
        int draw = 0;
        switch (type) {
            case "MISSED":
                draw = R.drawable.ic_call_missed_black_24dp;
                break;
            case "INCOMING":
                draw = R.drawable.ic_call_received_black_24dp;
                break;
            case "OUTGOING":
                draw = R.drawable.ic_call_made_black_24dp;
                break;
        }
        return draw;
    }
}
