package com.jboxx.sharebottomsheetdialog;

import android.content.pm.ResolveInfo;

/**
 * Created by Rifqi @jboxxpradhana
 */

public abstract class DefaultUtmSourceCallback implements ShareBottomSheetDialogInterface.OnCustomUtmSource {

    public String onChooseApps(ResolveInfo resolveInfo) {
        String utmSource = "";
        if (resolveInfo.activityInfo.packageName.contains("com.whatsapp")) {
            utmSource = UTMConstants.SOURCE_WHATSAPP;
        } else if (resolveInfo.activityInfo.packageName.contains("com.facebook") && resolveInfo.activityInfo.name.contains("com.facebook.composer.shareintent")) {
            utmSource = UTMConstants.SOURCE_FACEBOOK;
        } else if (resolveInfo.activityInfo.packageName.contains("com.facebook") && resolveInfo.activityInfo.name.contains("com.facebook.messenger.intents")) {
            utmSource = UTMConstants.SOURCE_FACEBOOK_MESSENGER;
        } else if (resolveInfo.activityInfo.name.contains("com.google.android.apps.docs.drive.clipboard.SendTextToClipboardActivity")) {
            utmSource = UTMConstants.SOURCE_COPY_LINK;
        } else if (resolveInfo.activityInfo.packageName.contains("jp.naver.line") || resolveInfo.activityInfo.name.contains("jp.naver.line")){
            utmSource = UTMConstants.SOURCE_LINE;
        } else if (resolveInfo.activityInfo.packageName.contains("com.google.android.apps.plus")) {
            utmSource = UTMConstants.SOURCE_GOOGLE_PLUS;
        } else if (resolveInfo.activityInfo.packageName.contains("com.google.android.talk") && resolveInfo.activityInfo.name.contains("com.google.android.apps.hangouts")) {
            utmSource = UTMConstants.SOURCE_GOOGLE_HANGOUT;
        } else if (resolveInfo.activityInfo.packageName.contains("com.twitter")) {
            utmSource = UTMConstants.SOURCE_TWITTER;
        } else if (resolveInfo.activityInfo.packageName.contains("org.telegram")) {
            utmSource = UTMConstants.SOURCE_TELEGRAM;
        } else if (resolveInfo.activityInfo.packageName.contains("com.tencent.mm")){
            utmSource = UTMConstants.SOURCE_WECHAT;
        } else if (resolveInfo.activityInfo.packageName.contains("com.bbm")){
            utmSource = UTMConstants.SOURCE_BLACKBERRY_MESSENGER;
        }
        return utmSource;
    }
}
