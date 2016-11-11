package com.todolist.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.interfece.AdapterCallback;
import com.todolist.model.DataDescription;
import com.todolist.sqlite.DbTODOList;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TODOHolder> {

    List<DataDescription> dataList;
    private boolean isCheckBoxShow = false;
    private AdapterCallback callbackAdapter;
    private DbTODOList dbTODOList;

    public TodoAdapter(Context context, List<DataDescription> dataList, AdapterCallback callbackDelete) {
        this.dataList = dataList;
        this.callbackAdapter = callbackDelete;
        dbTODOList = new DbTODOList(context);
    }

    @Override
    public TODOHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new TODOHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TODOHolder holder, final int position) {
        DataDescription dataDesc = dataList.get(position);
        holder.title.setText(dataDesc.getName());

        final Handler handler = new Handler();
        final Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                holder.cancelTransition.setVisibility(View.GONE);
                dbTODOList.updateTODO(dataList.get(position).getId(), dataList.get(position).getState() == 0 ? 1 : 0 );
                callbackAdapter.refreshFragment();
            }
        };

        if (isCheckBoxShow)
            holder.cb.setVisibility(View.VISIBLE);
        else
            holder.cb.setVisibility(View.GONE);

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataList.get(position).setSelected(isChecked);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Recycler Clicked", String.valueOf(position));
                holder.cancelTransition.setVisibility(View.VISIBLE);
                handler.postDelayed(runnableCode, 5000);
            }
        });

        holder.cancelTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cancelTransition.setVisibility(View.GONE);
                handler.removeCallbacks(runnableCode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void disableCheckBox() {
        isCheckBoxShow = false;
        notifyDataSetChanged();
    }

    public class TODOHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox cb;
        public Button cancelTransition;

        public TODOHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            cb = (CheckBox) view.findViewById(R.id.checkbox);
            cancelTransition = (Button) view.findViewById(R.id.cancel_transition);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isCheckBoxShow) {
                        isCheckBoxShow = true;
                        notifyDataSetChanged();
                        callbackAdapter.showDeleteMenu();
                    }
                    return false;
                }
            });
        }
    }
}