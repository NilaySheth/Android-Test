package com.todolist.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.todolist.R;
import com.todolist.activity.TODOActivity;
import com.todolist.adapter.TodoAdapter;
import com.todolist.interfaces.AdapterCallback;
import com.todolist.model.DataDescription;
import com.todolist.recycler.DividerItemDecoration;
import com.todolist.sqlite.DbTODOList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PendingFragment extends Fragment implements AdapterCallback{

    List<DataDescription> todoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TodoAdapter mAdapter;
    private EditText entryInput;
    private DbTODOList dbTODOList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isDeleteMenuShow = false;

    public PendingFragment() {
    }

//    public PendingFragment(CallWebService callback) {
//        this.callback = callback;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        recyclerView = (RecyclerView) view.findViewById(R.id.pending_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        dbTODOList = new DbTODOList(getActivity());
        todoList = dbTODOList.getFilteredTODO(0);

        mAdapter = new TodoAdapter(getActivity(), todoList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isDeleteMenuShow = false;
                getActivity().invalidateOptionsMenu();
                ((TODOActivity)getActivity()).callService();
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void addTODOItem() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_todo_string)
                .customView(R.layout.dialog_add_todo, true)
                .positiveText(R.string.add_todo_positive)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i("tag", entryInput.getText().toString());
                        DataDescription newEntry = new DataDescription(new Random().nextInt(100), entryInput.getText().toString().trim(), 0, false);
                        dbTODOList.addTODO(newEntry);
                        todoList.add(newEntry);
                        mAdapter.notifyDataSetChanged();
                    }

                }).build();
        entryInput = (EditText) dialog.getCustomView().findViewById(R.id.todoItem);
        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:

                addTODOItem();
                return true;
            case R.id.delete:
                ArrayList<DataDescription> tempList = new ArrayList<>(todoList);
                for (DataDescription data : tempList) {
                    if (data.getSelected()) {
                        dbTODOList.deleteTODO(data.getId());
                        todoList.remove(data);
                    }
                }
                mAdapter.disableCheckBox();
                isDeleteMenuShow = false;
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(isDeleteMenuShow){
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.add).setVisible(false);
        }else{
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.add).setVisible(true);
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void showDeleteMenu() {
        isDeleteMenuShow = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void updateData(){
        dbTODOList = new DbTODOList(getActivity());
        todoList.clear();
        todoList.addAll(dbTODOList.getFilteredTODO(0));
        mAdapter.notifyDataSetChanged();
    }
}