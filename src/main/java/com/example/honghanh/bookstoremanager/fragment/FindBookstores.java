package com.example.honghanh.bookstoremanager.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.honghanh.bookstoremanager.R;
import com.example.honghanh.bookstoremanager.activities.MainActivity;
import com.example.honghanh.bookstoremanager.adapter.BookstoreAdapter;
import com.example.honghanh.bookstoremanager.data.Bookstore;
import com.example.honghanh.bookstoremanager.database.SqlHelper;
import com.example.honghanh.bookstoremanager.utils.ImageUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindBookstores extends Fragment{
    @BindView(R.id.rv_bookstore)
    RecyclerView recyclerViewBookStore;
    List<Bookstore> bookstoreList;
    List<Bookstore> bookstoreRecyclerList;
    BookstoreAdapter adapter;
    SqlHelper sqlHelper;
    public FindBookstores(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_bookstore, container, false);
        ButterKnife.bind(this, view);
        bookstoreRecyclerList = new ArrayList<>();

        initializeData();
        insertDataToDB();
        bookstoreRecyclerList.clear();


        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        recyclerViewBookStore.setHasFixedSize(true);
        recyclerViewBookStore.setLayoutManager(llm);

        adapter = new BookstoreAdapter(getContext(), bookstoreRecyclerList);
        recyclerViewBookStore.setAdapter(adapter);
        loadDataToRecyver();
        setHasOptionsMenu(true);
        return view;
    }

    private void loadDataToRecyver() {
        SQLiteDatabase sqLiteDatabase = sqlHelper.getReadableDatabase();
        bookstoreRecyclerList.clear();
        adapter.clearAll();
        // Load data
        Cursor cursor = sqlHelper.getData("SELECT * FROM BOOKSTORE");
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String telephone = cursor.getString(3);
            String email = cursor.getString(4);
            String overview = cursor.getString(5);
            String posterPath = cursor.getString(6);
            String bigPosterPath = cursor.getString(7);
            //int liked = cursor.getInt(8);
            //if(liked == 1) btn_liked.setVisibility(View.VISIBLE);
            bookstoreRecyclerList.add(new Bookstore(id, name, address, telephone, email, overview, posterPath, bigPosterPath));


        }


    }

    private void searchData(String substring){
        //sqlHelper = new SqlHelper(getContext(), "Database.db", null, 1);
        //sqlHelper.queryDataToBookstore();
        bookstoreRecyclerList.clear();
        adapter.clearAll();
        // Load data
        Cursor cursor = sqlHelper.getData("SELECT * FROM BOOKSTORE WHERE name LIKE '%" + substring + "%'");
        try{
            while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String telephone = cursor.getString(3);
            String email = cursor.getString(4);
            String overview = cursor.getString(5);
            String posterPath = cursor.getString(6);
            String bigPosterPath = cursor.getString(7);

            bookstoreRecyclerList.add(new Bookstore(id, name, address, telephone, email, overview, posterPath, bigPosterPath));

            }
        } catch (Exception e){
            Log.e("ERROR", e.getMessage());
            Toast.makeText(getContext(), "Cannot Search", Toast.LENGTH_SHORT).show();
        }

    }



    private void insertDataToDB() {
        sqlHelper = new SqlHelper(getContext(), "Database.db", null, 1);
        sqlHelper.queryDataToBookstore();

        SQLiteDatabase sqLiteDatabase = sqlHelper.getWritableDatabase();
        Cursor cursor = sqlHelper.getData("SELECT COUNT(*) FROM BOOKSTORE");
        cursor.moveToFirst();
        int count = cursor.getInt(0);

        if(count == 0) {

            for (int i = 0; i < bookstoreList.size(); i++) {
                String id = bookstoreList.get(i).getId();
                String name = bookstoreList.get(i).getName();
                String address = bookstoreList.get(i).getAddress();
                String telephone = bookstoreList.get(i).getTelephone();
                String email = bookstoreList.get(i).getEmail();
                String overview = bookstoreList.get(i).getOverview();
                String image = bookstoreList.get(i).getPosterPath();
                String bigPoster = bookstoreList.get(i).getBigPosterPath();
                try {
                    sqlHelper.insertToBookstoreTable(id, name, address, telephone, email, overview, image, bigPoster);
                    //Toast.makeText(getContext(), "Successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getContext(), "Dont ....", Toast.LENGTH_LONG).show();
                }
            }
            //Toast.makeText(getContext(), "Insert Again", Toast.LENGTH_SHORT).show();
        }
        else {

        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.ic_favorite);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = new SearchView(getActivity());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //searchData(s);
                return true;
            }
        });
        item.setActionView(searchView);
    }

    private void initializeData() {

        bookstoreList = new ArrayList<>();
        bookstoreList.add(new Bookstore("1", "Nhã Nam Thư Quán","015, Chung Cư 43 Hồ Văn Huê, Phường 9, Phú Nhuận, Hồ Chí Minh", "0908427318",
                "hcm@nhanam.vn", "Không gian cà phê sách này mang đến cho những ai ghé chân cảm giác yên tĩnh, thoáng mát. Tránh xa những ồn ào xe cộ, " +
                "Nhã Nam thư quán nằm trong khu chung cư trên đường Hồ Văn Huê, quận Phú Nhuận, TP HCM. Được xây dựng từ năm 2011,đến nay tiệm cà phê sách này đã trở thành " +
                "điểm hẹn của những người yêu sách và muốn có không gian đọc thoải mái. ", "https://i.pinimg.com/564x/26/5b/f6/265bf6e9e76096ab7b87659994de04eb.jpg",
                "https://i.pinimg.com/564x/b6/7c/f8/b67cf8d021bcaf9f70467dd4349ed693.jpg" ));

        bookstoreList.add(new Bookstore("2", "Nhà Sách Cá Chép", "211 - 213 Võ Văn Tần, Quận 3, Tp. Hồ Chí Minh", "0862906951",
                "nhasachcachep@gmail.com", "Tọa lạc tại khu trung tâm của thành phố, Nhà sách Cá Chép - trực thuộc công ty Sách và Lịch Đại Nam, " +
                "chính thức hoạt động từ tháng 12/2013. Đây không chỉ là điểm dừng chân mua sắm thuận tiện, mà còn là một không gian đẹp và thân thiện để tôn " +
                "vinh sách với nhiều sản phẩm, dịch vụ tích hợp. ", "https://i.pinimg.com/564x/84/f2/75/84f275cd10f46ff4de696ba424203745.jpg",
                "https://i.pinimg.com/564x/aa/4c/0e/aa4c0e38faa243829e83e0dce2a69091.jpg"));

        bookstoreList.add(new Bookstore("3", "Nhà Sách Kim Đồng", "248 Cống Quỳnh, Quận 1, Tp. Hồ Chí Minh", "02839250170", "info@nxbkimdong.com.vn",
                "Mặc dù chỉ mới được khai trương cách đây chưa đầy một tháng, nhưng hiện nhà sách mới nằm ở số 248 Cống Quỳnh, quận 1 này lại trở thành một trong những điểm đến cực kỳ" +
                        " \"hot\" của các bạn trẻ Sài Gòn. Nguyên do một phần là vì nơi đây hầu như được bày bán toàn bộ những ấn phẩm đặc biệt của các bộ truyện \"huyền thoại\" do Kim Đồng xuất bản.",
                "https://i.pinimg.com/564x/0c/2b/85/0c2b85890470b9a15b2dbd3d10066638.jpg", "https://i.pinimg.com/564x/3b/fc/50/3bfc50898ea955d5df59b06f27969c32.jpg"));

        bookstoreList.add(new Bookstore("4", "Nhà Sách FAHASA", "60-62 Lê Lợi, Quận 1, Tp. Hồ Chí Minh", "1900636467", "info@fahasa.com",
                "Điểm đáng tin cậy mua sách trực tuyến với giá luôn ưu đãi kinh nghiệm gần 40 năm. Nhà sách Uy tín. Giao hàng toàn quốc. Chất Lượng Sách Tốt. Nhà sách hàng đầu. Giao Hàng Nhanh.",
                "https://i.pinimg.com/564x/83/40/9d/83409d1b46c24d0bcb3066a329d3a285.jpg","https://i.pinimg.com/564x/8e/2f/2b/8e2f2b870a9b40813f7c97d0b5196d64.jpg"));

        bookstoreList.add(new Bookstore("5", "Nhà Sách Phương Nam", "Vạn Hạnh Mall, 11 Sư Vạn Hạnh, Phường 12, Quận 10, Hồ Chí Minh", "19006656", "info@NhaSachPhuongNam.com",
                "Nhasachphuongnam.com ra đời vào tháng 12/2010 với hình thức kinh doanh Mua hàng online – Giao hàng tận nơi. Với mô hình nhà sách trên mạng, cho phép độc giả dễ dàng tìm sách & mua sách online. " +
                        "Đến nay nhasachphuongnam.com đã có hơn 70,000 đầu sách phục vụ cho độc giả cả nước", "https://i.pinimg.com/564x/4e/3a/de/4e3ade95ef84d73e7aa3b6d92e10f7f5.jpg",
                "https://i.pinimg.com/236x/9a/6a/97/9a6a97e247cf8e7dcbf6931f3b73a603.jpg"));


    }


}
