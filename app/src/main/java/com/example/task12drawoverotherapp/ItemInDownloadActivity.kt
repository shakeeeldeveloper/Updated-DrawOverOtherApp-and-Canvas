package com.example.task12drawoverotherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemInDownloadActivity : AppCompatActivity() {
    private lateinit var adapter: DownloadAdapter

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_in_download)
        val recyclerView = findViewById<RecyclerView>(R.id.fileListView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = DownloadAdapter()
        recyclerView.adapter = adapter

        requestStoragePermission()
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadDownloadsFromMediaStore() {
        val projection = arrayOf(
            MediaStore.Downloads._ID,
            MediaStore.Downloads.DISPLAY_NAME,
            MediaStore.Downloads.SIZE
        )

        val downloads = mutableListOf<String>()
        val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.Downloads.DATE_ADDED} DESC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                downloads.add(name)

            }
        }

        // Update RecyclerView
        adapter.submitList(downloads)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
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
                loadDownloadsFromMediaStore()
            }
        }
    }


}