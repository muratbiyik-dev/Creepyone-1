package com.pureblacksoft.creepyone.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.fragment.ArchiveFragment;
import com.pureblacksoft.creepyone.fragment.GalleryFragment;
import com.pureblacksoft.creepyone.fragment.HomeFragment;
import com.pureblacksoft.creepyone.fragment.SearchFragment;
import com.pureblacksoft.creepyone.fragment.UserFragment;
import com.pureblacksoft.creepyone.helper.DialogHelper;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.helper.TinyDB;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.loader.PrefLoader;
import com.pureblacksoft.creepyone.service.DownloadIntentService;
import com.pureblacksoft.creepyone.util.DownloadCallback;
import com.pureblacksoft.creepyone.util.ServerCallback;
import com.pureblacksoft.creepyone.veriable.MainVeriable;
import com.pureblacksoft.creepyone.veriable.SearchVeriable;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    public BottomNavigationView bottomNavMA;

    private boolean backPressedOnce;
    private DataLoader dataLoader;
    private MainActivity.DownloadBroadcastReceiver broadcastReceiver;
    private DownloadCallback downloadCallback;
    private FragmentTransaction fragmentTransaction;

    private AlertDialog touDialog;
    private AlertDialog arDialog;

    private Toast offlineToast;
    private Toast newStoriesToast;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        //Preference Control
        PrefLoader prefLoader = new PrefLoader();
        prefLoader.prefControl(this);
        //Preference Control/

        setContentView(R.layout.activity_main);

        //Toasts
        offlineToast = Toast.makeText(this, R.string.Offline_Toast, Toast.LENGTH_LONG);
        offlineToast.setGravity(Gravity.BOTTOM, 0, 160);
        newStoriesToast = Toast.makeText(this, R.string.New_Stories_Toast, Toast.LENGTH_LONG);
        newStoriesToast.setGravity(Gravity.BOTTOM, 0, 160);
        backToast = Toast.makeText(this, R.string.Back_Toast, Toast.LENGTH_SHORT);
        backToast.setGravity(Gravity.BOTTOM, 0, 160);
        //Toasts/

        TinyDB tinyDB = new TinyDB(getApplicationContext());

        //Write oldAppLocaleCodeMA & oldFontScaleCodeMA
        tinyDB.putInt("oldAppLocaleCodeMA", PrefLoader.appLocaleCode);
        tinyDB.putInt("oldFontScaleCodeMA", PrefLoader.fontScaleCode);
        //Write oldAppLocaleCodeMA & oldFontScaleCodeMA/

        //Read readedIdList
        ArticleActivity.readedIdList = tinyDB.getListInt("readedIdList");
        //Read readedIdList/

        //Load Data
        dataLoader = new DataLoader();
        if (PrefLoader.prefChanged)
        {
            dataLoader.setDatabase(this);

            if (MainVeriable.localDatasDifferent || DataLoader.artCount == 0)
            {
                if (MainVeriable.localDatasDifferent)
                {
                    LogHelper.LogD("Download", "(MainVeriable.localDatasDifferent) -> Start downloading.");

                    MainVeriable.localDatasDifferent = false;
                }
                else
                {
                    LogHelper.LogD("Download", "(DataLoader.artCount == 0) -> Start downloading.");
                }

                downloadData();
            }

            PrefLoader.prefChanged = false;
        }
        else if (!MainVeriable.downloadingAnimFinished)
        {
            LogHelper.LogD("Download", "(!MainVeriable.downloadingAnimFinished) -> Start downloading.");

            dataLoader.versionControl(this);
            dataLoader.setDatabase(this);
            downloadData();
        }
        //Load Data/

        //Load Fragment
        if (MainVeriable.selectedFragment == null)
        {
            MainVeriable.selectedFragment = new HomeFragment();
        }

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerMA, MainVeriable.selectedFragment).commit();
        //Load Fragment/

        //Bottom Navigation
        bottomNavMA = findViewById(R.id.bottomNavMA);
        bottomNavMA.setItemIconTintList(null);
        bottomNavMA.setOnNavigationItemSelectedListener(item ->
        {
            MainVeriable.selectedFragment = null;

            switch (item.getItemId())
            {
                case R.id.itmSearch:
                    MainVeriable.selectedFragment = new SearchFragment();
                    SearchVeriable.noResultVisible = false;
                    SearchVeriable.scrollViewGone = false;
                    SearchVeriable.query = null;
                    break;

                case R.id.itmArchive:
                    MainVeriable.selectedFragment = new ArchiveFragment();
                    break;

                case R.id.itmGallery:
                    MainVeriable.selectedFragment = new GalleryFragment();
                    break;

                case R.id.itmUser:
                    MainVeriable.selectedFragment = new UserFragment();
                    break;

                default:
                    MainVeriable.selectedFragment = new HomeFragment();
                    break;
            }

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerMA, MainVeriable.selectedFragment).commit();

            return true;
        });
        //Bottom Navigation/

        //App Launch Operations
        if (!MainVeriable.appLaunched)
        {
            //Terms of Use
            //Read termsAccepted
            boolean termsAccepted = tinyDB.getBoolean("termsAccepted");
            //Read termsAccepted/

            if (!termsAccepted)
            {
                AlertDialog.Builder builder = DialogHelper.alertBuilder(this);
                builder.setCancelable(false);
                builder.setTitle(R.string.Terms_Title);
                builder.setMessage(R.string.Terms_Info);
                builder.setPositiveButton(R.string.Terms_Accept, ((DialogInterface dialog, int which) ->
                {
                    //Write termsAccepted
                    tinyDB.putBoolean("termsAccepted", true);
                    //Write termsAccepted/
                }));
                builder.setNegativeButton(R.string.Terms_Reject, ((DialogInterface dialog, int which) ->
                {
                    //Write termsAccepted
                    tinyDB.putBoolean("termsAccepted", false);
                    //Write termsAccepted/

                    finishAffinity();
                    System.exit(0);
                }));
                touDialog = builder.show();
            }
            //Terms of Use/

            //App Rating
            //Read appLaunchCount
            int appLaunchCount = tinyDB.getInt("appLaunchCount");
            //Read appLaunchCount/

            if (!MainVeriable.appLaunchCounted && appLaunchCount != -5)
            {
                appLaunchCount++;

                //Write appLaunchCount
                tinyDB.putInt("appLaunchCount", appLaunchCount);
                //Write appLaunchCount/

                MainVeriable.appLaunchCounted = true;
            }

            if (appLaunchCount >= 5)
            {
                AlertDialog.Builder builder = DialogHelper.alertBuilder(this);
                builder.setCancelable(false);
                builder.setTitle(R.string.App_Rating_Title);
                builder.setMessage(R.string.App_Rating_Message);
                builder.setPositiveButton(R.string.App_Rating_Positive, ((DialogInterface dialog, int which) ->
                {
                    //Write appLaunchCount
                    tinyDB.putInt("appLaunchCount", -5);
                    //Write appLaunchCount/

                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.pureblacksoft.creepyone");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }));
                builder.setNegativeButton(R.string.App_Rating_Negative, ((DialogInterface dialog, int which) ->
                {
                    //Write appLaunchCount
                    tinyDB.putInt("appLaunchCount", -5);
                    //Write appLaunchCount/
                }));
                builder.setNeutralButton(R.string.App_Rating_Neutral, ((DialogInterface dialog, int which) ->
                {
                    //Write appLaunchCount
                    tinyDB.putInt("appLaunchCount", 0);
                    //Write appLaunchCount/
                }));
                arDialog = builder.show();
            }
            //App Rating/

            if (termsAccepted && appLaunchCount == -5)
            {
                MainVeriable.appLaunched = true;
            }
        }
        //App Launch Operations/
    }

    public void downloadData()
    {
        activateDownloading(true);

        dataLoader.downloadDataJSON(this, new ServerCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                if (!DataLoader.dataChanged)
                {
                    activateDownloading(false);
                    getDownloadCallback().outDataChange();
                }
            }

            @Override
            public void onFailure(VolleyError error)
            {
                offlineToast.show();

                activateDownloading(false);
                getDownloadCallback().outDataChange();
            }
        });
    }

    private void activateDownloading(Boolean choice)
    {
        if (choice)
        {
            LogHelper.LogD("Download", "{activateDownloading} -> True");

            MainVeriable.downloadingAnimFinished = false;

            registerBroadcastReceiver(true);
        }
        else
        {
            LogHelper.LogD("Download", "{activateDownloading} -> False");

            DataLoader.downloading = false;
            MainVeriable.downloadingAnimFinished = true;

            registerBroadcastReceiver(false);
        }
    }

    private class DownloadBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //New Stories
            if (DataLoader.newStories)
            {
                newStoriesToast.show();

                DataLoader.newStories = false;
            }
            //New Stories/

            activateDownloading(false);
            getDownloadCallback().onDataChange();
        }
    }

    private void registerBroadcastReceiver(Boolean choice)
    {
        if (choice)
        {
            LogHelper.LogD("Download", "{registerBroadcastReceiver} -> True");

            broadcastReceiver = new MainActivity.DownloadBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(DownloadIntentService.BROADCAST_ACTION);
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);

            MainVeriable.receiverRegistered = true;
        }
        else
        {
            if (MainVeriable.receiverRegistered)
            {
                LogHelper.LogD("Download", "{registerBroadcastReceiver} -> False");

                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

                MainVeriable.receiverRegistered = false;
            }
        }
    }

    public DownloadCallback getDownloadCallback()
    {
        return downloadCallback;
    }

    public void setDownloadCallback(DownloadCallback downloadCallback)
    {
        this.downloadCallback = downloadCallback;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        registerBroadcastReceiver(false);

        if (touDialog != null)
        {
            touDialog.dismiss();
        }

        if (arDialog != null)
        {
            arDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (backPressedOnce)
        {
            MainVeriable.selectedFragment = new HomeFragment();
            finishAffinity();

            return;
        }

        this.backPressedOnce = true;

        backToast.show();

        new Handler().postDelayed(() ->
        {
            backPressedOnce = false;
        }, 2000);
    }
}

//PureBlack Software / Murat BIYIK