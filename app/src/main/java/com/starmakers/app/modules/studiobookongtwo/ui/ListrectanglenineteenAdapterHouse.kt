package com.starmakers.app.modules.studiobookongtwo.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.starmakers.app.R
import com.starmakers.app.modules.auditionstwo.ui.AuditionsTwoActivity
import com.starmakers.app.modules.frame316.ui.Frame316Activity
import com.starmakers.app.modules.studiobookongtwo.`data`.model.Listrectanglenineteen1RowModel
import com.starmakers.app.responses.HouseLocationStudio
import com.starmakers.app.service.ApiManager
import kotlin.Int
import kotlin.collections.List

class ListrectanglenineteenAdapterHouse(
  var list: List<HouseLocationStudio>
) : RecyclerView.Adapter<ListrectanglenineteenAdapterHouse.RowListrectanglenineteen1VH>() {
  private var clickListener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowListrectanglenineteen1VH {
    val
        view=LayoutInflater.from(parent.context).inflate(R.layout.row_listrectanglenineteen1,parent,false)
    return RowListrectanglenineteen1VH(view)
  }

  override fun onBindViewHolder(holder: RowListrectanglenineteen1VH, position: Int) {
    return  holder.bindView(list[position])
  }

  override fun getItemCount(): Int {
    return list.size
  }

  public fun updateData(newData: List<HouseLocationStudio>) {
    list = newData
    notifyDataSetChanged()
  }

  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  interface OnItemClickListener {
    fun onItemClick(
      view: View,
      position: Int,
      item: Listrectanglenineteen1RowModel
    ) {
    }
  }

  inner class RowListrectanglenineteen1VH(
    view: View
  ) : RecyclerView.ViewHolder(view) {
    //val binding: RowListrectanglenineteen1Binding = RowListrectanglenineteen1Binding.bind(itemView)



    val studioName: TextView =itemView.findViewById(R.id.txtRamanandStudio)
    val location: TextView =itemView.findViewById(R.id.txtLocationJPN1)
    val txtMeasurement1: TextView =itemView.findViewById(R.id.txtMeasurement1)
    val image: ImageView =itemView.findViewById(R.id.imageRectangleNineteen)
    val requestButton: AppCompatButton =itemView.findViewById(R.id.btnRequest)
    var requestid=-1

    val aboutImage:ImageView=itemView.findViewById(R.id.imageInfo)

    // Define the corner radius in pixels (converted from dp)
    val cornerRadiusInPixels = 15 // Change to your dimension resource

    // Create a RequestOptions object with the RoundedCorners transformation
    val requestOptions = RequestOptions()
      .transform(RoundedCorners(cornerRadiusInPixels))

    fun bindView(postModel: HouseLocationStudio) {
      studioName.text=postModel.studio_name
      location.text=postModel.location
      txtMeasurement1.text=postModel.budget.toString()
      if (!postModel.studio_picture.isEmpty()) {
//        Picasso.get()
//          .load(postModel.studio_picture[0].studio_picture)
//          .into(image);

        val imagefile=postModel.studio_picture[0].studio_picture
        val file=ApiManager.getImageUrl(imagefile)
        Glide.with(itemView)
          .load(file) // Replace with your image URL or resource ID
          .apply(requestOptions)
          .into(image)
      } else {
        // Handle the case when the studio_picture list is empty
        // You can set a default image or show an error message.
      }

      requestid=postModel.id
      requestButton.setOnClickListener {
        val context = itemView.context
        val intent = Intent(context, Frame316Activity::class.java)
        intent.putExtra("requestId", requestid) // Pass the id to the new activity
        context.startActivity(intent)
      }

      aboutImage.setOnClickListener {
        val context = itemView.context
        val intent = Intent(context, AuditionsTwoActivity::class.java)
        intent.putExtra("studioId", postModel.id) // Pass the id to the new activity
        context.startActivity(intent)
      }
    }
    init {
//      binding.imageRectangleNineteen.setOnClickListener {
//        // TODO replace with value from datasource
//        clickListener?.onItemClick(it, adapterPosition, Listrectanglenineteen1RowModel())
//      }
//      binding.txtRamanandStudio.setOnClickListener {
//        // TODO replace with value from datasource
//        clickListener?.onItemClick(it, adapterPosition, Listrectanglenineteen1RowModel())
//      }
//      binding.btnRequest.setOnClickListener {
//        // TODO replace with value from datasource
//        clickListener?.onItemClick(it, adapterPosition, Listrectanglenineteen1RowModel())
//      }
    }
  }
}
