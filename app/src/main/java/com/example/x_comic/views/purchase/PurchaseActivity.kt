package com.example.x_comic.views.purchase

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.google.firebase.storage.FirebaseStorage
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import jp.wasabeef.glide.transformations.BlurTransformation
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

class PurchaseActivity : AppCompatActivity() {
    val clientId = "AetqiuaX2ZoHcRciOFWmqHOGSUcasvGCdv0wqrqFsLYxezwE_f2mvjaWrR0nwmep-nSPp_b_CITJitd7"
    val PAYPAL_REQUEST_CODE = 123
    companion object {
        var configuration: PayPalConfiguration? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val intent = intent
        val stringData = intent.getStringExtra("bookdata")
        val bookData = stringData?.let{ Klaxon().parse<Product>(it)}
        var title = findViewById(R.id.book_title) as TextView;
        var author = findViewById(R.id.book_author) as TextView;
        var cover = findViewById(R.id.book_cover) as ImageView;
        var view = findViewById(R.id.viewTextView) as TextView;
        var favorite = findViewById(R.id.favoriteTextView) as TextView;
        var chapter = findViewById(R.id.numOfChapterTextView) as TextView;

        title.text = bookData?.title
        author.text = bookData?.author
        favorite.text = bookData?.favorite.toString()
        val backCover = findViewById<ImageView>(R.id.background)
        // Get a reference to the Firebase Storage instance
        val storage = FirebaseStorage.getInstance()
        val imageName = bookData?.cover // Replace with your image name
        val imageRef = storage.reference.child("book_cover/$imageName")
        imageRef.getBytes(Long.MAX_VALUE)
            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                // Set the Bitmap to the ImageView
                cover.setImageBitmap(bitmap)


                Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                    .into(backCover)
            }.addOnFailureListener {
                // Handle any errors
            }
        view.text = bookData?.view.toString()
        chapter.text = bookData?.chapters?.size.toString()

        val chapterData = intent.getStringExtra("chapter")
        val chapterPurchase = chapterData?.let{ Klaxon().parse<Chapter>(it)}
        val chapter_purchase = findViewById<EditText>(R.id.chapter_purchase)
        chapter_purchase.setText(chapterPurchase!!.name)

        configuration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientId)
        val amount = 10
        val purchaseBtn = findViewById<Button>(R.id.purchaseBtn)
        purchaseBtn.setOnClickListener {
            getPayment(chapterPurchase)
        }
    }
    fun getPayment(chapterPurchase:Chapter){
        var payment = PayPalPayment(BigDecimal(10),"USD",chapterPurchase.name,PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE){
            var paymentConfirmation: PaymentConfirmation? = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
            if (paymentConfirmation != null){
                var paymentDetails = paymentConfirmation.toJSONObject().toString()
                try {
                    val obj = JSONObject(paymentDetails)
                } catch (e: JSONException) {
                    Toast.makeText(this,e.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            }else if (requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show()
            }
        }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this,"INVALID PAYMENT",Toast.LENGTH_SHORT).show()
        }
    }
}