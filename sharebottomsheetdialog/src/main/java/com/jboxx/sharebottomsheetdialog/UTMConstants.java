package com.jboxx.sharebottomsheetdialog;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Rifqi @jboxxpradhana
 */

public class UTMConstants {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UTM_CAMPAIGN,
            UTM_CONTENT,
            UTM_MEDIUM,
            UTM_TERM
    })
    public @interface UTM {}

    public static  final  String UTM_CAMPAIGN  = "utm_campaign";
    public static final String UTM_CONTENT = "utm_content";
    public static final String UTM_MEDIUM = "utm_medium";
    public static final String UTM_TERM = "utm_term";
    public static final String UTM_SOURCE = "utm_source";


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SOURCE_WHATSAPP,
            SOURCE_FACEBOOK,
            SOURCE_COPY_LINK
    })
    protected  @interface Source {}

    protected static final String SOURCE_COPY_LINK = "copyLink";
    protected static final String SOURCE_WHATSAPP = "whatsapp";
    protected static final String SOURCE_FACEBOOK = "facebook";
    protected static final String SOURCE_FACEBOOK_MESSENGER = "facebookMessenger";
    protected static final String SOURCE_LINE = "line";
    protected static final String SOURCE_GOOGLE_PLUS = "googlePlus";
    protected static final String SOURCE_GOOGLE_HANGOUT = "googleHangout";
    protected static final String SOURCE_TWITTER = "twitter";
    protected static final String SOURCE_TELEGRAM = "telegram";
    protected static final String SOURCE_WECHAT = "wechat";
    protected static final String SOURCE_BLACKBERRY_MESSENGER = "bbm";
}
