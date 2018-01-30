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

    /**
     * Constructor of the share bottom sheet dialog, to create bottom sheet dialog and controller as well
     */
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

    /**
     * @param isFullscreen flag that you want to make bottom sheet fullscreen or not
     * the default is `false`
     */
    public void isFullScreen(boolean isFullscreen) {
        this.isFullScreen = isFullscreen;
    }

    /**
     * @param title flag that you want to set title of bottom
     * the default is `send to`
     */
    public void setTitle(@NonNull String title) {
        shareBottomSheetController.setTitle(title);
    }


    /**
     * @param extraString fill message if you want to give messages into content
     * don't set the message if you don't want to give string content
     */
    public void setMessage(@NonNull String extraString) {
        shareBottomSheetController.setMessage(extraString);
    }

    /**
     * set listener if you want to give callback and set your content messages
     * @param listener that can give u callback `Resolve Info` object so you can manipulate messages
     *                 depends on user selection
     */
    public void setMessage(@NonNull ShareBottomSheetDialogInterface.OnCustomMessage listener) {
        shareBottomSheetController.setMessage(listener);
    }

    /**
     * set base url that u want to share
     * @param url can contain domain, path, and parameter that u needed
     */
    public void setUrl(@NonNull String url) {
        shareBottomSheetController.setUrl(url);
    }

    /**
     * set listener if you want to give callback and set your `utm_source`
     * @param listener providing callback `Resolve Info` triggering by user selecting another apps
     *                 so you can manipulate utm_source depends on apps that user selected
     * if you want to constant `utm_source` just set string return type
     */
    public void addParameterWithCallback(@NonNull String param, @NonNull ShareBottomSheetDialogInterface.OnCustomUtmSource listener) {
        shareBottomSheetController.setListenerParam(param, listener);
    }

    /**
     * set another `utm` parameter that u need included into your url
     * @param value set value that u want
     * @param param set param that u need to add into paramater
     */
    public void addParameter(@NonNull String param, @NonNull String value) {
        if (!TextUtils.isEmpty(param) && !TextUtils.isEmpty(value)) {
            try {
                shareBottomSheetController.setParameter(param, value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Builder {

        private static String TAG = ShareBottomSheetDialog.class.getSimpleName();
        private final ShareBottomSheetController.ShareDialogParam param;
        private final FragmentManager fragmentManager;

        /**
         * Creates a builder for an bottom sheet dialog that uses the default
         * <p>
         * @param fragmentManager the fragment manager of the activity
         */
        public Builder(FragmentManager fragmentManager) {
            this(fragmentManager, TAG);
        }

        /**
         * Creates a builder for an bottom sheet dialog that uses the default
         * <p>
         * @param fragmentManager the fragment manager of the activity
         * @param TAG flag TAG for Bottom Sheet Dialog
         */
        public Builder(@NonNull FragmentManager fragmentManager, String TAG) {
            this.param = new ShareBottomSheetController.ShareDialogParam();
            this.fragmentManager = fragmentManager;
            this.TAG = TAG;
        }

        /**
         * @param isCancelable flag that you want to make bottom sheet cancelable or not
         * the default is `true`, set cancelable into `false` will forcing user to choose apps
         * that she/he need to pick
         */
        public Builder setCancelable(boolean isCancelable) {
            this.param.isCancelable = isCancelable;
            return this;
        }

        /**
         * @param isFullscreen flag that you want to make bottom sheet fullscreen or not
         * the default is `false`
         */
        public Builder isFullScreen(boolean isFullscreen) {
            this.param.isFullScreen = isFullscreen;
            return this;
        }

        /**
         * @param title flag that you want to set title of bottom
         * the default is `send to`
         */
        public Builder setTitle(@NonNull String title) {
            this.param.title = title;
            return this;
        }

        /**
         * @param extraString fill message if you want to give messages into content
         * don't set the message if you don't want to give string content
         */
        public Builder setMessage(@NonNull String extraString) {
            this.param.extraString = extraString;
            return this;
        }

        /**
         * set listener if you want to give callback and set your content messages
         * @param listener that can give u callback `Resolve Info` object so you can manipulate messages
         *                 depends on user selection
         */
        public Builder setMessage(@NonNull ShareBottomSheetDialogInterface.OnCustomMessage listener) {
            this.param.mCustomMessageListener = listener;
            return this;
        }

        /**
         * set base url that u want to share
         * @param url can contain domain, path, and parameter that u needed
         */
        public Builder setUrl(@NonNull String url) {
            this.param.url = url;
            return this;
        }

        /**
         * set listener if you want to give callback and set your `utm_source`
         * @param listener providing callback `Resolve Info` triggering by user selecting another apps
         *                 so you can manipulate utm_source depends on apps that user selected
         * if you want to constant `utm_source` just set string return type
         */
        public Builder addParameterWithCallback(@NonNull String param, @NonNull final ShareBottomSheetDialogInterface.OnCustomUtmSource listener) {
            if (!TextUtils.isEmpty(param)) {
                this.param.listOfListener.put(param, listener);
            }
            return this;
        }

        /**
         * set another `utm` parameter that u need included into your url
         * @param value set value that u want
         * @param param set param that u need to add into parameter
         */
        public Builder addParameter(@NonNull String param, @NonNull String value) {
            if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(param)) {
                this.param.anotherParam.put(param, value);
            }
            return this;
        }

        /**
         * Creates an {@link ShareBottomSheetDialog} with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        public ShareBottomSheetDialog create() {
            final ShareBottomSheetDialog shareBottomSheetDialog = new ShareBottomSheetDialog();
            this.param.apply(shareBottomSheetDialog.shareBottomSheetController);
            Bundle args = new Bundle();
            args.putBoolean("fullscreen", param.isFullScreen);
            shareBottomSheetDialog.setCancelable(param.isCancelable);
            shareBottomSheetDialog.setArguments(args);
            return shareBottomSheetDialog;
        }

        /**
         * Creates an {@link ShareBottomSheetDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     ShareBottomSheetDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public ShareBottomSheetDialog show() {
            final ShareBottomSheetDialog shareBottomSheetDialog = create();
            shareBottomSheetDialog.show(fragmentManager, TAG);
            return shareBottomSheetDialog;
        }

    }

}
