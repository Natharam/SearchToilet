package com.example.natharam.searchtoilet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 .
 */
@SuppressLint("ValidFragment")
public class ListVi extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private static final String TAG = "submit";

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private String mLastUpdateTime;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutmanager;
    DividerItemDecoration dividerItemDecoration;
    RecyclerView.Adapter mAdapter;
    //  UserInformation userInfo;
    LayoutInflater layoutInflaterw;
    public  Context contexts;
    ArrayList<ToiletRetData> listToilet =new ArrayList<>();;
    ArrayList<String> stringArrayList=new ArrayList<>();
    ArrayList<Double> stringDistance=new ArrayList<>();
    DatabaseReference databaseReference;
    @SuppressLint("ValidFragment")
    public ListVi(Context context, ArrayList<ToiletRetData> toiletRetData, ArrayList<String> stringArrayList, ArrayList<Double> stringDistance) {
        contexts=context;
        this.stringArrayList=stringArrayList;
        this.stringDistance=stringDistance;
        listToilet=toiletRetData;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_vi, container, false);
        layoutInflaterw =inflater;

        //databaseReference=FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //Toast.makeText(contexts,"ssdksdsdkfjkdskasksda",Toast.LENGTH_SHORT).show();


        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleRVAdapter(contexts,listToilet,stringArrayList));

        return view;
    }



    public class SimpleRVAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
        public ArrayList<ToiletRetData> listDataa;
        ArrayList<String> stringArrayList;
        public SimpleRVAdapter(Context context, ArrayList<ToiletRetData> listData, ArrayList<String> stringArrayList){
            listDataa = listData;
            this.stringArrayList=stringArrayList;
            contexts=context;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;// = new TextView(parent.getContext());
            try {
                // LayoutInflater inflater = LayoutInflater.from(contexts);
                view = LayoutInflater.from(contexts).inflate(R.layout.list_item , parent , false);


            }catch (Exception e){}
            SimpleViewHolder viewHolder = new SimpleViewHolder(view,contexts,listDataa);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
            DecimalFormat decimalFormat=new DecimalFormat("#.#");

            holder.txtname.setText( listDataa.get(position).getNameOfToilet());
           holder.txtaddress.setText(listDataa.get(position).getModOfToilet());
            holder.cost.setText("Cost "+listDataa.get(position).getCostOfToilet());
            holder.city.setText(stringArrayList.get(position));
            holder.ratingBar.setRating(Float.parseFloat(stringArrayList.get(position)));
            holder.distence.setText(String.valueOf(decimalFormat.format(stringDistance.get(position))+"km"));

        }

        @Override
        public int getItemCount() {
            return listDataa.size();
        }
    }
    public double getLat(int lat){
        double as=listToilet.get(lat).getLat();

        return   as;
    }
    public double getLng(int lng){
        double asg=listToilet.get(lng).getLat();
        return asg;
    }
    public String getChild(int a){
        String ad=listToilet.get(a).getIdChild();
        return ad;
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder implements Button.OnClickListener {
        public TextView textStar;
        ImageView imageView;
        TextView txtname;
        TextView txtaddress,cost,city,distence;
        RatingBar ratingBar;
        double lat ;
        double lng;
        Button distances;
        public Context contexts;
        ArrayList<ToiletRetData> toiletRetData;



        public SimpleViewHolder(View itemView, Context context, ArrayList<ToiletRetData> listDataa) {
            super(itemView);
            contexts=context;
            distances=itemView.findViewById(R.id.distanceButton);
           // textStar = (TextView) itemView.findViewById(R.id.textView9);
            cost=itemView.findViewById(R.id.cost);
            city=itemView.findViewById(R.id.city);
            distence=itemView.findViewById(R.id.textView5);
            ratingBar=itemView.findViewById(R.id.ratingBarOfSatr);
            txtname = (TextView) itemView.findViewById(R.id.name_ofToilet);
            txtaddress = (TextView) itemView.findViewById(R.id.name_tt);
           // itemView.setOnClickListener(this);
            distances.setOnClickListener(this);
              imageView =itemView.findViewById(R.id.images);


        }



        @Override
        public void onClick(View v) {



            final int ind = getPosition();

            Intent i = new Intent(getActivity().getBaseContext(),ReViewActivity.class);

            //PACK DATA
            i.putExtra("lat",listToilet.get(ind).getLat());
            i.putExtra("lng",listToilet.get(ind).getLng());
            i.putExtra("titel",listToilet.get(ind).getNameOfToilet());
            i.putExtra("ChildId",getChild(ind));
            //START ACTIVITY
            getActivity().startActivity(i);

            Toast.makeText(contexts,"Track Path",Toast.LENGTH_SHORT).show();

        }



    }








}
