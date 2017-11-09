package com.jboxx.bottomsheetdialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.jboxx.sharebottomsheetdialog.ShareBottomSheetDialog
import com.jboxx.sharebottomsheetdialog.UTMConstants

/**
 * Created by Rifqi
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shareButton1 = findViewById<Button>(R.id.shareBtn1) as Button
        val shareButton2 = findViewById<Button>(R.id.shareBtn2) as Button

        shareButton1.setOnClickListener {
            /*ShareBottomSheetDialog.Builder(supportFragmentManager)
                    .setCancelable(true)
                    .setUrl("https://www.youtube.com/watch?v=QBGaO89cBMI&")
                    .addUtmSource(false)
                    .customUtmSource { return@customUtmSource "everywhere" }
                    .show()*/
        }

        shareButton2.setOnClickListener {
            /*val shareBottomSheet = ShareBottomSheetDialog.Builder(supportFragmentManager)
            shareBottomSheet.setUrl("https://code.tutsplus.com/articles/coding-functional-android-apps-in-kotlin-lambdas-null-safety-more--cms-27964")
            shareBottomSheet.setUtm("carousel", UTMConstants.UTM_CONTENT)
            shareBottomSheet.addUtmSource(true)
            shareBottomSheet.show()*/
        }

    }
}
