package com.jboxx.bottomsheetdialog;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialog;
import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialogInterface;
import com.jboxx.sharebottomsheetdialog.UTMConstants;

/**
 * Created by Rifqi
 */

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button shareButton1 = findViewById(R.id.shareBtn1);
        final Button shareButton2 = findViewById(R.id.shareBtn2);


        shareButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ShareBottomSheetDialog.Builder(getSupportFragmentManager())
                        .setCancelable(true)
                        .isFullScreen(true)
                        .setMessage(new ShareBottomSheetDialogInterface.OnCustomMessage() {
                            @Override
                            public String onChooseApps(ResolveInfo resolveInfo) {
                                return "Share this link!";
                            }
                        })
                        .setUrl("https://www.youtube.com/watch?v=2ThFJODUZ_4")
                        .addParameter(UTMConstants.UTM_TERM, "A.O.V")
                        .addParameter(UTMConstants.UTM_CAMPAIGN, "video-miwon")
                        .addParameterWithCallback(UTMConstants.UTM_SOURCE, new ShareBottomSheetDialogInterface.OnCustomUtmSource() {
                            @Override
                            public String onChooseApps(ResolveInfo resolveInfo) {
                                String utmSource = "";
                                if (resolveInfo.activityInfo.packageName.contains("com.whatsapp")) {
                                    utmSource = "from whatsapp";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.facebook") && resolveInfo.activityInfo.name.contains("com.facebook.composer.shareintent")) {
                                    utmSource = "from facebook";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.facebook") && resolveInfo.activityInfo.name.contains("com.facebook.messenger.intents")) {
                                    utmSource = "from facebook messenger";
                                } else if (resolveInfo.activityInfo.name.contains("com.google.android.apps.docs.drive.clipboard.SendTextToClipboardActivity")) {
                                    utmSource = "from copy link";
                                } else if (resolveInfo.activityInfo.packageName.contains("jp.naver.line") || resolveInfo.activityInfo.name.contains("jp.naver.line")){
                                    utmSource = "from line";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.google.android.apps.plus")) {
                                    utmSource = "from google plus";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.google.android.talk") && resolveInfo.activityInfo.name.contains("com.google.android.apps.hangouts")) {
                                    utmSource = "from google hangouts";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.twitter")) {
                                    utmSource = "from twitter";
                                } else if (resolveInfo.activityInfo.packageName.contains("org.telegram")) {
                                    utmSource = "from telegram";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.tencent.mm")){
                                    utmSource = "from wechat";
                                } else if (resolveInfo.activityInfo.packageName.contains("com.bbm")){
                                    utmSource = "from bbm";
                                }
                                return utmSource;
                            }
                        })
                        .addParameterWithCallback(UTMConstants.UTM_CONTENT, new ShareBottomSheetDialogInterface.OnCustomUtmSource() {
                            @Override
                            public String onChooseApps(ResolveInfo resolveInfo) {
                                return "video";
                            }
                        })
                        .show();
            }
        });

        shareButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareBottomSheetDialog.Builder shareBottomSheetDialog = new ShareBottomSheetDialog.Builder(getSupportFragmentManager());
                shareBottomSheetDialog.isFullScreen(false);
                shareBottomSheetDialog.setCancelable(false);
                shareBottomSheetDialog.setTitle("Share");
                shareBottomSheetDialog.setMessage(new ShareBottomSheetDialogInterface.OnCustomMessage() {
                    @Override
                    public String onChooseApps(ResolveInfo resolveInfo) {
                        String message = "Share this link!";
                        if (resolveInfo.activityInfo.packageName.contains("com.whatsapp")) {
                            message = "Share this link via whatsapp!";
                        } else if (resolveInfo.activityInfo.packageName.contains("com.facebook") && resolveInfo.activityInfo.name.contains("com.facebook.composer.shareintent")) {
                            message = "Share this link via facebook!";
                        } else if (resolveInfo.activityInfo.packageName.contains("com.facebook") && resolveInfo.activityInfo.name.contains("com.facebook.messenger.intents")) {
                            message = "Share this link via facebook messenger";
                        } else if (resolveInfo.activityInfo.name.contains("com.google.android.apps.docs.drive.clipboard.SendTextToClipboardActivity")) {
                            message = "Share this link via copy link!";
                        }
                        return message;
                    }
                });
                shareBottomSheetDialog.show();
            }
        });
    }
}
