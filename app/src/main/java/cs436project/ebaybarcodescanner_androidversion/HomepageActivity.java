package cs436project.ebaybarcodescanner_androidversion;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class HomepageActivity extends AppCompatActivity {


    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private String lstScn;
    private int seletectedDrawer = R.id.nav_first_fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.scan_result);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);
        actionBar.setDisplayShowTitleEnabled(true);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.add(R.id.flContent, new NoScanFragment());
        ft.commit();

        scanBarcodeCustomLayout(null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        if(menuItem.getItemId() == seletectedDrawer) {
            mDrawer.closeDrawers();
            return;
        }

        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                seletectedDrawer = R.id.nav_first_fragment;
                fragmentClass = LastScanFragment.class;
                break;
            case R.id.nav_second_fragment:
                seletectedDrawer = R.id.nav_second_fragment;
                fragmentClass = HistoryFragment.class;
                break;
            default:
                seletectedDrawer = R.id.nav_first_fragment;
                fragmentClass = LastScanFragment.class;
        }

        try {

            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    public void postToEbay(View view) {
        Intent intent = new Intent(this, PostToEbay.class);
        startActivity(intent);
    }

    public void manualSearch(View view) {
        System.out.println("clicked");

    }

    public void scanBarcodeCustomLayout(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a Barcode");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, R.string.cancelled_scan, Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, R.string.successful_scan, Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Scanned");
                lstScn = result.getContents();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, new LastScanFragment()).commitAllowingStateLoss();

                new EbayFindItem(this, new EbayFindItem.AsyncResponse() {

                    @Override
                    public void processFinish(HashMap<String, Object> output){
                        ApplicationState state = ((ApplicationState) getApplicationContext());
                        output.put("barcode",lstScn);
                        state.addHistory(output);

                    }
                }).execute(result.getContents());
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
