package com.example.honghanh.bookstoremanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.honghanh.bookstoremanager.R;
import com.example.honghanh.bookstoremanager.database.SqlHelper;
import com.example.honghanh.bookstoremanager.views.MyButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewBookStore extends Fragment {

    @BindView(R.id.et_inputName)
    EditText inputName;
    @BindView(R.id.et_inputAddress)
    EditText inputAddress;
    @BindView(R.id.et_inputTelephone)
    EditText inputTelephone;
    @BindView(R.id.et_inputEmail)
    EditText inputEmail;
    @BindView(R.id.et_inputOverview)
    EditText inputOverview;
    @BindView(R.id.et_inputPosterLink)
    EditText inputPosterLink;
    @BindView(R.id.et_inputBigPosterLink)
    EditText inputBigPosterLink;
    @BindView(R.id.mybtn_insertData)
    MyButton btnInsertData;
    SqlHelper sqlHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_bookstore, container, false);
        ButterKnife.bind(this, view);


        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelper = new SqlHelper(getContext(), "Database.db", null, 1);
                sqlHelper.queryDataToBookstore();

                Cursor cursor = sqlHelper.getData("SELECT COUNT(*) FROM BOOKSTORE");
                cursor.moveToFirst();
                int count = cursor.getInt(0);

                int id = count + 1;

                String name = inputName.getText().toString();
                String address = inputAddress.getText().toString();
                String telephone = inputTelephone.getText().toString();
                String email = inputEmail.getText().toString();
                String overview = inputOverview.getText().toString();
                String posterLink = inputPosterLink.getText().toString();
                String bigPosterLink = inputBigPosterLink.getText().toString();

                if (name.matches("") | address.matches("") | posterLink.matches("") | bigPosterLink.matches("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("NOTI");
                    alertDialog.setMessage("Some value is null.");
                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                } else {
                    try {
                        sqlHelper.insertToBookstoreTable(Integer.toString(id), name, address, telephone, email, overview, posterLink, bigPosterLink);
                        Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Dont ....", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getContext(), name , Toast.LENGTH_LONG).show();
                }
            }

        });
        return view;
    }
}
