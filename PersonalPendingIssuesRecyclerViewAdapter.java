package com.adapter;

/**
 * Created by TheLucifer on 10/30/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bean.HelpDeskPersonal;
import com.dialogs.DeletePersonalHelpdeskDialog;
import com.dialogs.DeletePersonalPendingIssueDialog;
import com.helper.CustomVolleyRequest;
import com.helper.RequiredFunction;
import com.helper.URL;
import com.societtee.EditPersonalIssue;
import com.societtee.R;

import java.util.ArrayList;

public class PersonalPendingIssuesRecyclerViewAdapter extends RecyclerView
        .Adapter<PersonalPendingIssuesRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "personalpendingRecycler";
    private ArrayList<HelpDeskPersonal> mDataset;
    private static MyClickListener myClickListener;
    Context context=null;
    Activity activity=null;
    String buildingId = "120";
    String flatNo = "104";
    boolean status=false;
    int flag=0;
    RecyclerView.Adapter adapter;

    ImageLoader imageLoader;

    RequiredFunction rf = new RequiredFunction();
    URL url = new URL();


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView issue_name;
        TextView issue_category;
        TextView issue_description;
        TextView issue_date;
        NetworkImageView issue_image;
        ImageView edit;
        ImageView delete;
        TextView i_name;
        TextView i_category;
        TextView i_description;
        TextView i_date;



        public DataObjectHolder(View itemView) {
            super(itemView);
            Typeface toolBarText = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            i_name = (TextView) itemView.findViewById(R.id.i_name);
            i_name.setTypeface(toolBarText);
            i_category = (TextView) itemView.findViewById(R.id.i_cateory);
            i_category.setTypeface(toolBarText);
            i_description = (TextView) itemView.findViewById(R.id.i_description);
            i_description.setTypeface(toolBarText);
            i_date = (TextView) itemView.findViewById(R.id.i_date);
            i_date.setTypeface(toolBarText);

            issue_image = (NetworkImageView) itemView.findViewById(R.id.staff_image);
            issue_name = (TextView) itemView.findViewById(R.id.issue_name);
            issue_category = (TextView) itemView.findViewById(R.id.issue_category);
            issue_description = (TextView) itemView.findViewById(R.id.issue_description);
            issue_date = (TextView) itemView.findViewById(R.id.issue_date);
            edit = (ImageView) itemView.findViewById(R.id.edit_ppending);
            delete = (ImageView) itemView.findViewById(R.id.delete_ppending);


            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PersonalPendingIssuesRecyclerViewAdapter(ArrayList<HelpDeskPersonal> myDataset, Context context, Activity activity,RecyclerView.Adapter adapter) {
        this.mDataset = myDataset;
        this.context = context;
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personal_helpdesk_pending_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        final HelpDeskPersonal helpDeskPersonal = mDataset.get(position);

        holder.issue_name.setText(helpDeskPersonal.getIssueName());
        holder.issue_category.setText(helpDeskPersonal.getCategory());
        holder.issue_description.setText(helpDeskPersonal.getDescription());
        String date[] = helpDeskPersonal.getIssueDate().split(" ");
        holder.issue_date.setText(date[0]);
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        String image_url="http://"+url.ip+"/societteewebservices/"+helpDeskPersonal.getImage();
        Log.d(LOG_TAG,image_url);
        imageLoader.get(image_url, ImageLoader.getImageListener(holder.issue_image,R.mipmap.placeholder, R.mipmap.placeholder));
        holder.issue_image.setImageUrl(image_url, imageLoader);
        
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(activity, EditPersonalIssue.class);
                edit.putExtra("issue_name",helpDeskPersonal.getIssueName());

                String[] category_array = activity.getResources().getStringArray(R.array.category);
                int cat_int=0;
                for(int i=0;i<category_array.length;i++)
                {
                    if(category_array[i].equalsIgnoreCase(helpDeskPersonal.getCategory())){
                        cat_int=i;
                    }
                }
                edit.putExtra("issue_category",String.valueOf(cat_int));
                edit.putExtra("issue_description",helpDeskPersonal.getDescription());

                String[] type_array = activity.getResources().getStringArray(R.array.issueType);
                int type_int=0;
                for(int i=0;i<type_array.length;i++)
                {
                    if(type_array[i].equalsIgnoreCase(helpDeskPersonal.getType())){
                        type_int=i;
                    }
                }
                edit.putExtra("issue_type",String.valueOf(type_int));
                edit.putExtra("issue_image",helpDeskPersonal.getImage());
                edit.putExtra("societyId",helpDeskPersonal.getSocietyId());
                edit.putExtra("buildingId",helpDeskPersonal.getBuildingId());

                SharedPreferences sharedPreferences = activity.getSharedPreferences("Society",context.MODE_PRIVATE);
                edit.putExtra("flatNo",sharedPreferences.getString("flatNo",""));
                edit.putExtra("userId",helpDeskPersonal.getUserId());
                edit.putExtra("issueId",helpDeskPersonal.getIssueId());
                activity.startActivity(edit);
            }
        });
        
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "delete clicked", Toast.LENGTH_SHORT).show();
                DeletePersonalPendingIssueDialog dialog = new DeletePersonalPendingIssueDialog(activity,helpDeskPersonal.getSocietyId(),helpDeskPersonal.getBuildingId(),
                                                            helpDeskPersonal.getIssueId(),helpDeskPersonal.getUserId(),adapter,position);
                dialog.show();
            }
        });
    }

    public void addItem(HelpDeskPersonal dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}