package com.example.x_comic.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Category


class CategoryListAdapter (
    private var categoryList: MutableList<Category>,
) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>()
{
    var onItemClick: ((Category) -> Unit)? = null
    var context: Context? = null;
    private var _listCategory : ArrayList<Category> = ArrayList()
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var genre = listItemView.findViewById(R.id.genre) as TextView;

        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(categoryList[adapterPosition])
            }
        }

    }

    public fun getAllItem () : ArrayList<Category> {
        return _listCategory
    }
    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.category_2, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return categoryList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList.get(position);

        val genre = holder.genre;




        genre.setTextColor(R.color.white);
        genre.setText(category.name);
        genre.setTextColor(R.color.white);

            when (category.name){
                "Romance" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.love
                        )
                    );
                }
                "Fiction" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.yellow_green
                        )
                    );
                }
                "Short Story" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.light_blue
                        )
                    );
                }
                "Mystery" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.violet
                        )
                    );
                }
                "Thriller" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.golden
                        )
                    );

                }
                "Horror" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.purple_500
                        )
                    );
                }
                "Humor" -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.pink
                        )
                    );
                }
                else -> {
                    genre.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context!!,
                            R.color.lightgrey
                        )
                    );
                }


            }
        }




}