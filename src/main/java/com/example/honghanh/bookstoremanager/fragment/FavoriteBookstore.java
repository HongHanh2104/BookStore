package com.example.honghanh.bookstoremanager.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.honghanh.bookstoremanager.R;
import com.example.honghanh.bookstoremanager.adapter.BookstoreAdapter;
import com.example.honghanh.bookstoremanager.data.Bookstore;
import com.example.honghanh.bookstoremanager.database.SqlHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteBookstore extends Fragment {
    @BindView(R.id.rv_favoriteList)
    RecyclerView recyclerViewFavoriteBookstore;
    List<Bookstore> favoritedList;
    BookstoreAdapter adapter;
    SqlHelper sqlHelper;

    public FavoriteBookstore(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_bookstore, container, false);
        ButterKnife.bind(this, view);
        favoritedList = new ArrayList<>();

        //initializeData();
        //insertDataToDB();
        favoritedList.clear();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewFavoriteBookstore.setHasFixedSize(true);
        recyclerViewFavoriteBookstore.setLayoutManager(linearLayoutManager);

        adapter = new BookstoreAdapter(getContext(), favoritedList);
        recyclerViewFavoriteBookstore.setAdapter(adapter);
        loadDataToRecyver();

        return view;
    }

    private void loadDataToRecyver() {
        sqlHelper = new SqlHelper(getContext(), "Database.db", null, 1);
        sqlHelper.queryDataToBookstore();
        favoritedList.clear();
        adapter.clearAll();
        // Load data
        int value = 1;
        Cursor cursor = sqlHelper.getData("SELECT * FROM BOOKSTORE WHERE likebookstore = '" + value + "'");
        if(cursor == null) Toast.makeText(getContext(), "Not value", Toast.LENGTH_SHORT).show();
        try {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                String telephone = cursor.getString(3);
                String email = cursor.getString(4);
                String overview = cursor.getString(5);
                String posterPath = cursor.getString(6);
                String bigPosterPath = cursor.getString(7);

                favoritedList.add(new Bookstore(id, name, address, telephone, email, overview, posterPath, bigPosterPath));
                String size = Integer.toString(favoritedList.size());
                //Toast.makeText(getContext(), size, Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Log.e("Select error", e.getMessage());
        }


    }
}
