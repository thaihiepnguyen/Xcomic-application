package com.example.x_comic.views.purchase

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.BuildConfig
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.google.firebase.storage.FirebaseStorage
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import jp.wasabeef.glide.transformations.BlurTransformation


class PurchaseActivity : AppCompatActivity() {
    // QuickStartConstants.kt
     val PAYPAL_CLIENT_ID = "AetqiuaX2ZoHcRciOFWmqHOGSUcasvGCdv0wqrqFsLYxezwE_f2mvjaWrR0nwmep-nSPp_b_CITJitd7"
     val PAYPAL_SECRET = "ONLY-FOR-QUICKSTART-DO-NOT-INCLUDE-SECRET-IN-CLIENT-SIDE-APPLICATIONS"

    val PAYPAL_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = CheckoutConfig(
            application = this.application,
            clientId = "AetqiuaX2ZoHcRciOFWmqHOGSUcasvGCdv0wqrqFsLYxezwE_f2mvjaWrR0nwmep-nSPp_b_CITJitd7",
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)

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

        val paymentButtonContainer = findViewById<PaymentButtonContainer>(R.id.payment_button_container)
        paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                }
            },
            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }
        )
    }}