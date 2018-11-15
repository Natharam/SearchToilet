package com.example.natharam.searchtoilet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ReViewFragment extends Fragment implements View.OnClickListener {
    String s;
    public DatabaseReference mDatabaseReference;
    static EditText editComment;
    Button  addButton;
    static RatingBar ratingBar;
    RecyclerView mRecyleryView;
    LayoutInflater mLayoutInflater;
    LinearLayoutManager linearLayoutManager;
    Context context;
    static double aFloat=  0.0;
    ArrayList<CommentAdd> commentAddsList;
    TextView textViewaaa;
    float a;String[] strings;
    public String idChilds;
    public ReViewFragment(ReViewActivity reViewActivity, String idChild) {
        // Required empty public constructor
        idChilds=idChild;
        context=reViewActivity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayoutInflater=inflater;

        View view=inflater.inflate(R.layout.fragment_re_view, container, false);
        addButton=view.findViewById(R.id.button);
        addButton.setOnClickListener(this);
        commentAddsList=new ArrayList<>();
        editComment=view.findViewById(R.id.editComments);
        ratingBar=view.findViewById(R.id.ratingBar2);
        mRecyleryView=view.findViewById(R.id.recyclercommen);
        textViewaaa=view.findViewById(R.id.textView7);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference(idChilds);



        mRecyleryView.setLayoutManager( new LinearLayoutManager(getContext()));



        return view;




    }

    public void recylery(){

        mRecyleryView.setAdapter(new SimpleRVAdapters(context,commentAddsList));

    }




    public void getAvere(){

        DecimalFormat as=new DecimalFormat("#.#");
        String aFloata=as.format(aFloat);
        textViewaaa.setText(aFloata);
    }


    @Override
    public void onClick(View v) {

        String s=editComment.getText().toString();
        String  a= String.valueOf(ratingBar.getRating());
        String id =mDatabaseReference.push().getKey();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" dd-MM-yyyy HH:mm ");
        String timestamp=simpleDateFormat.format(calendar.getTime());

        CommentAdd commentAdd=new CommentAdd(s,a,timestamp);

        mDatabaseReference.child(id).setValue(commentAdd);

        ReViewActivity reViewActivity=ReViewActivity.getReViewActivity();


        reViewActivity.finishs();

        //Toast.makeText(context,"erwewerdsfsdf",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onStart() {
        super.onStart();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aFloat=  0.0;
                int count=0 ;
                commentAddsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    CommentAdd commentAdd=dataSnapshot1.getValue(CommentAdd.class);

                    double a= Float.parseFloat(commentAdd.getRating());
                    aFloat=aFloat+a;
                    count++;

                    commentAddsList.add(commentAdd);

                }


                aFloat=aFloat/count;
                recylery();
                getAvere();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class SimpleRVAdapters extends RecyclerView.Adapter<SimpleViewHolders> {
        public ArrayList<CommentAdd> listDataa;
        Context contexts;
        public SimpleRVAdapters(Context context, ArrayList<CommentAdd> listData){
            listDataa = listData;
            contexts=context;
        }

        @Override
        public SimpleViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;// = new TextView(parent.getContext());
            try {
                // LayoutInflater inflater = LayoutInflater.from(contexts);
                view = LayoutInflater.from(contexts).inflate(R.layout.commen , parent , false);


            }catch (Exception e){}
            SimpleViewHolders viewHolder = new SimpleViewHolders(view,contexts);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final SimpleViewHolders holder, final int position) {

            holder.textStar.setText( listDataa.get(position).getRating());
            holder.textComment.setText(listDataa.get(position).getComment());
            holder.textDaTe.setText(listDataa.get(position).getTimestamp());
            //  holder.city.setText(listDataa.get(position).getAddressLocat());



        }



        @Override
        public int getItemCount() {
            return listDataa.size();
        }
    }



    public class SimpleViewHolders extends RecyclerView.ViewHolder  {
        public TextView textView;
        ImageView imageView;
        TextView textComment,textStar,textDaTe;

        public Context contexts;




        public SimpleViewHolders(View itemView,Context context) {
            super(itemView);
            contexts=context;
            textComment=itemView.findViewById(R.id.textCommen);
            textStar=itemView.findViewById(R.id.textView6);
            textDaTe=itemView.findViewById(R.id.textTimesta);


        }

    }
}



