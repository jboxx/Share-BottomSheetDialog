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
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Rifqi @jboxxpradhana
 */

public class ShareBottomSheetDialog extends BottomSheetDialogFragment {

    private ShareBottomSheetController shareBottomSheetController;

    private boolean isFullScreen = false;
    private boolean isCancelable = true;
    private DialogInterface.OnDismissListener dismissListener;

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
                if(dismissListener != null) {
                    bottomSheetDialog.setOnDismissListener(dismissListener);
                }
                bottomSheetDialog.setCancelable(isCancelable);
                FrameLayout bottomSheetLayout = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
                if(bottomSheetLayout != null){
                    final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
                    if(isFullScreen) {
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
     * @param isCancelable flag that you want to make bottom sheet cancelable or not
     * the default is `true`, set cancelable into `false` will forcing user to choose apps
     * that she/he need to pick
     */
    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    /**
     * @param isFullscreen flag that you want to make bottom sheet fullscreen or not
     * the default is `false`
     */
    private void setFullScreen(boolean isFullscreen) {
        this.isFullScreen = isFullscreen;
    }

    /**
     * @param dismissListener listener if you want to give callback while user dismissing bottomsheet
     */
    private void setOnDismissListener(CustomOnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void showDialogAllowingStateLoss(FragmentManager fragmentManager, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public static class Builder {

        private static String TAG = ShareBottomSheetDialog.class.getSimpleName();
        private ShareBottomSheetDialog shareBottomSheetDialog;
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
         * @return true if ShareBottomSheetDialog was shown
         */
        public boolean isAdded() {
            return shareBottomSheetDialog != null && shareBottomSheetDialog.isAdded();
        }

        /**
         * @param isCancelable flag that you want to make bottom sheet cancelable or not
         * the default is `true`, set cancelable into `false` will forcing user to choose apps
         * that she/he need to pick
         */
        public Builder setCancelable(boolean isCancelable) {
            this.param.setCancelable(isCancelable);
            return this;
        }

        /**
         * @param isFullscreen flag that you want to make bottom sheet fullscreen or not
         * the default is `false`
         */
        public Builder setFullScreen(boolean isFullscreen) {
            this.param.setFullScreen(isFullscreen);
            return this;
        }

        /**
         * @param dismissListener listener if you want to give callback while user dismissing bottomsheet
         */
        public Builder setOnDismissListener(CustomOnDismissListener dismissListener) {
            this.param.setDismissListener(dismissListener);
            return this;
        }

        /**
         * @param title flag that you want to set title of bottom
         * the default is `send to`
         */
        public Builder setTitle(@NonNull String title) {
            this.param.setTitle(title);
            return this;
        }

        /**
         * @param extraSubject fill subject if you want to give subject into subject of messages
         * don't set the subject if you don't want to give subject to your messages
         */
        public Builder setExtraSubject(@NonNull String extraSubject) {
            this.param.setExtraSubject(extraSubject);
            return this;
        }

        /**
         * set listener if you want to give callback and set your content messages
         * @param mCustomExtraSubjectListener that can give u callback `Resolve Info` object so you can manipulate subject
         *                 depends on user selection
         */
        public Builder setExtraSubject(@NonNull ShareBottomSheetDialogInterface.OnCustomMessage mCustomExtraSubjectListener) {
            this.param.setmCustomExtraTitleListener(mCustomExtraSubjectListener);
            return this;
        }

        /**
         * @param extraString fill message if you want to give messages into content
         * don't set the message if you don't want to give string content
         */
        public Builder setMessage(@NonNull String extraString) {
            this.param.setExtraString(extraString);
            return this;
        }

        /**
         * set listener if you want to give callback and set your content messages
         * @param mCustomMessageListener that can give u callback `Resolve Info` object so you can manipulate messages
         *                 depends on user selection
         */
        public Builder setMessage(@NonNull ShareBottomSheetDialogInterface.OnCustomMessage mCustomMessageListener) {
            this.param.setmCustomMessageListener(mCustomMessageListener);
            return this;
        }

        /**
         * set base url that u want to share
         * @param url can contain domain, path, and parameter that u needed
         */
        public Builder setUrl(@NonNull String url) {
            this.param.setUrl(url);
            return this;
        }

        /**
         * set listener if you want to give callback and set your `utm_source`
         * @param listener providing callback `Resolve Info` triggering by user selecting another apps
         *                 so you can manipulate utm_source depends on apps that user selected
         * if you want to constant `utm_source` just set string return type
         */
        public Builder addParameterWithCallback(@NonNull String param, @NonNull final ShareBottomSheetDialogInterface.OnCustomParameter listener) {
            if (!TextUtils.isEmpty(param)) {
                this.param.setListOfListener(param, listener);
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
                this.param.setAnotherParam(param, value);
            }
            return this;
        }

        /**
         * fill package names which you wants to exclude in your share intent
         * @param excludePackageNames can contain array of String package name
         */
        public Builder excludePackageNames(String... excludePackageNames) {
            this.param.setExcludePackageNames(Arrays.asList(excludePackageNames));
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
        private ShareBottomSheetDialog create() {
            ShareBottomSheetDialog shareBottomSheetDialog = new ShareBottomSheetDialog();
            this.param.apply(shareBottomSheetDialog.shareBottomSheetController);
            shareBottomSheetDialog.setFullScreen(param.isFullScreen());
            shareBottomSheetDialog.setCancelable(param.isCancelable());
            shareBottomSheetDialog.setOnDismissListener(param.getDismissListener());
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
            if (!this.isAdded()) {
                shareBottomSheetDialog = create();
                fragmentManager.executePendingTransactions();
                shareBottomSheetDialog.show(fragmentManager, TAG);
            }
            return shareBottomSheetDialog;
        }

        public ShareBottomSheetDialog showAllowingStateLoss() {
            if (!this.isAdded()) {
                shareBottomSheetDialog = create();
                shareBottomSheetDialog.showDialogAllowingStateLoss(fragmentManager, TAG);
            }
            return shareBottomSheetDialog;
        }
    }

}
