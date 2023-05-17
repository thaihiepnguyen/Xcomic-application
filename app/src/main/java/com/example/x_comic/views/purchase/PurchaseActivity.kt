package com.example.x_comic.views.purchase

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import bolts.Task.delay
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Order
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.LocalNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import java.text.SimpleDateFormat
import java.util.*


class PurchaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val intent = intent
        val userViewModel: UserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val stringData = intent.getStringExtra("book_data")
        var bookData = Product()
        val databaseReference = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("book").child(stringData!!)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tmp = dataSnapshot.getValue(Product::class.java)
                if (tmp != null){
                    bookData = Product(tmp)
                    val title = findViewById<TextView>(R.id.book_title)
                    val name_book = findViewById<TextView>(R.id.name_book)
                    val author = findViewById<TextView>(R.id.book_author)
                    val cover = findViewById<ImageView>(R.id.book_cover)
                    val view = findViewById<TextView>(R.id.viewTextView)
                    val favorite = findViewById<TextView>(R.id.favoriteTextView)
                    val chapter = findViewById<TextView>(R.id.numOfChapterTextView)

                    val backBtn = findViewById<ImageButton>(R.id.backBtn);
                    backBtn.setOnClickListener{
                        finish();
                    }

                    title.text = bookData?.title
                    name_book.text = bookData?.title
                    author.text = bookData?.author
                    userViewModel.getUserById(bookData!!.author) { user ->
                        run {
                            author.text = user.penname
                        }
                    }
                    favorite.text = bookData?.favorite.toString()
                    val backCover = findViewById<ImageView>(R.id.background)

                    val imageName = bookData?.cover
                    Glide.with(cover.context)
                        .load(imageName)
                        .apply(RequestOptions().override(500, 600))
                        .into(cover)
                    Glide.with(this@PurchaseActivity)
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
                                val currentTime = Calendar.getInstance().time
                                val dateFormat = SimpleDateFormat("HH:mm:ss yyyy-MM-dd", Locale.getDefault())
                                val time = dateFormat.format(currentTime)
                                val order = Order("",uid!!, bid, cid!!, cost, time)
                                val newOrderRef = ordersRef.push()
                                val newOrderId = newOrderRef.key
                                order.id = newOrderId!!
                                newOrderRef.setValue(order)
                                Toast.makeText(this@PurchaseActivity, "Purchase success", Toast.LENGTH_SHORT).show()
                                createNotificationChannel()
                                scheduleNotification()
                                finish()
                            }
                        },
                        onCancel = OnCancel {
                            Log.d("OnCancel", "Buyer canceled the PayPal experience.")
                        },
                        onError = OnError { errorInfo ->
                            Log.d("OnError", "Error: $errorInfo")
                        }
                    )
                }

            }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if any
                }
            })

    }
    fun scheduleNotification(){
        val intent = Intent(applicationContext, LocalNotification::class.java)
        val title = "X BOOK NOTIFICATION"
        val message = "Thank you <3 You have purchased a chapter"
        intent.putExtra(com.example.x_comic.views.main.titleExtra, title)
        intent.putExtra(com.example.x_comic.views.main.messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            com.example.x_comic.views.main.notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentTimeMillis = System.currentTimeMillis()
        val futureTimeMillis = currentTimeMillis + 30
        val time = futureTimeMillis
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(){
        val name = "Notif Channel"
        val desc = "A description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(com.example.x_comic.views.main.channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}