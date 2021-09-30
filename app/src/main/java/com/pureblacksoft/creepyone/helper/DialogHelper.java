package com.pureblacksoft.creepyone.helper;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.pureblacksoft.creepyone.R;

public class DialogHelper
{
    public static AlertDialog.Builder alertBuilder(Context context)
    {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogTheme));
    }
}

//PureBlack Software / Murat BIYIK