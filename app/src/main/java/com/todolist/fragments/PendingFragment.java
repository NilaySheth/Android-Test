package com.todolist.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import com.todolist.ExtraClasses.CommonMethods;
import com.todolist.R;
import com.todolist.adapter.TodoAdapter;
import com.todolist.model.Data;
import com.todolist.model.DataDescription;
import com.todolist.recycler.DividerItemDecoration;
import com.todolist.retrofit.ApiClient;
import com.todolist.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {

    private List<DataDescription> todoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TodoAdapter mAdapter;

    public PendingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.pending_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mAdapter = new TodoAdapter(todoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if(CommonMethods.isNetworkConnected(getActivity())){
            getTODOData();
        }else{
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_LONG)
//                    .setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
//                            snackbar1.show();
//                        }
//                    });
//
//            snackbar.show();
        }


        return view;
    }

    private void getTODOData() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Data> call = apiService.getTODOData();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                int statusCode = response.code();
                todoList = response.body().getResults();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failed RestClient", t.toString());
            }
        });
    }

    private void addTODOItem(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.googleWifi)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.connect)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showToast("Password: " + passwordInput.getText().toString());
                    }
                }).build();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem deleteMenu = menu.findItem(R.id.delete);
        deleteMenu.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:

                addTODOItem();
                return true;
            case R.id.delete:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}