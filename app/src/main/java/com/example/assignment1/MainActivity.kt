package com.example.assignment1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        findViewById<TextView>(R.id.mengrong).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.wesley).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.jon).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.keefe).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.minyao).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.lynette).setOnClickListener(::clickName)
        */


        findViewById<TextView>(R.id.itBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.constrBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.eduBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.enterBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.financeBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.fnbBtn).setOnClickListener(::contactus)

        findViewById<TextView>(R.id.debugBtn).setOnClickListener(::debugPage)
    }

    /*
    private fun clickName(view: View) {
        val intent = Intent(this@MainActivity, InfoActivity::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }
     */

    private fun debugPage(view: View) {
        val intent = Intent(this@MainActivity, ProfileTemplate::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }

    private fun profile(view: View) {
        val intent = Intent(this@MainActivity, ProfileTemplate::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }

    private fun contactus(view: View) {
        val intent = Intent(this@MainActivity, ContactTemplate::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }

    // Testing---------------------------
    private fun storagePage(view: View) {
        if (checkPerms()) {
            openPage()
        }
        else {
            requestPerms()
        }
    }

    private fun checkPerms(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            }
            catch (e: Exception){
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }
        else{
            //Android is below 11(R)
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                100           )
        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //here we will handle the result of our intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            if (Environment.isExternalStorageManager()){
                //Manage External Storage Permission is granted
                toast("Manage External Storage Permission is allowed")
                openPage()
            }
            else{
                //Manage External Storage Permission is denied....
                toast("Manage External Storage Permission is denied")
            }
        }
        else {
            //Android is below 11(R)
        }
    }

    private fun openPage() {
        val intent = Intent(this@MainActivity, ProfileTemplate::class.java)
        val path = Environment.getExternalStorageDirectory().path
        intent.putExtra("path", path)
        startActivity(intent)
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
