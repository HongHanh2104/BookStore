package com.example.honghanh.bookstoremanager.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.honghanh.bookstoremanager.R;
import com.example.honghanh.bookstoremanager.data.Bookstore;
import com.example.honghanh.bookstoremanager.database.SqlHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookstoreDetailActivity extends AppCompatActivity {
    Bookstore bookstore;
    @BindView(R.id.iv_bigPosterImage)
    ImageView iv_bigPosterImage;
    @BindView(R.id.tv_overview)
    TextView tv_overview;
    String destinationAddress;
    SqlHelper sqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookstore_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookstore = (Bookstore) extras.getSerializable("BOOKSTORE");
            this.setTitle(bookstore.getName());
            tv_overview.setText(bookstore.getOverview());
            destinationAddress = bookstore.getAddress();

            Picasso.get()
                    .load(bookstore.getBigPosterPath())
                    .into(iv_bigPosterImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    });

        }

        FloatingActionButton btnGPS = findViewById(R.id.btn_GPS);
        btnGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationAddress);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        FloatingActionButton btnLike = findViewById(R.id.btn_like);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Bookstore saved as favorite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                sqlHelper = new SqlHelper(getApplicationContext(), "Database.db", null, 1);
                sqlHelper.queryDataToBookstore();


                try {
                    sqlHelper.updateOneValue(bookstore.getId(), 1);
                    Toast.makeText(getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Update error", e.getMessage());
                }

            }
        });

        FloatingActionButton btnCall = findViewById(R.id.btn_Call);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + bookstore.getTelephone()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, đi với mình đi ~~~~ \n" +
                        bookstore.getName() + "\n nó ở đây nè " + bookstore.getAddress() + "\n hình nè " +
                bookstore.getPosterPath());
                startActivity(shareIntent);
            }
        });

    }
}
