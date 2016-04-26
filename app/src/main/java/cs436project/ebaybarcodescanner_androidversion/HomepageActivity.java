package cs436project.ebaybarcodescanner_androidversion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class HomepageActivity extends AppCompatActivity {


    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private String lstScn;
    private HashMap<String,Object> lastScan;
    private int seletectedDrawer = R.id.nav_first_fragment;
    private ActionBar actionBar;
    private String manualBarcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBar = getSupportActionBar();
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

    public void selectFromHistory(String barcode) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new LastScanFragment()).commit();
        new EbayFindItem(this, new EbayFindItem.AsyncResponse() {

            @Override
            public void processFinish(HashMap<String, Object> output) {
                ApplicationState state = ((ApplicationState) getApplicationContext());
                output.put("barcode", lstScn);
                state.addHistory(output);

            }
        }).execute(barcode);
        nvDrawer.setCheckedItem(R.id.nav_first_fragment);
        seletectedDrawer = R.id.nav_first_fragment;

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
                actionBar.setTitle(R.string.scan_result);
                break;
            case R.id.nav_second_fragment:
                seletectedDrawer = R.id.nav_second_fragment;
                fragmentClass = HistoryFragment.class;
                actionBar.setTitle(R.string.history);
                break;
            default:
                seletectedDrawer = R.id.nav_first_fragment;
                fragmentClass = LastScanFragment.class;
                actionBar.setTitle(R.string.scan_result);
        }

        try {

            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
        if(seletectedDrawer == R.id.nav_first_fragment) {
            selectFromHistory(lastScan.get("barcode").toString());
        }
    }


    public void postToEbay(View view) {
        Intent intent = new Intent(this, PostToEbay.class);
        startActivity(intent);
    }

    public void manualSearch(View view) {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        popup.setTitle("Manual Search:\nEnter a barcode number");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        popup.setView(input);


        popup.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manualBarcode = input.getText().toString();
                selectFromHistory(manualBarcode);
            }
        });
        popup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        popup.show();
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
                        state.addHistory(output);
                        lastScan = output;

                    }
                }).execute(result.getContents());
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
