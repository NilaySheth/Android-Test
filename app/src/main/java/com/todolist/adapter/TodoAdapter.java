package com.todolist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.interfece.ShowDeleteMenu;
import com.todolist.model.DataDescription;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TODOHolder> {

    List<DataDescription> dataList;
    private boolean isCheckBoxShow = false;
    private ShowDeleteMenu callbackDelete;

    public class TODOHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox cb;

        public TODOHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            cb = (CheckBox) view.findViewById(R.id.checkbox);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!isCheckBoxShow){
                        isCheckBoxShow = true;
                        notifyDataSetChanged();
                        callbackDelete.showDeleteMenu();
                    }
                    return false;
                }
            });
        }
    }

    public TodoAdapter(List<DataDescription> dataList, ShowDeleteMenu callbackDelete) {
        this.dataList = dataList;
        this.callbackDelete = callbackDelete;
    }

    @Override
    public TODOHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new TODOHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TODOHolder holder, final int position) {
        DataDescription dataDesc = dataList.get(position);
        holder.title.setText(dataDesc.getName());

        if(isCheckBoxShow)
            holder.cb.setVisibility(View.VISIBLE);
        else
            holder.cb.setVisibility(View.GONE);

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataList.get(position).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void disableCheckBox(){
        isCheckBoxShow = false;
        notifyDataSetChanged();
    }
}