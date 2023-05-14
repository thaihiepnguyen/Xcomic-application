package com.example.x_comic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R

import com.example.x_comic.models.BookReading
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.fragments.listReading
import com.example.x_comic.views.main.fragments.online_number

class BookReadingAdapter (
    private var bookReadingList: MutableList<BookReading>,
    private var online_number: TextView
) : RecyclerView.Adapter<BookReadingAdapter.ViewHolder>()
{
    var onItemClick: ((BookReading) -> Unit)? = null
    private var longClickListener: ((BookReading,Int) -> Unit)? = null

    // Create a function to set the long click listener
    fun setOnItemLongClickListener(listener: (BookReading,Int) -> Unit) {
        longClickListener = listener
    }
    var context: Context? = null;
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        var cover = listItemView.findViewById(R.id.cover) as ImageView;
        var title = listItemView.findViewById(R.id.bookname) as TextView;
        var progressBar = listItemView.findViewById(R.id.progress_bar) as ProgressBar;
        var removeBook = listItemView.findViewById(R.id.remove_book) as ImageButton;


        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(bookReadingList[adapterPosition])
            }



        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        val inflater = LayoutInflater.from(context)


        var columnView = inflater.inflate(R.layout.book_reading_online, parent, false);


        return ViewHolder(columnView);
    }

    override fun getItemCount(): Int {
        return bookReadingList.size;
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookReadingList.get(position);

        val cover = holder.cover;
        val title = holder.title;
        var progressbar = holder.progressBar;
        var remove = holder.removeBook;


       // cover.setImageResource(book.book.book.cover);

        if (book!=null) {
            val imageName = book.book?.cover
            Glide.with(cover.context)
                .load(imageName)
                .placeholder(R.drawable.empty_image)
                .apply(RequestOptions().override(500, 600))
                .into(cover)
            title.setText(book.book?.title);

            var total = book.chapter;
            var current = book.current;

            progressbar.progress = current * 100 / total;

            holder.itemView.setOnLongClickListener {

                longClickListener?.invoke(book, position)
                true // Return true to indicate the event has been consumed
            }

            var userViewModel: UserViewModel = UserViewModel()
            var productViewModel: ProductViewModel = ProductViewModel()

            remove.setOnClickListener {

                val uid = FirebaseAuthManager.auth.uid
                if (uid != null) {
                    productViewModel.getAllReadingBook(uid) { booksID ->
                        run {
                            //  productViewModel.removeBookReading(uid,position)
                            val childrenList: ArrayList<com.example.x_comic.models.Reading> =
                                arrayListOf();
                            for (snapshot in booksID.children) {
                                var bookid =
                                    snapshot.getValue(com.example.x_comic.models.Reading::class.java)
                                if (bookid != null) {
                                    childrenList.add(bookid!!)
                                }
                            }
                            childrenList.removeAt(position);
                            listReading.removeAt(position)

                            val storyText = if (listReading.size == 1) "Story" else "Stories"
                            online_number.text = "${listReading.size} $storyText"

                            userViewModel.updateReadingUserList(childrenList)
                            notifyDataSetChanged();
                        }
                    }
                }
            }

        }


    }

}





