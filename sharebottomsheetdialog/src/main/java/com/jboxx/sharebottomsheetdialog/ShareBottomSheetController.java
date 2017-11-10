package com.jboxx.sharebottomsheetdialog;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rifqi @jboxxpradhana
 */

class ShareBottomSheetController {

    private Context mContext;
    private ShareBottomSheetDialog mDialog;

    private TextView mTitleView;

    private StringBuilder populateUtm = new StringBuilder();
    private StringBuilder message = new StringBuilder();
    private String extraString;
    private String url;
    private boolean enabledUtmSource = false;
    private ShareBottomSheetDialogInterface.OnCustomUtmSource customUtmSourceCallback;
    private ShareBottomSheetDialogInterface.OnCustomMessage customMessageCallback;

    protected ShareBottomSheetController() {}

    protected void initiateView(Context context, final ShareBottomSheetDialog dialog, final View view) {
        this.mContext = context;
        this.mDialog = dialog;

        mTitleView = view.findViewById(R.id.tv_share_title);
        final RecyclerView mRecyclerView = view.findViewById(R.id.rv_share_apps);

        final ApplicationsAdapter mAdapter = new ApplicationsAdapter(mContext, showAllShareApp(),
                new ApplicationsAdapter.ApplicationsAdapterCallback() {
                    @Override public void onChooseApps(ResolveInfo resolveInfo) {
                        if(enabledUtmSource && customUtmSourceCallback != null) {
                            String utm_source = customUtmSourceCallback.onChooseApps(resolveInfo);
                            if(!TextUtils.isEmpty(utm_source)){
                                try {
                                    setUtm(UTMConstants.UTM_SOURCE, utm_source);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(customMessageCallback != null) {
                            if(!TextUtils.isEmpty(customMessageCallback.onChooseApps(resolveInfo))){
                                setMessage(customMessageCallback.onChooseApps(resolveInfo));
                            }
                        }
                        doShare(resolveInfo, populateMessage().toString());
                    }
                });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @TargetApi(21)
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(dy==0){
                        mTitleView.setElevation(dpFromPx(0));
                    } else {
                        mTitleView.setElevation(dpFromPx(2));
                    }
                }
            }
        });
    }

    protected void setTitle(CharSequence title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    protected void setListenerCustomUtm(ShareBottomSheetDialogInterface.OnCustomUtmSource listenerCustomUtm) {
        customUtmSourceCallback = listenerCustomUtm;
    }

    protected void setMessage(String extraString) {
        this.extraString = extraString;
    }

    protected void setMessage(ShareBottomSheetDialogInterface.OnCustomMessage listenerCustomMessage) {
        customMessageCallback = listenerCustomMessage;
    }

    protected void setUtm(String key, String value) throws UnsupportedEncodingException {
        this.populateUtm.append("&" + key + "=" + URLEncoder.encode(value, "utf-8"));
    }

    protected void setEnabledUtmSource(boolean enabledUtmSource) {
        this.enabledUtmSource = enabledUtmSource;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    private List<ResolveInfo> showAllShareApp() {
        java.util.List<ResolveInfo> mApps;
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.setType("text/plain");
        PackageManager pManager = mContext.getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.GET_SHARED_LIBRARY_FILES);
        return mApps;
    }

    private float dpFromPx(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    private void doShare(ResolveInfo resolveInfo, String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);

        if (resolveInfo != null) {
            sendIntent.setComponent(new ComponentName(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.name));
        }
        sendIntent.setType("text/plain");
        mContext.startActivity(sendIntent);
        mDialog.dismiss();
    }

    private StringBuilder populateMessage(){
        if(!TextUtils.isEmpty(extraString)) {
            message.append(extraString);
        }
        if(!TextUtils.isEmpty(url)) {
            if(!TextUtils.isEmpty(extraString)) {
                message.append("\n\n");
            }
            message.append(url);
            if(!TextUtils.isEmpty(populateUtm)) {
                if(!message.toString().contains("?")) {
                    message.append("?");
                }
                if(url.charAt(url.length()-1) == '&') {
                    message = new StringBuilder(message.substring(0, message.length()-1));
                }
                message.append(populateUtm.substring(1, populateUtm.length()));
            }
        }
        return message;
    }

    protected static class ShareDialogParam {

        protected boolean isFullScreen = false;
        protected boolean isCancelable = true;

        protected String title;
        protected ShareBottomSheetDialogInterface.OnCustomUtmSource mCustomUtmSourceListener;
        protected ShareBottomSheetDialogInterface.OnCustomMessage mCustomMessageListener;
        protected boolean enabledUtmSource = false;
        protected String extraString;
        protected String url;
        protected HashMap<String, String> utms = new LinkedHashMap<>();

        protected ShareDialogParam() {}

        protected void apply(ShareBottomSheetController dialog) {
            if(title != null) {
                dialog.setTitle(title);
            }
            dialog.setEnabledUtmSource(enabledUtmSource);

            if(mCustomUtmSourceListener != null) {
                dialog.setListenerCustomUtm(mCustomUtmSourceListener);
            } else {
                dialog.setListenerCustomUtm(new DefaultUtmSourceCallback() {});
            }

            if(utms != null && !utms.isEmpty() && utms.size() > 0) {
                for(Map.Entry<String, String> entry : utms.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    try {
                        dialog.setUtm(key, value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(extraString != null) {
                dialog.setMessage(extraString);
            }
            if(mCustomMessageListener != null) {
                dialog.setMessage(mCustomMessageListener);
            }
            if(url != null) {
                dialog.setUrl(url);
            }
        }
    }

}
