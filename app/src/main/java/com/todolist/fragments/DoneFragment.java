package com.todolist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.R;
import com.todolist.adapter.TodoAdapter;
import com.todolist.interfece.AdapterCallback;
import com.todolist.interfece.CallWebService;
import com.todolist.model.DataDescription;
import com.todolist.recycler.DividerItemDecoration;
import com.todolist.sqlite.DbTODOList;

import java.util.ArrayList;
import java.util.List;

public class DoneFragment extends Fragment implements AdapterCallback {

    List<DataDescription> todoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TodoAdapter mAdapter;
    private DbTODOList dbTODOList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CallWebService callback;
    private boolean isDeleteMenuShow = false;

    public DoneFragment() {
    }

    public DoneFragment(CallWebService callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.pending_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        dbTODOList = new DbTODOList(getActivity());
        todoList = dbTODOList.getFilteredTODO(1);

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
                callback.callService();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
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
        menu.findItem(R.id.add).setVisible(false);
        if (isDeleteMenuShow)
            menu.findItem(R.id.delete).setVisible(true);
        else
            menu.findItem(R.id.delete).setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void showDeleteMenu() {
        isDeleteMenuShow = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void refreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void updateData(){
        dbTODOList = new DbTODOList(getActivity());
        todoList.clear();
        todoList.addAll(dbTODOList.getFilteredTODO(1));
        mAdapter.notifyDataSetChanged();
    }
}
