package com.example.anandu_sem2_final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.anandu_sem2_final_project.Database.AppDatabase;
import com.example.anandu_sem2_final_project.Database.UserData;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment implements CallbackDelegate {

    private AppDatabase db;
    private List<UserData> userDataList, filteredList;
    private UserData selectedUserdata = null;
    private MyItemRecyclerViewAdapter adaptor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        db = AppDatabase.getDbInstance(getContext());
        userDataList = db.userDao().getAllData();
        Log.i("anandu", "Read data size: " + userDataList.size());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adaptor = new MyItemRecyclerViewAdapter(this, userDataList);
            recyclerView.setAdapter(adaptor);
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userDataList = db.userDao().getAllData();
        Log.i("anandu", "Read data size: " + userDataList.size());
        adaptor.setmValues(userDataList);
    }

    @Override
    public void callback(View v, int position) {
        selectedUserdata = userDataList.get(position);
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra("SelectedItem", selectedUserdata);
        startActivity(intent);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.i("anandu", "searched in list " + query);

                if (query.isEmpty()) {
                    adaptor.setmValues(userDataList);
                } else {
                    Predicate<UserData> bynameOrCountry = userData ->
                            userData.getName().toLowerCase().contains(query.toLowerCase())
                                    || userData.getCountry().toLowerCase().contains(query.toLowerCase());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        filteredList = userDataList.stream().filter(bynameOrCountry).collect(Collectors.toList());
                    }
                    adaptor.setmValues(filteredList);
                }
                return false;
            }
        });
    }
}