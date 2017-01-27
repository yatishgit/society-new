package com.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.helper.RequiredFunction;
import com.helper.URL;
import com.interfaces.MyDialogFragmentListener;
import com.societtee.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import interfaces.MyDialogFragmentListener;

public class DeletePersonalHelpdeskDialog extends Dialog implements View.OnClickListener  {

    Button cancel;
    Button delete;
    Activity activity;
    public Context context;
    public Dialog d;
    RequiredFunction rf=new RequiredFunction();
    URL url = new URL();
    boolean status = false;
    static String LOG_TAG="deletePersonalHelpd";

    String societyId;
    String buildingId;
    String flatNo;
    String issueId;
    RecyclerView.Adapter mAdapter;
    int position;

    public DeletePersonalHelpdeskDialog(Activity activity, String societyId, String buildingId, String issueId) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.societyId = societyId;
        this.buildingId = buildingId;
        this.issueId = issueId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_personal_helpdesk_dialog);

        cancel = (Button) findViewById(R.id.cancel);
        delete = (Button) findViewById(R.id.delete);

        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.delete:
                remove();
                Toast.makeText(activity.getApplicationContext(),"Issue removed",Toast.LENGTH_SHORT).show();
                MyDialogFragmentListener activitys = (MyDialogFragmentListener) context;
                activitys.onReturnValue("issue removed");
                dismiss();
                break;
            default:

                break;

        }
    }

    public void remove(){
        if(rf.isConnected(activity)){

            String url_path="http://"+url.ip+"/societteewebservices/DeleteHelpDeskIssue.php";
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,url_path,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("rsesponse ", response.toString());
                            try{
                                JSONObject object = new JSONObject(response);
                                String res=object.getString("response");
                                if (res.equalsIgnoreCase("success")){
                                    status = true;
                                }
                                else {
                                    status = false;
                                }
                            }
                            catch (JSONException e){
                                Log.d(LOG_TAG,e.getLocalizedMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Error",error.getLocalizedMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("societyId", societyId);
                    params.put("buildingId",buildingId);
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Society",getContext().MODE_PRIVATE);
                    String userId = sharedPreferences.getString("userId","");
                    params.put("userId",userId);
                    params.put("issueId",issueId);

                    Log.d(LOG_TAG,params.toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);
        }
        else{
            status = false;
            Toast.makeText(context,R.string.no_internet,Toast.LENGTH_SHORT).show();
        }
    }
}
