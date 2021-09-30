package com.pureblacksoft.creepyone.loader;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.pureblacksoft.creepyone.helper.TinyDB;

import java.util.Locale;

import static android.content.Context.WINDOW_SERVICE;

public class PrefLoader
{
    public static boolean prefChanged;
    public static boolean prefChangedAL;
    public static boolean prefChangedFS;
    public static int appLocaleCode;
    public static int fontScaleCode;

    public void prefControl(Context context)
    {
        //Read appLocaleCode & fontScaleCode
        TinyDB tinyDB = new TinyDB(context.getApplicationContext());
        appLocaleCode = tinyDB.getInt("appLocaleCode");
        fontScaleCode = tinyDB.getInt("fontScaleCode");
        //Read appLocaleCode & fontScaleCode/

        //App Locale
        String appLocaleEN = new Locale("en").getLanguage();
        String appLocaleTR = new Locale("tr").getLanguage();

        if (appLocaleCode == 1)
        {
            setAppLocale(context, appLocaleEN);
        }
        else if (appLocaleCode == 2)
        {
            setAppLocale(context, appLocaleTR);
        }
        else
        {
            String defaultAppLocale = Locale.getDefault().getLanguage();
            if (defaultAppLocale.equals(appLocaleTR))
            {
                appLocaleCode = 2;
                setAppLocale(context, defaultAppLocale);
            }
            else
            {
                appLocaleCode = 1;
                setAppLocale(context, appLocaleEN);
            }
        }
        //App Locale/

        //Font Scale
        switch (fontScaleCode)
        {
            case 1:
                setFontScale(context, 0.85f);
                break;

            case 2:
                setFontScale(context, 1f);
                break;

            case 3:
                setFontScale(context, 1.15f);
                break;

            case 4:
                setFontScale(context, 1.30f);
                break;

            default:
                float defaultFontScale = context.getResources().getConfiguration().fontScale;
                if (defaultFontScale == 0.85f)
                {
                    fontScaleCode = 1;
                    setFontScale(context, defaultFontScale);
                }
                else if (defaultFontScale == 1.15f)
                {
                    fontScaleCode = 3;
                    setFontScale(context, defaultFontScale);
                }
                else if (defaultFontScale == 1.30f)
                {
                    fontScaleCode = 4;
                    setFontScale(context, defaultFontScale);
                }
                else
                {
                    fontScaleCode = 2;
                    setFontScale(context, 1f);
                }
                break;
        }
        //Font Scale/
    }

    private void setAppLocale(Context context, String localeCode)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = new Locale(localeCode);
        config.setLocale(locale);
        resources.updateConfiguration(config, metrics);
    }

    private void setFontScale(Context context, Float fontScale)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        Configuration config = resources.getConfiguration();
        config.fontScale = fontScale;
        metrics.scaledDensity = config.fontScale * metrics.density;
        resources.updateConfiguration(config, metrics);
    }

    public void prefChangeControl(Context context, int oldAppLocaleCode, int oldFontScaleCode)
    {
        prefChangedAL = appLocaleCode != oldAppLocaleCode;
        prefChangedFS = fontScaleCode != oldFontScaleCode;
        prefChanged = prefChangedAL || prefChangedFS;
    }
}

//PureBlack Software / Murat BIYIK