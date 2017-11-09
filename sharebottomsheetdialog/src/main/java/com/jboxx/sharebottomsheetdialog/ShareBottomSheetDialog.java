package com.jboxx.sharebottomsheetdialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.UnsupportedEncodingException;

/**
 * Created by Rifqi @jboxxpradhana
 */

public class ShareBottomSheetDialog extends BottomSheetDialogFragment {

    private ShareBottomSheetController shareBottomSheetController;

    private boolean isFullScreen = false;

    public ShareBottomSheetDialog(){
        shareBottomSheetController = new ShareBottomSheetController();
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                FrameLayout bottomSheetLayout = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
                if(bottomSheetLayout != null){
                    final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
                    if(isFullScreen || getArguments().getBoolean("fullscreen", false)) {
                        bottomSheetBehavior.setPeekHeight(bottomSheetLayout.getHeight());
                    }
                }
            }
        });
        return dialog;
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        shareBottomSheetController.initiateView(getContext(),this, view);
        return view;
    }

    public void isFullScreen(boolean isFullscreen) {
        this.isFullScreen = isFullscreen;
    }

    public void setTitle(@NonNull String title) {
        shareBottomSheetController.setTitle(title);
    }

    public void setMessage(@NonNull String extraString) {
        shareBottomSheetController.setMessage(extraString);
    }

    public void setMessage(ShareBottomSheetDialogInterface.OnCustomMessage listener) {
        shareBottomSheetController.setMessage(listener);
    }

    public void setUrl(@NonNull String url) {
        shareBottomSheetController.setUrl(url);
    }

    public void customUtmSource(ShareBottomSheetDialogInterface.OnCustomUtmSource listener) {
        shareBottomSheetController.setListenerCustomUtm(listener);
    }

    public void addUtmSource(boolean isEnableUtmSource) {
        shareBottomSheetController.setEnabledUtmSource(isEnableUtmSource);
    }

    public void setUtm(@NonNull String value, @NonNull @UTMConstants.UTM String utm) {
        try {
            if(!TextUtils.isEmpty(value)) {
                shareBottomSheetController.setUtm(utm, value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static class Builder {

        private static String TAG = ShareBottomSheetDialog.class.getSimpleName();
        private final ShareBottomSheetController.ShareDialogParam param;
        private final FragmentManager fragmentManager;

        public Builder(FragmentManager fragmentManager) {
            this(fragmentManager, TAG);
        }

        public Builder(@NonNull FragmentManager fragmentManager, String TAG) {
            this.param = new ShareBottomSheetController.ShareDialogParam();
            this.fragmentManager = fragmentManager;
            this.TAG = TAG;
        }

        public Builder setCancelable(boolean isCancelable) {
            this.param.isCancelable = isCancelable;
            return this;
        }

        public Builder isFullScreen(boolean isFullscreen) {
            this.param.isFullScreen = isFullscreen;
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            this.param.title = title;
            return this;
        }

        public Builder setMessage(@NonNull String extraString) {
            this.param.extraString = extraString;
            return this;
        }

        public Builder setMessage(final ShareBottomSheetDialogInterface.OnCustomMessage listener) {
            this.param.mCustomMessageListener = listener;
            return this;
        }

        public Builder setUrl(@NonNull String url) {
            this.param.url = url;
            return this;
        }

        public Builder customUtmSource(final ShareBottomSheetDialogInterface.OnCustomUtmSource listener) {
            this.param.mCustomUtmSourceListener = listener;
            return this;
        }

        public Builder addUtmSource(boolean isEnableUtmSource) {
            this.param.enabledUtmSource = isEnableUtmSource;
            return this;
        }

        public Builder setUtm(@NonNull String value, @NonNull @UTMConstants.UTM String utm) {
            if(!TextUtils.isEmpty(value)) {
                this.param.utms.put(utm, value);
            }
            return this;
        }

        public ShareBottomSheetDialog create() {
            final ShareBottomSheetDialog shareBottomSheetDialog = new ShareBottomSheetDialog();
            this.param.apply(shareBottomSheetDialog.shareBottomSheetController);
            Bundle args = new Bundle();
            args.putBoolean("fullscreen", param.isFullScreen);
            shareBottomSheetDialog.setCancelable(param.isCancelable);
            shareBottomSheetDialog.setArguments(args);
            return shareBottomSheetDialog;
        }

        public ShareBottomSheetDialog show() {
            final ShareBottomSheetDialog shareBottomSheetDialog = create();
            shareBottomSheetDialog.show(fragmentManager, TAG);
            return shareBottomSheetDialog;
        }

    }

}
