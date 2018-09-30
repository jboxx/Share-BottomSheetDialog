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
import java.util.ArrayList;
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

    private StringBuilder populateParam = new StringBuilder();
    private StringBuilder message = new StringBuilder();
    private String extraString;
    private String extraSubject;
    private String url;
    private LinkedHashMap<String, ShareBottomSheetDialogInterface.OnCustomParameter> listOfListener = new LinkedHashMap<>();
    private List<String> excludePackageNames = new ArrayList<>();
    private ShareBottomSheetDialogInterface.OnCustomMessage customMessageCallback;
    private ShareBottomSheetDialogInterface.OnCustomMessage customExtraSubjectCallback;
    private CustomOnDismissListener dismissListener;

    protected ShareBottomSheetController() {}

    protected void initiateView(Context context, final ShareBottomSheetDialog dialog, final View view) {
        this.mContext = context;
        this.mDialog = dialog;

        mTitleView = view.findViewById(R.id.tv_share_title);
        final RecyclerView mRecyclerView = view.findViewById(R.id.rv_share_apps);

        final ApplicationsAdapter mAdapter = new ApplicationsAdapter(mContext, showAllShareApp(),
                new ApplicationsAdapter.ApplicationsAdapterCallback() {
                    @Override public void onChooseApps(ResolveInfo resolveInfo) {
                        if (listOfListener != null && !listOfListener.isEmpty() && listOfListener.size() > 0) {
                            for(Map.Entry<String, ShareBottomSheetDialogInterface.OnCustomParameter> entry : listOfListener.entrySet()) {
                                String key = entry.getKey();
                                ShareBottomSheetDialogInterface.OnCustomParameter listener = entry.getValue();
                                try {
                                    String value = listener.onChooseApps(resolveInfo);
                                    if(!TextUtils.isEmpty(value)) {
                                        setParameter(key, value);
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (customMessageCallback != null) {
                            if (!TextUtils.isEmpty(customMessageCallback.onChooseApps(resolveInfo))) {
                                setMessage(customMessageCallback.onChooseApps(resolveInfo));
                            }
                        }
                        if (customExtraSubjectCallback != null) {
                            if (!TextUtils.isEmpty(customExtraSubjectCallback.onChooseApps(resolveInfo))) {
                                setExtraSubject(customExtraSubjectCallback.onChooseApps(resolveInfo));
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

    private void setTitle(CharSequence title) {
        if (mTitleView != null && !TextUtils.isEmpty(title)) {
            this.mTitleView.setText(title);
        }
    }

    private void setDismissListener(CustomOnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void setExcludePackageNames(List<String> excludePackageNames) {
        this.excludePackageNames = excludePackageNames;
    }

    private void setListOfListener(LinkedHashMap<String, ShareBottomSheetDialogInterface.OnCustomParameter> listOfListener) {
        this.listOfListener = listOfListener;
    }

    private void setExtraSubject(String extraSubject) {
        if (!TextUtils.isEmpty(extraSubject)) {
            this.extraSubject = extraSubject;
        }
    }

    private void setExtraSubject(ShareBottomSheetDialogInterface.OnCustomMessage listenerCustomExtraSubject) {
        if (listenerCustomExtraSubject != null) {
            this.customExtraSubjectCallback = listenerCustomExtraSubject;
        }
    }

    private void setMessage(String extraString) {
        if (!TextUtils.isEmpty(extraString)) {
            this.extraString = extraString;
        }
    }

    private void setMessage(ShareBottomSheetDialogInterface.OnCustomMessage listenerCustomMessage) {
        if (listenerCustomMessage != null) {
            this.customMessageCallback = listenerCustomMessage;
        }
    }

    private void setParameter(String key, String value) throws UnsupportedEncodingException {
        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(value)) {
            this.populateParam.append("&" + key + "=" + URLEncoder.encode(value, "utf-8"));
        }
    }

    private void setUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
        }
    }

    private List<ResolveInfo> showAllShareApp() {
        List<ResolveInfo> mAllShareApps;
        List<ResolveInfo> targetedShareIntents = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.setType("text/plain");
        PackageManager pManager = mContext.getPackageManager();
        mAllShareApps = pManager.queryIntentActivities(intent,
                PackageManager.GET_SHARED_LIBRARY_FILES);
        if (!excludePackageNames.isEmpty()) {
            if (!mAllShareApps.isEmpty()) {
                for (ResolveInfo resolveInfo : mAllShareApps) {
                    if (!excludePackageNames.contains(resolveInfo.activityInfo.packageName)) {
                        targetedShareIntents.add(resolveInfo);
                    }
                }
            }
        } else {
            targetedShareIntents.addAll(mAllShareApps);
        }

        return targetedShareIntents;
    }

    private float dpFromPx(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    private void doShare(ResolveInfo resolveInfo, String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(extraSubject)) {
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        }
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
            if(url.charAt(url.length()-1) == '&') {
                message.append(url.substring(0, url.length()-1));
            } else {
                message.append(url);
            }

            if(!TextUtils.isEmpty(populateParam)) {
                if(!message.toString().contains("?")) {
                    message.append("?");
                    message.append(populateParam.substring(1, populateParam.length()));
                } else {
                    message.append(populateParam.substring(0, populateParam.length()));
                }
            }
        }
        if (dismissListener != null) {
            dismissListener.content = message.toString();
        }
        return message;
    }

    protected static class ShareDialogParam {

        private boolean isFullScreen = false;
        private boolean isCancelable = true;

        private CustomOnDismissListener dismissListener;

        private String title;
        private ShareBottomSheetDialogInterface.OnCustomMessage mCustomMessageListener;
        private ShareBottomSheetDialogInterface.OnCustomMessage mCustomExtraSubjectListener;
        private LinkedHashMap<String, ShareBottomSheetDialogInterface.OnCustomParameter> listOfListener = new LinkedHashMap<>();
        private List<String> excludePackageNames = new ArrayList<>();

        private String extraString;
        private String extraSubject;
        private String url;
        private HashMap<String, String> anotherParam = new LinkedHashMap<>();

        protected ShareDialogParam() {}

        protected void apply(ShareBottomSheetController dialog) {
            if(!TextUtils.isEmpty(title)) {
                dialog.setTitle(title);
            }

            if (!excludePackageNames.isEmpty()) {
                dialog.setExcludePackageNames(excludePackageNames);
            }

            if (!TextUtils.isEmpty(extraSubject)) {
                dialog.setExtraSubject(extraSubject);
            }

            if (mCustomExtraSubjectListener != null) {
                dialog.setExtraSubject(mCustomExtraSubjectListener);
            }

            if(!TextUtils.isEmpty(extraString)) {
                dialog.setMessage(extraString);
            }

            if(mCustomMessageListener != null) {
                dialog.setMessage(mCustomMessageListener);
            }

            if(!TextUtils.isEmpty(url)) {
                dialog.setUrl(url);
            }

            if(anotherParam != null && !anotherParam.isEmpty() && anotherParam.size() > 0) {
                for(Map.Entry<String, String> entry : anotherParam.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    try {
                        dialog.setParameter(key, value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(listOfListener != null && !listOfListener.isEmpty() && listOfListener.size() > 0) {
                dialog.setListOfListener(listOfListener);
            }

            if(dismissListener != null) {
                dialog.setDismissListener(dismissListener);
            }
        }

        protected boolean isFullScreen() {
            return isFullScreen;
        }

        protected void setFullScreen(boolean fullScreen) {
            isFullScreen = fullScreen;
        }

        protected boolean isCancelable() {
            return isCancelable;
        }

        protected void setCancelable(boolean cancelable) {
            isCancelable = cancelable;
        }

        protected CustomOnDismissListener getDismissListener() {
            return dismissListener;
        }

        protected void setDismissListener(CustomOnDismissListener dismissListener) {
            this.dismissListener = dismissListener;
        }

        protected String getTitle() {
            return title;
        }

        protected void setTitle(String title) {
            this.title = title;
        }

        protected void setmCustomExtraTitleListener(ShareBottomSheetDialogInterface.OnCustomMessage mCustomExtraSubjectListener) {
            this.mCustomExtraSubjectListener = mCustomExtraSubjectListener;
        }

        protected void setmCustomMessageListener(ShareBottomSheetDialogInterface.OnCustomMessage mCustomMessageListener) {
            this.mCustomMessageListener = mCustomMessageListener;
        }

        protected void setListOfListener(String param, ShareBottomSheetDialogInterface.OnCustomParameter listener) {
            this.listOfListener.put(param, listener);
        }

        protected void setExtraSubject(String extraSubject) {
            this.extraSubject = extraSubject;
        }

        protected void setExtraString(String extraString) {
            this.extraString = extraString;
        }

        protected void setUrl(String url) {
            this.url = url;
        }

        protected void setAnotherParam(String param, String value) {
            this.anotherParam.put(param, value);
        }

        protected void setExcludePackageNames(List<String> excludePackageNames) {
            this.excludePackageNames = excludePackageNames;
        }
    }
}
