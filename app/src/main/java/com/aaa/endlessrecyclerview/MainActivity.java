package com.aaa.endlessrecyclerview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.aaa.endlessrecyclerview.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TwoWayEndlessAdapter.Callback, SwipeRefreshLayout.OnRefreshListener {

    private boolean isAtTop = true;

    private TwoWayEndlessAdapterImp<ItemLayoutViewHolder> endlessAdapter = null;

    private DummyDataLoader dataLoader = null;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        refreshLayout = findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        endlessAdapter = new TwoWayEndlessAdapterImp<>();
        endlessAdapter.setDataContainer(new ArrayList<ValueItem>());
        endlessAdapter.addItemsAtBottom(Utils.generateDummyItemsList(15));

        endlessAdapter.setEndlessCallback(this);

        recyclerView.setAdapter(endlessAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTopReached()
    {
        isAtTop = true;
    }

    @Override
    public void onBottomReached()
    {
        isAtTop = false;

        if (dataLoader == null) {
            dataLoader = new DummyDataLoader();
            dataLoader.setDataLoaderCallback(bottomResponseCallback);
            dataLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private ResponseCallback bottomResponseCallback = new ResponseCallback() {
        @Override
        public void onResponse(Object result)
        {
            if (dataLoader != null) {
                dataLoader.cancel(true);
                dataLoader = null;
            }

            if (result instanceof Integer) {
                endlessAdapter.addItemsAtBottom(Utils.generateDummyItemsList((Integer) result));
            }
        }
    };

    private ResponseCallback swipeResponseCallback = new ResponseCallback() {
        @Override
        public void onResponse(Object result)
        {
            refreshLayout.setRefreshing(false);

            if (dataLoader != null) {
                dataLoader.cancel(true);
                dataLoader = null;
            }

            if (result instanceof Integer) {
                endlessAdapter.addItemsAtTop(Utils.generateDummyItemsList((Integer) result));
            }
        }
    };

    @Override
    public void onRefresh()
    {
        if (dataLoader != null) {
            dataLoader.cancel(true);
            dataLoader = null;
        }

        dataLoader = new DummyDataLoader();
        dataLoader.setDataLoaderCallback(swipeResponseCallback);
        dataLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
