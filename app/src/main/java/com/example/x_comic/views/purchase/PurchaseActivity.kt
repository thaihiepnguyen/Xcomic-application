package com.example.x_comic.views.purchase

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import bolts.Task.delay
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Order
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import jp.wasabeef.glide.transformations.BlurTransformation


class PurchaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val intent = intent
        val stringData = intent.getStringExtra("bookdata")
        val bookData = stringData?.let{ Klaxon().parse<Product>(it)}
        val title = findViewById<TextView>(R.id.book_title)
        val author = findViewById<TextView>(R.id.book_author)
        val cover = findViewById<ImageView>(R.id.book_cover)
        val view = findViewById<TextView>(R.id.viewTextView)
        val favorite = findViewById<TextView>(R.id.favoriteTextView)
        val chapter = findViewById<TextView>(R.id.numOfChapterTextView)

        title.text = bookData?.title
        author.text = bookData?.author
        favorite.text = bookData?.favorite.toString()
        val backCover = findViewById<ImageView>(R.id.background)

        val imageName = bookData?.cover
        Glide.with(cover.context)
            .load(imageName)
            .apply(RequestOptions().override(500, 600))
            .into(cover)
        Glide.with(this)
            .load(imageName)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
            .into(backCover)
        view.text = bookData?.view.toString()
        chapter.text = bookData?.chapters?.size.toString()

        val chapterData = intent.getStringExtra("chapter")
        val chapterPurchase = chapterData?.let{ Klaxon().parse<Chapter>(it)}
        val chapter_purchase = findViewById<EditText>(R.id.chapter_purchase)
        chapter_purchase.setText(chapterPurchase!!.name)

        val paymentButton = findViewById<PaymentButtonContainer>(R.id.payment_button_container)

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
                    val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    val ordersRef = database.getReference("orders")

                    val uid =
                        FirebaseAuthManager.getUser()?.uid // get current user ID from FirebaseAuth
                    val bid = chapterPurchase.id_book
                    val cid = chapterPurchase.id_chapter
                    val cost = 10.0
                    val order = Order("",uid!!, bid, cid!!, cost)
                    val newOrderRef = ordersRef.push()
                    val newOrderId = newOrderRef.key
                    order.id = newOrderId!!
                    newOrderRef.setValue(order)
                    Handler().postDelayed({
                        finish()
                    }, 500)
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