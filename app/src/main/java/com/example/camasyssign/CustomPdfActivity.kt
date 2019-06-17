package com.example.camasyssign

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.pspdfkit.signatures.SignatureManager
import com.pspdfkit.ui.PdfActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CustomPdfActivity : PdfActivity() {

    var docName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signer = ExternalSigner("Camasys")
        signer.init()
        SignatureManager.addSigner("camasys-signer", signer)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Call the super implementation so PSPDFKit can add its own icons to the menu. This has
        // to be called first or your changes will be overwritten.
        super.onPrepareOptionsMenu(menu)

        // Finds the search action by its ID and hides it.
        menu.findItem(MENU_OPTION_SEARCH).isVisible = false
//        menu.findItem(MENU_OPTION_SHARE).isVisible = false
        menu.findItem(MENU_OPTION_OUTLINE).isVisible = false
        menu.findItem(R.id.doc_signing_complete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onGenerateMenuItemIds(menuItems: MutableList<Int>): List<Int> {
        // For example let's say we want to add custom menu items after the outline button.
        // First, we get an index of outline button (all default button ids can be retrieved
        // via MENU_OPTION_* variables defined in the PdfActivity.
        val indexOfOutlineButton = menuItems.indexOf(MENU_OPTION_THUMBNAIL_GRID)

        // Add items after the outline button.
        menuItems.add(indexOfOutlineButton + 1, R.id.doc_signing_complete)

        // Return new menu items order.
        return menuItems
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // This will populate menu with items ordered as specified in onGenerateMenuItemIds().
        super.onCreateOptionsMenu(menu)

        val signCompletedMenu = menu.findItem(R.id.doc_signing_complete)
        signCompletedMenu.title = "Done"
        signCompletedMenu.setIcon(R.drawable.ic_check_black_24dp)

        val a = theme.obtainStyledAttributes(
            null,
            R.styleable.pspdf__ActionBarIcons,
            R.attr.pspdf__actionBarIconsStyle,
            R.style.PSPDFKit_ActionBarIcons
        )
        val mainToolbarIconsColor = a.getColor(R.styleable.pspdf__ActionBarIcons_pspdf__iconsColor, ContextCompat.getColor(this, R.color.white))
        a.recycle()

        val signCompletedMenuIcon = signCompletedMenu.icon
        DrawableCompat.setTint(signCompletedMenuIcon, mainToolbarIconsColor)
        signCompletedMenu.icon = signCompletedMenuIcon

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    copyAndFinish()
                } else {
                    Toast.makeText(this, "UNABLE TO COPY", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val handled = when (item.itemId) {
            R.id.doc_signing_complete -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)

                } else {
                    copyAndFinish()
                }
                true
            }

            else -> {
                false
            }
        }

        // Return true if you have handled the current event. If your code has not handled the event,
        // pass it on to the superclass. This is important or standard PSPDFKit actions won't work.
        return handled || super.onOptionsItemSelected(item)
    }

    private fun copyAndFinish() {
        val resultIntent = Intent()
        try {
            Log.i("Test", "Copying")
            val input = File(pdfFragment?.document?.documentSource?.fileUri?.path).inputStream()
            val out = FileOutputStream(File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "sign" + File.separator + "contract.pdf"))
            input.copyTo(out)
            input.close()
            out.close()
            Log.i("Test", "Copy done")
        } catch (e: FileNotFoundException) {
            Log.i("Test", "Error copying FileNotFoundException - " + e.localizedMessage)
        } catch (e: IOException) {
            Log.i("Test", "Error copying FileNotFoundException - " + e.localizedMessage)
        }
        resultIntent.putExtra("RESPONSE", Environment.getExternalStorageDirectory().absolutePath + File.separator + "sign" + File.separator + "contract.pdf")
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}