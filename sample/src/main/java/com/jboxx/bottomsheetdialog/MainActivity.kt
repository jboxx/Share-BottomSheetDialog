package com.jboxx.bottomsheetdialog

import android.content.ClipDescription
import android.content.ClipboardManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.widget.Button
import android.widget.EditText
import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialog
import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialogInterface
import com.jboxx.sharebottomsheetdialog.UTMConstants
import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo


/**
 * Created by Rifqi
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val shareButton1 = findViewById<Button>(R.id.shareBtn1) as Button
        val shareButton2 = findViewById<Button>(R.id.shareBtn2) as Button
        val edtCopy = findViewById<EditText>(R.id.edtCopy)
        val btnSwitchActivity = findViewById<Button>(R.id.BtnSwitchActivity)
        btnSwitchActivity.text = "To Java Activity"
        btnSwitchActivity.setOnClickListener {
            clipboard.removePrimaryClipChangedListener(null)
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }


        clipboard.addPrimaryClipChangedListener(object : ClipboardManager.OnPrimaryClipChangedListener {
            override fun onPrimaryClipChanged() {
                val description = clipboard.primaryClipDescription
                val data = clipboard.primaryClip
                if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    edtCopy.setText(data.getItemAt(0).text.toString())
                }
            }
        })

        shareButton1.setOnClickListener {
            edtCopy.setText("")
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
        }

        shareButton2.setOnClickListener {
            edtCopy.setText("")
            val shareBottomSheet = ShareBottomSheetDialog.Builder(supportFragmentManager)
            shareBottomSheet.setUrl("https://code.tutsplus.com/articles/coding-functional-android-apps-in-kotlin-lambdas-null-safety-more--cms-27964")
            shareBottomSheet.excludePackageNames("com.google.android.gm", "com.facebook")
            shareBottomSheet.addParameter(UTMConstants.UTM_CONTENT,"carousel")
            shareBottomSheet.show()
        }
    }
}
