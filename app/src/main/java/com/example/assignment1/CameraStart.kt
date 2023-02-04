package com.example.assignment1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider

class CameraStart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id",0)
        renderInfo(id)

        findViewById<TextView>(R.id.takePictureBtn).setOnClickListener(::takePhoto)
    }
    private fun renderInfo(id: Int) {
        when (id) {
            R.id.cameraBtn -> setContentView(R.layout.activity_camera)
        }
    }

    private fun takePhoto(view: View) {
    }
}