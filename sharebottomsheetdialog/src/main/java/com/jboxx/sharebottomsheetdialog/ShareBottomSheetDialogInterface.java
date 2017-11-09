package com.jboxx.sharebottomsheetdialog;

import android.content.pm.ResolveInfo;

/**
 * Created by Rifqi @jboxxpradhana
 */

public interface ShareBottomSheetDialogInterface {

    public interface OnCustomUtmSource {
        String onChooseApps(ResolveInfo resolveInfo);
    }

    public interface OnCustomMessage {
        String onChooseApps(ResolveInfo resolveInfo);
    }


}
