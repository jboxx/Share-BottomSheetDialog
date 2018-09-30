# Share-BottomSheetDialog
Share Bottom Sheet Dialog to help u customize parameters to URLs to identify the campaigns that refer traffic.

![](https://media.giphy.com/media/X8biWFH2h0QyMl9srk/giphy.gif) ![](https://media.giphy.com/media/oFucz2imdVLzd3VFEV/giphy.gif)

# Download Repository

The Gradle dependency is available via [jCenter][1]. jCenter is the default Maven repository used by Android Studio.

The minimum API level supported by this library is API 15.

    compile 'com.jboxx.sharebottomsheetdialog:sharebottomsheetdialog:0.6.1'
        

[1]: https://bintray.com/jboxx/Share-BottomSheetDialog/sharebottomsheetdialog/view

# Usage

### Java
    ShareBottomSheetDialog.Builder shareBottomSheetDialog shareBottomSheetDialog = new ShareBottomSheetDialog.Builder(getSupportFragmentManager());
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
                
## Or

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
                                } else if (resolveInfo.activityInfo.packageName.contains("com.twitter")) {
                                    utmSource = "from twitter";
                                } else if (resolveInfo.activityInfo.packageName.contains("org.telegram")) {
                                    utmSource = "from telegram";
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
### Kotlin
    ShareBottomSheetDialog.Builder(supportFragmentManager)
                    .setFullScreen(true)
                    .setCancelable(true)
                    .setUrl("https://www.youtube.com/watch?v=QBGaO89cBMI&")
                    .setExtraSubject({resolveInfo: ResolveInfo -> String
                        var extraSubject = "Subject for All"
                        if (resolveInfo.activityInfo.packageName.contains("android.gm")) {
                            extraSubject = "Subject for Gmail";
                        } else if (resolveInfo.activityInfo.packageName.contains("android.email")) {
                            extraSubject = "Subject for Android mail";
                        } else if (resolveInfo.activityInfo.packageName.contains("android.apps.inbox")) {
                            extraSubject = "Subject for Inbox by Gmail";
                        }
                        return@setExtraSubject extraSubject
                    })
                    .addParameterWithCallback(UTMConstants.UTM_SOURCE, ShareBottomSheetDialogInterface.OnCustomParameter { resolveInfo -> "everywhere" })
                    .show()
                    
For more example details can see [this][2]

[2]: https://github.com/jboxx/Share-BottomSheetDialog/tree/master/sample


