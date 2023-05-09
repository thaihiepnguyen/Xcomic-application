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

import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.google.firebase.storage.FirebaseStorage
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.Approval
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.*
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.*
import com.paypal.checkout.paymentbutton.PayPalButton
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import jp.wasabeef.glide.transformations.BlurTransformation


class PurchaseActivity : AppCompatActivity() {
    // QuickStartConstants.kt
     val PAYPAL_CLIENT_ID = "AetqiuaX2ZoHcRciOFWmqHOGSUcasvGCdv0wqrqFsLYxezwE_f2mvjaWrR0nwmep-nSPp_b_CITJitd7"
     val PAYPAL_SECRET = "ONLY-FOR-QUICKSTART-DO-NOT-INCLUDE-SECRET-IN-CLIENT-SIDE-APPLICATIONS"

    val PAYPAL_REQUEST_CODE = 123

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
        val imageRef = storage.reference.child("book/$imageName")
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



        val paymentButton = findViewById<PaymentButtonContainer>(R.id.payment_button_container)
//        paymentButton.setup(
//            object : CreateOrder {
//                override fun create(createOrderActions: CreateOrderActions) {
//                    val purchaseUnits: ArrayList<PurchaseUnit> = ArrayList()
//                    purchaseUnits.add(
//                        PurchaseUnit.Builder()
//                            .amount(
//                                Amount.Builder()
//                                    .currencyCode(CurrencyCode.USD)
//                                    .value("10.00")
//                                    .build()
//                            )
//                            .build()
//                    )
//                    val order = Order(
//                        OrderIntent.CAPTURE,
//                        AppContext.Builder()
//                            .userAction(UserAction.PAY_NOW)
//                            .build(),
//                        purchaseUnits
//                    )
//                    createOrderActions.create(order, null as CreateOrderActions.OnOrderCreated?)
//                }
//            },
//            object : OnApprove {
//                override fun onApprove(approval: Approval) {
//                    approval.orderActions.capture(object : OnCaptureComplete {
//                        override fun onCaptureComplete(result: CaptureOrderResult) {
//                            Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result))
//                        }
//                    })
//                }
//            }
//
//        )
        paymentButton.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
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