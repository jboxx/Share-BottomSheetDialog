package com.jboxx.sharebottomsheetdialog;

import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * Created by Rifqi on 9/2/18.
 */

public abstract class CustomOnDismissListener implements DialogInterface.OnDismissListener {

    protected String content;

    public void onDismiss (DialogInterface dialog, String content) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(!TextUtils.isEmpty(content)) {
            onDismiss(dialog, content);
        }
    }
}
