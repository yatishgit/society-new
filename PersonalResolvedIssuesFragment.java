package com.societtee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.PersonalResolvedIssuesRecyclerViewAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bean.HelpDeskPersonal;
import com.helper.RequiredFunction;
import com.helper.URL;
import com.parsers.Parsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class PersonalResolvedIssuesFragment extends Fragment {

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "resolivity";
    public ArrayList<HelpDeskPersonal> results = new ArrayList<>();
    URL url = new URL();
    RequiredFunction rf = new RequiredFunction();
    TextView empty_msg;

    public PersonalResolvedIssuesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDataSet();

        View rootView = inflater.inflate(R.layout.fragment_resolved_issues, container, false);
        empty_msg = (TextView) rootView.findViewById(R.id.empty_visitors);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.resolved_issues_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.d("res jhj", results.toString());
        mAdapter = new PersonalResolvedIssuesRecyclerViewAdapter(results, getContext(), getActivity(),mAdapter);

        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    private void getDataSet() {
        String url_path = "http://" + url.ip + "/societteewebservices/GetAllHelpDeskIssues.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rsesponse ", response.toString());
                        getObject(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Error",""+error.getLocalizedMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();
                SharedPreferences userDetails = getContext().getSharedPreferences("Society", getActivity().MODE_PRIVATE);
                params.put("societyId",userDetails.getString("societyId",""));
                params.put("userId",userDetails.getString("userId",""));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    public void getObject(String response) {
        ArrayList<HelpDeskPersonal> helpDeskPersonalArrayList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            String check = object.getString("response");
            if (!check.equalsIgnoreCase("failure")) {
                Parsers parsers = new Parsers();
                helpDeskPersonalArrayList = parsers.parseHelpDeskPersonal(response);
                //results = helpDeskPersonalArrayList;
                Iterator iterator = helpDeskPersonalArrayList.iterator();
                while(iterator.hasNext())
                {
                    HelpDeskPersonal helpDeskPersonal = new HelpDeskPersonal();
                    helpDeskPersonal=(HelpDeskPersonal)iterator.next();
                    if(helpDeskPersonal.getResolvedStatus().equalsIgnoreCase("1")){
                        results.add(helpDeskPersonal);
                    }
                }
                Log.d(LOG_TAG, results.toString());

            } else {
                empty_msg.setText(R.string.no_resolved_issued);
            }

        } catch (JSONException e) {
            Log.d("JSON notice", e.getLocalizedMessage());
        }
        mAdapter = new PersonalResolvedIssuesRecyclerViewAdapter(results, getContext(), getActivity(),mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        if (results.size() <= 0) {
            empty_msg.setText(R.string.no_resolved_issued);
        }
    }
}