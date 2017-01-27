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
import android.support.v4.app.FragmentTransaction;
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
import com.interfaces.MyDialogFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllPersonalHelpdesk extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyDialogFragmentListener {

    URL url = new URL();
    RequiredFunction rf = new RequiredFunction();

    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<Integer> listImages;
    int previousGroup;
    TextView empty_msg;


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

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_personalhelpdesk);

        Toolbar toolbar = (Toolbar) findViewById(R.id.personalhelpdesk_toolbar);
        setSupportActionBar(toolbar);

        TextView toolbar_title = (TextView)findViewById(R.id.toolbar_text);
        Typeface toolBarText = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        toolbar_title.setTypeface(toolBarText);
        toolbar_title.setText("Personal Helpdesk");

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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.personalhelpdesk_tabs);
        tabLayout.setupWithViewPager(viewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_personalhelpdesk);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_personalhelpdesk);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_personal_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_personal = new Intent(GetAllPersonalHelpdesk.this, AddPersonalIssue.class);
                add_personal.putExtra("societyId", societyId);
                add_personal.putExtra("buildingId", buildingId);
                add_personal.putExtra("flatNo", flatNo);
                add_personal.putExtra("userId",prefs.getString("userId",""));
                startActivity(add_personal);
            }
        });



        empty_msg=(TextView)findViewById(R.id.empty_notices);


        expListView = (ExpandableListView) findViewById(R.id.lvExp_personalhelpdesk);
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
                        Intent i = new Intent(GetAllPersonalHelpdesk.this, MyProfile.class);
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
                        LogoutDialog logoutDialog = new LogoutDialog(GetAllPersonalHelpdesk.this);
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
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,AllConversation.class);
                            startActivity(i);
                            finishAffinity();
                        } else if (childPosition == 1) {
                            Intent i = new Intent(GetAllPersonalHelpdesk.this, GetAlbum.class);
                            startActivity(i);
                        } else if (childPosition == 2) {
                            // Intent i = new Intent(GetAllPersonalHelpdesk.this, GetPoll.class);
                            // startActivity(i);
                        } else if (childPosition == 3) {
                            Intent i = new Intent(GetAllPersonalHelpdesk.this, Notices.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1:
                        //MyFlat
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetMembers.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 1) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetVehicles.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 2) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetVisitors.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 3) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetStaff.class);
                            startActivity(i);
                            finish();
                        }else if (childPosition == 4) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetTransaction.class);
                            startActivity(i);
                            finish();
                        }  else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:
                        //Buzz
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,Buzz.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 3:
                        //directory
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetVendors.class);
                            startActivity(i);
                            finish();
                        }else if(childPosition == 1) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetBuildings.class);
                            startActivity(i);
                            finish();
                        }else if(childPosition == 2) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetManagementCommittee.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 4:
                        //helpdesk
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetAllPersonalHelpdesk.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 1) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetAllSocietyHelpdesk.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 5:
                        //Facility
                        if (childPosition == 0) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,Facilities.class);
                            startActivity(i);
                            finish();
                        } else if (childPosition == 1) {
                            Intent i= new Intent(GetAllPersonalHelpdesk.this,GetAllBooking.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 6:
                        //myprofile
                        Intent i= new Intent(GetAllPersonalHelpdesk.this,MyProfile.class);
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
                        LogoutDialog logoutDialog = new LogoutDialog(GetAllPersonalHelpdesk.this);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_personalhelpdesk);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalPendingIssuesFragment(), "Pending Issues");
        adapter.addFragment(new PersonalResolvedIssuesFragment(), "Resolved Issues");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onReturnValue(String foo) {
        if(foo.equalsIgnoreCase("Personal Pending Delete")){
            //Toast.makeText(this, "i am returned", Toast.LENGTH_SHORT).show();'
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(new PersonalPendingIssuesFragment()).attach(new PersonalPendingIssuesFragment()).commit();
        }
        if(foo.equalsIgnoreCase("Society Delete")){
            //Toast.makeText(this, "i am returned", Toast.LENGTH_SHORT).show();'
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(new PersonalPendingIssuesFragment()).attach(new PersonalPendingIssuesFragment()).commit();
        }
        if(foo.equalsIgnoreCase("Personal Edit")){
            //Toast.makeText(this, "i am returned", Toast.LENGTH_SHORT).show();'
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(new PersonalPendingIssuesFragment()).attach(new PersonalPendingIssuesFragment()).commit();
        }
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