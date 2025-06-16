package com.example.task12drawoverotherapp


import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.*
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import java.io.File

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingIcon: View
    private var popupView: View? = null
    private lateinit var fileAdapter: FileAdapter
    private var fileObserver: FileObserver? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        showFloatingIcon()
        //ok
    }




    private fun showFloatingIcon() {
       floatingIcon = LayoutInflater.from(this).inflate(R.layout.floating_icon, null)

       val params = WindowManager.LayoutParams(
           WindowManager.LayoutParams.WRAP_CONTENT,
           WindowManager.LayoutParams.WRAP_CONTENT,
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
               WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
           else WindowManager.LayoutParams.TYPE_PHONE,
           WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
           PixelFormat.TRANSLUCENT
       )

       params.gravity = Gravity.TOP or Gravity.START
       params.x = 100
       params.y = 300

       windowManager.addView(floatingIcon, params)

       val iconView = floatingIcon.findViewById<ImageView>(R.id.floatingIcon)

       Glide.with(this)
           .load(R.drawable.devsky_logo)
           .circleCrop()
           .into(iconView)

       var initialX = 0
       var initialY = 0
       var initialTouchX = 0f
       var initialTouchY = 0f

       iconView.setOnTouchListener { _, event ->
           when (event.action) {
               MotionEvent.ACTION_DOWN -> {
                   initialX = params.x
                   initialY = params.y
                   initialTouchX = event.rawX
                   initialTouchY = event.rawY
                   true
               }

               MotionEvent.ACTION_MOVE -> {
                   params.x = initialX + (event.rawX - initialTouchX).toInt()
                   params.y = initialY + (event.rawY - initialTouchY).toInt()
                   windowManager.updateViewLayout(floatingIcon, params)
                   true
               }

               MotionEvent.ACTION_UP -> {
                   val deltaX = (event.rawX - initialTouchX).toInt()
                   val deltaY = (event.rawY - initialTouchY).toInt()
                   if (Math.abs(deltaX) < 10 && Math.abs(deltaY) < 10) {
                       if (popupView == null) showPopupWindow() else removePopup()
                   }
                   true
               }

               else -> false
           }
       }
   }

    /*private fun showFloatingIcon() {
        floatingIcon = LayoutInflater.from(this).inflate(R.layout.floating_icon, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
=======
    private fun showFloatingIcon() {
        floatingIcon = LayoutInflater.from(this).inflate(R.layout.floating_icon, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
>>>>>>> 37a384d12a41c4db8da5e48b5a814fec5254db33
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 300

        windowManager.addView(floatingIcon, params)

<<<<<<< HEAD
        val iconView = floatingIcon.findViewById<ImageView>(R.id.floatingIcon)

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        iconView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(floatingIcon, params)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    // Optional: handle tap separately if needed
                    val deltaX = (event.rawX - initialTouchX).toInt()
                    val deltaY = (event.rawY - initialTouchY).toInt()
                    if (Math.abs(deltaX) < 10 && Math.abs(deltaY) < 10) {
                        if (popupView == null) showPopupWindow() else removePopup()
                    }
                    true
                }

                else -> false
            }
        }
    }*/

    private fun showPopupWindow() {
        // Inflate the popup layout
        popupView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null)

        // Define layout parameters
        val popupParams = WindowManager.LayoutParams(
            800, // Width of popup in pixels
            1000, // Height of popup in pixels
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // Position popup relative to the floating icon
        val iconParams = floatingIcon.layoutParams as WindowManager.LayoutParams
        popupParams.x = iconParams.x
        popupParams.y = iconParams.y + floatingIcon.height + 20 // 20px margin below icon
        popupParams.gravity = Gravity.TOP or Gravity.START

        // Add the popup view to the WindowManager
        windowManager.addView(popupView, popupParams)

        // Setup RecyclerView to show files
        val recyclerView = popupView!!.findViewById<RecyclerView>(R.id.popupRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fileAdapter = FileAdapter()
        recyclerView.adapter = fileAdapter
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val allFiles = dir.listFiles()?.filter {
            it.isFile || it.isDirectory // Include both files and folders
        } ?: emptyList()
        fileAdapter.submitList(allFiles)


        fileObserver = @RequiresApi(Build.VERSION_CODES.Q)
        object : FileObserver(allFiles, CREATE or DELETE or MOVED_FROM or MOVED_TO) {
            override fun onEvent(event: Int, path: String?) {
                Handler(Looper.getMainLooper()).post {
                    fileAdapter.submitList(allFiles)
                }
            }
        }
        fileObserver?.startWatching()
        // Start file observer if needed
       // startWatchingDownloads()
    }

    /*private fun showPopupWindow() {
=======
        floatingIcon.findViewById<ImageView>(R.id.floatingIcon).setOnClickListener {
            if (popupView == null) {
                showPopupWindow()
            } else {
                removePopup()
            }
        }
    }

    private fun showPopupWindow() {
>>>>>>> 37a384d12a41c4db8da5e48b5a814fec5254db33
        popupView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null)
        val recyclerView = popupView!!.findViewById<RecyclerView>(R.id.popupRecyclerView)
        fileAdapter = FileAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fileAdapter

        val layoutParams = WindowManager.LayoutParams(
            600, 800,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.CENTER
        windowManager.addView(popupView, layoutParams)

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        fileAdapter.submitList(dir.listFiles()?.toList() ?: emptyList())

        fileObserver = @RequiresApi(Build.VERSION_CODES.Q)
        object : FileObserver(dir, CREATE or DELETE or MOVED_FROM or MOVED_TO) {
            override fun onEvent(event: Int, path: String?) {
                Handler(Looper.getMainLooper()).post {
                    fileAdapter.submitList(dir.listFiles()?.toList() ?: emptyList())
                }
            }
        }
        fileObserver?.startWatching()
<<<<<<< HEAD
    }*/


    private fun removePopup() {
        popupView?.let { windowManager.removeView(it) }
        popupView = null
        fileObserver?.stopWatching()
    }

    override fun onDestroy() {
        try {
            windowManager.removeView(floatingIcon)
            popupView?.let { windowManager.removeView(it) }
        } catch (_: Exception) {}
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

