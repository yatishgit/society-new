package com.adapter;

/**
 * Created by TheLucifer on 10/30/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.HelpDeskPersonal;
import com.dialogs.DeletePersonalPendingIssueDialog;
import com.helper.RequiredFunction;
import com.helper.URL;
import com.societtee.R;

import java.util.ArrayList;

public class PersonalResolvedIssuesRecyclerViewAdapter extends RecyclerView
        .Adapter<PersonalResolvedIssuesRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "personalAdapter";
    private ArrayList<HelpDeskPersonal> mDataset;
    private static MyClickListener myClickListener;
    Context context=null;
    Activity activity=null;
    //String buildingId = "120";
    //String flatNo = "104";
    boolean status=false;
    int flag=0;
    RecyclerView.Adapter adapter;

    RequiredFunction rf = new RequiredFunction();
    URL url = new URL();


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView issue_name;
        TextView issue_category;
        TextView issue_description;
        TextView issue_date;
        TextView status;
        TextView resolved_date;
        TextView resolved_by;

        TextView i_name;
        TextView i_category;
        TextView i_description;
        TextView i_resolveddate;
        ImageView delete_ppending;

        public DataObjectHolder(View itemView) {
            super(itemView);
            Typeface toolBarText = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            i_name = (TextView) itemView.findViewById(R.id.i_name);
            i_name.setTypeface(toolBarText);
            i_category = (TextView) itemView.findViewById(R.id.i_cateory);
            i_category.setTypeface(toolBarText);
            i_description = (TextView) itemView.findViewById(R.id.i_description);
            i_description.setTypeface(toolBarText);

            i_resolveddate = (TextView) itemView.findViewById(R.id.i_resolveddate);
            i_resolveddate.setTypeface(toolBarText);

            delete_ppending = (ImageView) itemView.findViewById(R.id.delete_ppending);

            issue_name = (TextView) itemView.findViewById(R.id.issue_name);
            issue_category = (TextView) itemView.findViewById(R.id.issue_category);
            issue_description = (TextView) itemView.findViewById(R.id.issue_description);
            issue_date = (TextView) itemView.findViewById(R.id.issue_date);
            status = (TextView) itemView.findViewById(R.id.status);
            resolved_date = (TextView) itemView.findViewById(R.id.resolved_date);

            /*delete_staff_button = (Button) itemView.findViewById(R.id.delete_staff_button);
            delete_staff_button.setOnClickListener(this);
            */
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

    public PersonalResolvedIssuesRecyclerViewAdapter(ArrayList<HelpDeskPersonal> myDataset, Context context, Activity activity,RecyclerView.Adapter adapter) {
        this.mDataset = myDataset;
        this.context = context;
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personal_helpdesk_resolved_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder,final int position) {

        final HelpDeskPersonal helpDeskPersonal = mDataset.get(position);

        holder.issue_name.setText(helpDeskPersonal.getIssueName());
        holder.issue_category.setText(helpDeskPersonal.getCategory());
        holder.issue_description.setText(helpDeskPersonal.getDescription());
        holder.issue_date.setText(helpDeskPersonal.getIssueDate());
   //     holder.resolved_by.setText(helpDeskPersonal.getIssueBy());
//        holder.status.setText(helpDeskPersonal.getResolvedStatus());
//        holder.resolved_date.setText(helpDeskPersonal.getResolvedDate());
        holder.delete_ppending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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