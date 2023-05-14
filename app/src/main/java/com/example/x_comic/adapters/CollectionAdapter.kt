package com.example.x_comic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.CollectionReading
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.main.fragments.CollectionDialogFragment

class CollectionAdapter (
    private var CollectionList: MutableList<CollectionReading>,
) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>()
{
    var onItemClick: ((CollectionReading) -> Unit)? = null
    var context: Context? = null;
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var cover = listItemView.findViewById(R.id.cover) as ImageView;
        var title = listItemView.findViewById(R.id.collection_name) as TextView;
        var number = listItemView.findViewById(R.id.books_number) as TextView
        var options = listItemView.findViewById(R.id.option) as TextView
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(CollectionList[adapterPosition])
            }
        }

    }


    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        val inflater = LayoutInflater.from(context)


       var columnView =  inflater.inflate(R.layout.collection_book, parent, false);
        return ViewHolder(columnView);
    }

    override fun getItemCount(): Int {
        return CollectionList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = CollectionList.get(position);

        val cover = holder.cover;
        val title = holder.title;
        var number = holder.number;
        var first_collection = collection.bookList[0];


        var bookViewModel : ProductViewModel = ProductViewModel()
        var imageName: String? = null;
        bookViewModel.getBookById(first_collection){
            book -> run {
            imageName = book.cover;
            Glide.with(cover.context)
                .load(imageName)
                .apply(RequestOptions().override(500, 600))
                .into(cover)
        }
        }

        var option = holder.options;

       option.setOnClickListener{
           showPopup(it, position, collection.name)
       }


//        Glide.with(context!!)
//            .load(collection.bookList[0].book.cover)
//            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
//            .into(cover)
        title.setText(collection.name);
        number.setText(collection.bookList.size.toString());







    }

    private fun showPopup(v: View, position: Int, name: String="") {
        val popup = PopupMenu(context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.option_collection_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete -> {
                   CollectionList.removeAt(position);
                    notifyDataSetChanged();

                    var bookViewModel : ProductViewModel = ProductViewModel()
                    FirebaseAuthManager.auth.uid?.let{
                        bookViewModel.updateCollection(it,CollectionList);
                    }
                    true
                }
                R.id.rename -> {
                    val dialog = CollectionDialogFragment("Rename a Collection",name)
                    dialog.show((context as? FragmentActivity)!!.supportFragmentManager,"dbchau10");

                    dialog.setFragmentResultListener("1") { key, bundle ->
                        if (key == "1") {

                            val collectionName = bundle.getString("1")!!
                            CollectionList[position].name = collectionName;
                            notifyDataSetChanged();

                            var bookViewModel: ProductViewModel = ProductViewModel()
                            FirebaseAuthManager.auth.uid?.let {
                                bookViewModel.updateCollection(it, CollectionList);
                            }

                        }
                    }
                    true
                }
                R.id.edit -> {
                    true
                }
                else -> true
            }
        }
        popup.show()
    }
}