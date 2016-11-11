package com.todolist.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import com.afollestad.materialdialogs.MaterialDialog;
import com.todolist.Config.CommonMethods;
import com.todolist.R;
import com.todolist.fragments.DoneFragment;
import com.todolist.fragments.PendingFragment;
import com.todolist.interfece.CallWebService;
import com.todolist.model.Data;
import com.todolist.model.DataDescription;
import com.todolist.retrofit.ApiClient;
import com.todolist.retrofit.ApiInterface;
import com.todolist.sqlite.DbTODOList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TODOActivity extends AppCompatActivity implements CallWebService {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DbTODOList dbTODOList;
    private List<DataDescription> todoList = new ArrayList<>();
    private MaterialDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.root_coordinatelayout);
        dbTODOList = new DbTODOList(getApplicationContext());

        fetchAllTODOList();
    }

    private void setTabbarUI(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 :
                        ((PendingFragment)adapter.getItem(position)).updateData();
                        break;
                    case 1 :
                        ((DoneFragment)adapter.getItem(position)).updateData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void fetchAllTODOList(){
        if(CommonMethods.isNetworkConnected(getApplicationContext())){
            showIndeterminateProgressDialog();
            getTODOData();
        }else{
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry_connection), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fetchAllTODOList();
                        }
                    });

            snackbar.show();
        }
    }

    @Override
    public void callService() {
        fetchAllTODOList();
    }

    private void getTODOData() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Data> call = apiService.getTODOData();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                hideIndeterminateProgressDialog();
                int statusCode = response.code();
                todoList = response.body().getResults();
                dbTODOList.deleteAllTODO();
                dbTODOList.addAllTODO(todoList);

                setTabbarUI();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failed RestClient", t.toString());
            }
        });
    }

    private void showIndeterminateProgressDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progres_fetch_todo)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    private void hideIndeterminateProgressDialog() {
        progressDialog.dismiss();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingFragment(this), getString(R.string.pending));
        adapter.addFragment(new DoneFragment(this), getString(R.string.done));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
