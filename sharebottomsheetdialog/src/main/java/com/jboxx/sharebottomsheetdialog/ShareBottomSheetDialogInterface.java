package com.jboxx.sharebottomsheetdialog;

import android.content.pm.ResolveInfo;

/**
 * Created by Rifqi @jboxxpradhana
 */

public interface ShareBottomSheetDialogInterface {

     interface OnCustomParameter {
        String onChooseApps(ResolveInfo resolveInfo);
    }

     interface OnCustomMessage {
        String onChooseApps(ResolveInfo resolveInfo);
    }

}
