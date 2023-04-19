package com.example.x_comic.adapters
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.BookSneek
import com.example.x_comic.models.Product
import com.example.x_comic.views.detail.DetailActivity
import com.google.firebase.storage.FirebaseStorage

class ListAdapterSlideshow (
    private  var context: Activity,
    private var bookList: MutableList<Product>,
) : RecyclerView.Adapter<ListAdapterSlideshow.ViewHolder>()
{
    var onItemClick: ((Product) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var title = listItemView.findViewById(R.id.bookname) as TextView;
        var author = listItemView.findViewById(R.id.author) as TextView;
        var cover = listItemView.findViewById(R.id.cover) as ImageView;
        var rating = listItemView.findViewById(R.id.badge_rating) as TextView;
        var love = listItemView.findViewById(R.id.favorite_book) as ImageButton;
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(bookList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.book_cover_slideshow, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return bookList.size;
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList.get(position);
        val title = holder.title;
        val author = holder.author;
        val cover = holder.cover;
        val rating = holder.rating;
        var love = holder.love;
        var favourite = false;
        title.setText(book.title);
        author.setText(book.author);
        val storage = FirebaseStorage.getInstance()
        val imageName = book.cover // Replace with your image name
        val imageRef = storage.reference.child("book_cover/$imageName")
        imageRef.getBytes(Long.MAX_VALUE)
            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                // Set the Bitmap to the ImageView
                cover.setImageBitmap(bitmap)
            }.addOnFailureListener {
                // Handle any errors
            }
        rating.text= Html.fromHtml("<font>${book.rating} </font>" +
                "<font color='#FFC000'> ★ </font>")

        love.setOnClickListener{
            favourite = !favourite;
            if (favourite) {
                love.setImageResource(R.drawable.love_clickable)
            }else {
                love.setImageResource(R.drawable.love)
            }
        }
        holder.itemView.setOnClickListener {
            Log.i("String",book.toString())
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("book_data", book.toString())
            ActivityCompat.startActivityForResult(context, intent, 302, null)
        }
    }

}



