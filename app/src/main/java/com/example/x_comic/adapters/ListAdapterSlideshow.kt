package com.example.x_comic.adapters
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.BookSneek

class ListAdapterSlideshow (
    private var bookList: MutableList<BookSneek>,
) : RecyclerView.Adapter<ListAdapterSlideshow.ViewHolder>()
{
    var onItemClick: ((BookSneek) -> Unit)? = null

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
        cover.setImageResource(book.cover);
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

    }

}


