package com.example.task12drawoverotherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var fileAdapter: FileAdapter
    private lateinit var fileObserver: FileObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start overlay service
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivityForResult(intent, 123)
        } else {
            startService(Intent(this, OverlayService::class.java))
        }*/
        requestStoragePermission()

        val recyclerView = findViewById<RecyclerView>(R.id.fileListView)
        fileAdapter = FileAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fileAdapter


        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        updateFileList(dir)

        fileObserver = @RequiresApi(Build.VERSION_CODES.Q)
        object : FileObserver(dir, CREATE or DELETE or MOVED_FROM or MOVED_TO) {
            override fun onEvent(event: Int, path: String?) {
                runOnUiThread { updateFileList(dir) }
            }
        }
        fileObserver.startWatching()
    }

    private fun updateFileList(dir: File) {
        val allFiles = dir.listFiles()?.filter {
            it.isFile || it.isDirectory // Include both files and folders
        } ?: emptyList()
        fileAdapter.submitList(allFiles)
        fileAdapter.submitList(dir.listFiles()?.toList() ?: emptyList())
    }

    override fun onDestroy() {
        stopService(Intent(this, OverlayService::class.java))
        super.onDestroy()
        fileObserver.stopWatching()

    }
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                startService(Intent(this, OverlayService::class.java))
            } else {
                Toast.makeText(this, "Overlay permission required", Toast.LENGTH_LONG).show()
            }
        }
    }*/

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivityForResult(intent, 123)
        } else {
            startService(Intent(this, OverlayService::class.java))

        }
    }
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
            requestPermissions(permissions, 101)
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            } else {
               // loadDownloadsFromMediaStore()
            }
        }
    }



}
