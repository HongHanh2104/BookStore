package com.example.honghanh.bookstoremanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.honghanh.bookstoremanager.R;
import com.example.honghanh.bookstoremanager.activities.MainActivity;
import com.example.honghanh.bookstoremanager.data.Bookstore;
import com.example.honghanh.bookstoremanager.database.SqlHelper;
import com.example.honghanh.bookstoremanager.fragment.FavoriteBookstore;
import com.example.honghanh.bookstoremanager.fragment.FindBookstores;
import com.example.honghanh.bookstoremanager.utils.ImageUtils;
import com.example.honghanh.bookstoremanager.activities.BookstoreDetailActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookstoreAdapter extends RecyclerView.Adapter<BookstoreAdapter.ViewHolder> {
    Context context;
    List<Bookstore> bookstoreList;
    Bitmap bitmap = null;
    SqlHelper sqlHelper;

    public BookstoreAdapter(Context context, List<Bookstore> bookstoreList){
        this.context = context;
        this.bookstoreList = bookstoreList;

    }

    private Context getContext(){
        return context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bookstore, viewGroup, false);
        v.setBackground(getContext().getDrawable(R.drawable.ic_item_transparent));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Bookstore bookstore = bookstoreList.get(position);

        holder.tv_name.setText(bookstore.getName());
        holder.tv_address.setText(bookstore.getAddress());
        holder.tv_telephone.setText(bookstore.getTelephone());
        holder.tv_email.setText(bookstore.getEmail());

        //holder.iv_BookstoreImage.setImageResource(R.drawable.nhanam);

        Picasso.get()
                .load(bookstore.getPosterPath())
                .into(holder.iv_BookstoreImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return bookstoreList.size();
    }


    public void insert(Bitmap bitmap, int position) {
        //bookstoreList.add(position, new Bookstore("ahihi", bitmap));
        notifyItemInserted(position);
    }

    public void remove(int position) {
        bookstoreList.remove(position);
        notifyDataSetChanged();
    }

    public void clearAll() {
        for (int i = 0; i < bookstoreList.size(); i++) {
            if (bookstoreList.get(i) != null) {}
            bookstoreList.remove(i);
        }
        bookstoreList.clear();
        //mCheck = 0;
        notifyDataSetChanged();
    }

    public void addAll(List<Bookstore> bookstores) {
        int startIndex = bookstores.size();
        this.bookstoreList.addAll(startIndex, bookstores);
        notifyItemRangeInserted(startIndex, bookstores.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.iv_BookstoreImage)
        ImageView iv_BookstoreImage;

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_telephone)
        TextView tv_telephone;
        @BindView(R.id.tv_email)
        TextView tv_email;
        @BindView(R.id.cv_bookstore)
        CardView cv_bookstore;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bookstore bookstore = bookstoreList.get(getAdapterPosition());
            //v.setBackgroundColor(Color.parseColor("#80FFFFFF"));
            Intent intent = new Intent(getContext(), BookstoreDetailActivity.class);
            intent.putExtra("BOOKSTORE", bookstore);
            getContext().startActivity(intent);
        }


        @Override
        public boolean onLongClick(View v) {
            CharSequence[] items = {"Update"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Choose an action");
            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.setPositiveButton("UNLIKE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getContext(), "Unlike?", Toast.LENGTH_SHORT).show();
                    sqlHelper = new SqlHelper(getContext(), "Database.db", null, 1);
                    sqlHelper.queryDataToBookstore();
                    String idSearch = bookstoreList.get(getAdapterPosition()).getId();
                    Cursor cursor = sqlHelper.getData("SELECT * FROM BOOKSTORE WHERE id = '" + idSearch + "'");
                    try{
                        while(cursor.moveToNext()){
                            int likebookstore = cursor.getInt(8);
                            if(likebookstore == 1) {
                                showDialogUpdate(bookstoreList.get(getAdapterPosition()).getId());
                            }
                            else {
                                final AlertDialog.Builder dialogNoti = new AlertDialog.Builder(getContext());
                                dialogNoti.setTitle("NOTIFICATION !!!");
                                dialogNoti.setMessage("You can not unlike it because you are not used to like it -.-");
                                dialogNoti.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog showDialog = dialogNoti.create();
                                showDialog.show();
                            }
                        }

                    }
                    catch (Exception e){
                        Log.e("Selection Error ", e.getMessage());
                    }


                }
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

            return true;
        }

        private void showDialogUpdate(String id){
            final String idUpdate = id;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("UNLIKE BOOKSTORE");
            alertDialog.setMessage("Are you sure you want to unlike this bookstore? ~^00^~");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sqlHelper = new SqlHelper(getContext(), "Database.db", null, 1);
                    sqlHelper.queryDataToBookstore();

                    try {
                        sqlHelper.updateOneValue(idUpdate, 0);
                        Toast.makeText(getContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Update error", e.getMessage());
                    }
                }
            });
            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();


        }
    }
}
