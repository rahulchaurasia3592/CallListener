package com.rahul.calllistener.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rahul.calllistener.R;
import com.rahul.calllistener.database.CallEntryDataSource;
import com.rahul.calllistener.model.CallEntry;

import java.util.List;

/**
 * Main dashboard activity to show the incoming call entries from the database if any otherwise
 * show a message saying no entries have found.
 *
 */
public class DashboardActivity extends AppCompatActivity {

    private CallEntryDataSource mCallEntryDataSource;
    private ListView listView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview);
        emptyView  = (TextView) findViewById(android.R.id.empty);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Create the incoming data source
        mCallEntryDataSource = new CallEntryDataSource(getApplicationContext());
        mCallEntryDataSource.open();

        List<CallEntry> values = mCallEntryDataSource.getAllCallEntries();

        // Set the adapter to the listview to display the call entries
        ArrayAdapter<CallEntry> adapter = new ArrayAdapter<CallEntry>(this,
                android.R.layout.simple_list_item_1, values);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCallEntryDataSource.close();
    }
}
