package com.jboxx.bottomsheetdialog;

import android.app.Activity;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jboxx.sharebottomsheetdialog.CustomOnDismissListener;
import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialog;
import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialogInterface;
import com.jboxx.sharebottomsheetdialog.UTMConstants;

/**
 * Created by Rifqi
 */

public class JavaActivity extends AppCompatActivity {

    private static final int KOTLIN_RC = 666;

    EditText edtCopy;
    ShareBottomSheetDialog.Builder shareBottomSheetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final Button shareButton1 = findViewById(R.id.shareBtn1);
        final Button shareButton2 = findViewById(R.id.shareBtn2);
        final Button btnSwitchActivity = findViewById(R.id.BtnSwitchActivity);
        edtCopy = findViewById(R.id.edtCopy);

        btnSwitchActivity.setText("To Kotlin Activity");
        btnSwitchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JavaActivity.this, MainActivity.class);
                startActivityForResult(intent, KOTLIN_RC);
            }
        });

        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (clipboard.hasPrimaryClip()) {
                    android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
                    android.content.ClipData data = clipboard.getPrimaryClip();
                    if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                        edtCopy.setText(data.getItemAt(0).getText().toString());
                }
            }
        });

        shareButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtCopy.setText("");
                new ShareBottomSheetDialog.Builder(getSupportFragmentManager())
                        .setCancelable(false)
                        .setFullScreen(false)
                        .setMessage(new ShareBottomSheetDialogInterface.OnCustomMessage() {
                            @Override
                            public String onChooseApps(ResolveInfo resolveInfo) {
                                return "Share this link!";
                            }
                        })
                        .setUrl("https://www.youtube.com/watch?v=2ThFJODUZ_4")
                        .addParameter(UTMConstants.UTM_TERM, "A.O.V")
                        .addParameter(UTMConstants.UTM_CAMPAIGN, "video-miwon")
                        .addParameterWithCallback(UTMConstants.UTM_SOURCE, new ShareBottomSheetDialogInterface.OnCustomParameter() {
                            @Override
                            public String onChooseApps(ResolveInfo resolveInfo) {
                                Log.d(JavaActivity.class.getSimpleName(), "packageName " + resolveInfo.activityInfo.packageName + " name " + resolveInfo.activityInfo.name);
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
                        .addParameterWithCallback(UTMConstants.UTM_CONTENT, new ShareBottomSheetDialogInterface.OnCustomParameter() {
                            @Override
                            public String onChooseApps(ResolveInfo resolveInfo) {
                                return "video";
                            }
                        })
                        .setOnDismissListener(new CustomOnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog, String content) {
                                super.onDismiss(dialog, content);
                                Toast.makeText(JavaActivity.this, content, Toast.LENGTH_LONG).show();
                            }
                        })
                        .showAllowingStateLoss();
            }
        });

        shareButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtCopy.setText("");
                shareBottomSheetDialog = new ShareBottomSheetDialog.Builder(getSupportFragmentManager());
                shareBottomSheetDialog.setFullScreen(true);
                shareBottomSheetDialog.setCancelable(false);
                shareBottomSheetDialog.setTitle("Share");
                shareBottomSheetDialog.setExtraSubject("This is subject");
                shareBottomSheetDialog.setMessage(new ShareBottomSheetDialogInterface.OnCustomMessage() {
                    @Override
                    public String onChooseApps(ResolveInfo resolveInfo) {
                        Log.d(JavaActivity.class.getSimpleName(), "packageName " + resolveInfo.activityInfo.packageName + " name " + resolveInfo.activityInfo.name);
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
                shareBottomSheetDialog.setOnDismissListener(new CustomOnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog, String content) {
                        Toast.makeText(JavaActivity.this, content, Toast.LENGTH_LONG).show();
                    }
                });
                if (!shareBottomSheetDialog.isAdded()) {
                    shareBottomSheetDialog.show();
                    Log.d(JavaActivity.class.getSimpleName(), "" + shareBottomSheetDialog.isAdded());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == KOTLIN_RC) {
                edtCopy.setText("");
            }
        }
    }
}
