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
import android.widget.TextView;

import com.bean.HelpDeskSociety;
import com.helper.RequiredFunction;
import com.helper.URL;
import com.societtee.R;

import java.util.ArrayList;

public class SocietyResolvedIssuesRecyclerViewAdapter extends RecyclerView
        .Adapter<SocietyResolvedIssuesRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "societyresolvedRecyclerViewAdapter";
    private ArrayList<HelpDeskSociety> mDataset;
    private static MyClickListener myClickListener;
    Context context=null;
    Activity activity=null;
    String buildingId = "120";
    String flatNo = "104";
    boolean status=false;
    int flag=0;

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
        TextView i_name;
        TextView i_category;
        TextView i_description;
        TextView i_raiseddate;
        TextView i_resolvedstatus;
        TextView i_resolveddate;
        TextView resolved_by;


        public DataObjectHolder(View itemView) {
            super(itemView);
            Typeface toolBarText = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Roboto-Medium.ttf");

            i_name = (TextView) itemView.findViewById(R.id.i_name);
            i_name.setTypeface(toolBarText);
            i_category = (TextView) itemView.findViewById(R.id.i_category);
            i_category.setTypeface(toolBarText);
            i_description = (TextView) itemView.findViewById(R.id.i_description);
            i_description.setTypeface(toolBarText);
            i_raiseddate = (TextView) itemView.findViewById(R.id.i_raiseddate);
            i_raiseddate.setTypeface(toolBarText);
            i_resolvedstatus = (TextView) itemView.findViewById(R.id.i_resolvedstatus);
            i_resolveddate.setTypeface(toolBarText);
            i_resolveddate = (TextView) itemView.findViewById(R.id.i_resolveddate);
            i_resolveddate.setTypeface(toolBarText);

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

    public SocietyResolvedIssuesRecyclerViewAdapter(ArrayList<HelpDeskSociety> myDataset, Context context, Activity activity) {
        this.mDataset = myDataset;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.society_helpdesk_resolved_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        HelpDeskSociety helpDeskSociety = mDataset.get(position);

        holder.issue_name.setText(helpDeskSociety.getIssueName());
        holder.issue_category.setText(helpDeskSociety.getCategory());
        holder.issue_description.setText(helpDeskSociety.getDescription());
        holder.issue_date.setText(helpDeskSociety.getIssueDate());
        holder.resolved_by.setText(helpDeskSociety.getIssueBy());
        holder.status.setText(helpDeskSociety.getResolvedStatus());
        holder.resolved_date.setText(helpDeskSociety.getResolvedDate());
    }

    public void addItem(HelpDeskSociety dataObj, int index) {
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