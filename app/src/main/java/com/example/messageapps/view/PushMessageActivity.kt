package com.example.messageapps.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.messageapps.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PushMessageActivity : AppCompatActivity() {

    lateinit var phoneEdt: EditText
    lateinit var messageEdt: EditText
    lateinit var sendMsgBtn: Button
    private lateinit var  progressBar: ProgressDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_message)

        phoneEdt = findViewById(R.id.idEdtPhone)
        messageEdt = findViewById(R.id.idEdtMessage)
        sendMsgBtn = findViewById(R.id.idBtnSendMessage)


        progressBar = ProgressDialog(this)
        progressBar.setMessage("please wait...")


        sendMsgBtn.setOnClickListener {

            initialPermission()

        }
    }

    private fun initialPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                "android.permission.INTERNET",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.CALL_PHONE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.SEND_SMS",
                "android.permission.READ_SMS",
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    sendMessage()

                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {

                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings

                        //   Toast.makeText(applicationContext, " permission is denied  user to app settings", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    @SuppressLint("SuspiciousIndentation")
    private  fun  sendMessage(){

        val phoneNumber = phoneEdt.text.toString()
        val message = messageEdt.text.toString()


        if(phoneEdt.text.isEmpty()){

            Toast.makeText(applicationContext, "Phone Number is Empty?", Toast.LENGTH_LONG).show()

        }else if(message.isEmpty()){
            Toast.makeText(applicationContext, "Message is Empty?", Toast.LENGTH_LONG).show()

        }else{

            progressBar.show()

            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    phoneNumber,
                    null,
                    message,
                    null,
                    null
                )
                Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

                val intent = Intent(this, DetailsActivity::class.java)
                startActivity(intent)
                progressBar.dismiss()


              notification(message,"Message Alert")
            } catch (e: Exception) {

                Toast.makeText(applicationContext, "Error.."+e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }


    @SuppressLint("MissingPermission")
    fun notification(message:String, title:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("Channel", "Channel", NotificationManager.IMPORTANCE_HIGH)
            val manager = applicationContext.getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, "Channel"
        )
            .setContentTitle(title)
            .setSmallIcon(R.drawable.notification_prog)
            .setContentText(message)
            .setAutoCancel(true) //.setSound(Uri.)
            .setWhen(System.currentTimeMillis())

        val notificationManagerCompat = NotificationManagerCompat.from(
            applicationContext
        )
        notificationManagerCompat.notify(1, notification.build())

    }
}