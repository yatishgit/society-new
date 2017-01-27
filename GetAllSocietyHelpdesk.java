package com.societtee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ExpandableListAdapter;
import com.dialogs.LogoutDialog;
import com.helper.DrawerMenuItems;
import com.helper.RequiredFunction;
import com.helper.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllSocietyHelpdesk extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    URL url = new URL();
    RequiredFunction rf = new RequiredFunction();

    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<Integer> listImages;
    int previousGroup;

    private SharedPreferences prefs;
    private String prefName = "Society";
    String societyId;
    String buildingId;
    String flatNo;
    String username;
    String email;
    String societyname;
    String usertype;
    // String societyId="8";
//String buildingId="111";
//String flatNo="101";
    boolean status=false;

    TextView empty_msg;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_societyhelpdesk);

        Toolbar toolbar = (Toolbar) findViewById(R.id.societyhelpdesk_toolbar);
        setSupportActionBar(toolbar);

        TextView toolbar_title = (TextView)findViewById(R.id.toolbar_text);
        Typeface toolBarText = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        toolbar_title.setTypeface(toolBarText);
        toolbar_title.setText("Society Helpdesk");


        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        societyId= prefs.getString("societyId", "");
        buildingId= prefs.getString("buildingId", "");
        flatNo= prefs.getString("flatNo", "");
        username= prefs.getString("userName", "");
        email= prefs.getString("emailId", "");
        societyname= prefs.getString("societyName", "");
        usertype= prefs.getString("userType", "");

        Log.d("societyId","");
        Log.d("buildingId","");
        Log.d("flatNo","");
        Log.d("userName","");
        Log.d("email","");
        Log.d("societyName","");
        Log.d("userType","");
        viewPager = (ViewPager) findViewById(R.id.viewpager_society);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.societyhelpdesk_tabs);
        tabLayout.setupWithViewPager(viewPager);


        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        societyId = String.valueOf(prefs.getString
                ("societyId", societyId));

        buildingId = String.valueOf(prefs.getString
                ("buildingId", buildingId));

        flatNo = String.valueOf(prefs.getString
                ("flatNo", flatNo));

        Log.d("prefs","societyId: " + societyId);
        Log.d("prefs","buildingId: " + buildingId);
        Log.d("prefs","flatNo: " + flatNo);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_society_issues_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_society_issue = new Intent(GetAllSocietyHelpdesk.this, AddSocietyIssue.class);
                add_society_issue.putExtra("societyId", societyId);
                add_society_issue.putExtra("buildingId", buildingId);
                add_society_issue.putExtra("flatNo", flatNo);
                startActivity(add_society_issue);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_societyhelpdesk);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_societyhelpdesk);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView user = (TextView)hView.findViewById(R.id.username_textview);
        user.setText(username);

        TextView emailid = (TextView)hView.findViewById(R.id.useremail_textview);
        emailid.setText(email);

        TextView society = (TextView)hView.findViewById(R.id.usersociety_textview);
        society.setTypeface(toolBarText);
        society.setText(societyname);

        TextView flatno = (TextView)hView.findViewById(R.id.userflatno_textview);
        flatno.setText(flatNo);
        Log.i("flat no",flatNo);

        TextView flat = (TextView)hView.findViewById(R.id.userflat_textview);
        flat.setTypeface(toolBarText);

        TextView usertypee = (TextView)hView.findViewById(R.id.usertype_textview);
        usertypee.setText(usertype);

        empty_msg=(TextView)findViewById(R.id.empty_notices);


        expListView = (ExpandableListView) findViewById(R.id.lvExp_societyhelpdesk);
        DrawerMenuItems dr = new DrawerMenuItems();
        listDataHeader = dr.getListDataHeader();
        listDataChild = dr.getListDataChild();
        listImages = dr.getListImages();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild ,listImages);
        expListView.setAdapter(listAdapter);

        //group click
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if(groupPosition==6){
                    Intent login = new Intent(GetAllSocietyHelpdesk.this,Login.class);
                    startActivity(login);
                    finish();
                }
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    Log.d("collapse", String.valueOf(groupPosition));
                } else {
                    if (groupPosition != previousGroup) {
                        parent.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                    parent.expandGroup(groupPosition);
                    Log.d("expand", String.valueOf(groupPosition));
                }
                parent.smoothScrollByOffset(groupPosition);
                switch (groupPosition) {
                    case 6:
                        //myprofile
                        Intent i = new Intent(GetAllSocietyHelpdesk.this, MyProfile.class);
                        startActivity(i);
                        finish();
                        break;
                    case 7:
                        //logout
                        Log.d("adad", "das 9999");
                        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("Society", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userDetails.edit();
                        editor.clear();
                        editor.apply();
                        Log.d("adad", "das 9999");
                        LogoutDialog logoutDialog = new LogoutDialog(GetAllSocietyHelpdesk.this);
                        logoutDialog.show();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        //societee
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,AllConversation.class);
                            startActivity(i);
                            finishAffinity();
                        } else if (childPosition == 1) {
                            Intent i = new Intent(GetAllSocietyHelpdesk.this, GetAlbum.class);
                            startActivity(i);
                        } else if (childPosition == 2) {
                            // Intent i = new Intent(GetAllSocietyHelpdesk.this, GetPoll.class);
                            // startActivity(i);
                        } else if (childPosition == 3) {
                            Intent i = new Intent(GetAllSocietyHelpdesk.this, Notices.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1:
                        //MyFlat
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetMembers.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 1) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetVehicles.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 2) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetVisitors.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 3) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetStaff.class);
                            startActivity(i);
                            finish();
                        }else if (childPosition == 4) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetTransaction.class);
                            startActivity(i);
                            finish();
                        }  else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:
                        //Buzz
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,Buzz.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 3:
                        //directory
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetVendors.class);
                            startActivity(i);
                            finish();
                        }else if(childPosition == 1) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetBuildings.class);
                            startActivity(i);
                            finish();
                        }else if(childPosition == 2) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetManagementCommittee.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 4:
                        //helpdesk
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetAllPersonalHelpdesk.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 1) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetAllSocietyHelpdesk.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 5:
                        //Facility
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,Facilities.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 1) {
                            Intent i= new Intent(GetAllSocietyHelpdesk.this,GetAllBooking.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 6:
                        //myprofile
                        Intent i= new Intent(GetAllSocietyHelpdesk.this,MyProfile.class);
                        startActivity(i);
                        finish();
                        break;
                    case 7:
                        //logout
                        Log.d("adad","das 9999");
                        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("Society", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userDetails.edit();
                        editor.clear();
                        editor.apply();
                        Log.d("adad","das 9999");
                        LogoutDialog logoutDialog = new LogoutDialog(GetAllSocietyHelpdesk.this);
                        logoutDialog.show();
                        break;

                    default:
                        break;

                }
                return true;
            }
        });
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_societyhelpdesk);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SocietyPendingIssuesFragment(), "Pending Issues");
        adapter.addFragment(new SocietyResolvedIssuesFragment(), "Resolved Issues");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}