package com.example.messageapps.view

import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messageapps.R


class DetailsActivity : AppCompatActivity() {


    private lateinit var  numberTV: TextView
    private lateinit var bodyTV: TextView
    private lateinit var dateTV: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        numberTV = findViewById(R.id.idTVNumber)
        bodyTV = findViewById(R.id.idTVSMSBody)
        dateTV = findViewById(R.id.idTVSMSDate)


        val cr = applicationContext.contentResolver

        val c: Cursor? = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)

        if (c != null) {

            if (c.moveToFirst()) {

               // var smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));

                var number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))

                var body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY))


               //  c.moveToNext()

                numberTV.text ="Sender Number : " + number
                bodyTV.setText("Body is : " + body)

                c.close()
            } else {
                // on below line displaying toast message if there is no sms present in the device.
                Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}