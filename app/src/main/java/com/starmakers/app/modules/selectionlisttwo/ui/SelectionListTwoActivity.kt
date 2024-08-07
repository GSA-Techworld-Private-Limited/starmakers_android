package com.starmakers.app.modules.selectionlisttwo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.starmakers.app.R
import com.starmakers.app.appcomponents.base.BaseActivity
import com.starmakers.app.databinding.ActivitySelectionListTwoBinding
import com.starmakers.app.modules.selectionlisttwo.`data`.model.Listrectangle141RowModel
import com.starmakers.app.modules.selectionlisttwo.`data`.viewmodel.SelectionListTwoVM
import com.starmakers.app.responses.SelectionListResponse
import com.starmakers.app.service.ApiManager
import com.starmakers.app.service.SessionManager
import retrofit2.Call
import retrofit2.Response
import kotlin.Int
import kotlin.String
import kotlin.Unit

class SelectionListTwoActivity :
    BaseActivity<ActivitySelectionListTwoBinding>(R.layout.activity_selection_list_two) {
  private val viewModel: SelectionListTwoVM by viewModels<SelectionListTwoVM>()

  private lateinit var sessionManager: SessionManager
  override fun onInitialized(): Unit {
    sessionManager=SessionManager(this)

    val auditionId = intent.getStringExtra("artistDataId")
    if (auditionId != null) {
      fetchData(auditionId.toFloat().toInt())
    }

    viewModel.navArguments = intent.extras?.getBundle("bundle")
//    val listrectangle140Adapter =
//    Listrectangle140Adapter(viewModel.listrectangle140List.value?:mutableListOf())
//    binding.recyclerListrectangle140.adapter = listrectangle140Adapter
//    listrectangle140Adapter.setOnItemClickListener(
//    object : Listrectangle140Adapter.OnItemClickListener {
//      override fun onItemClick(view:View, position:Int, item : Listrectangle141RowModel) {
//        onClickRecyclerListrectangle140(view, position, item)
//      }
//    }
//    )
//    viewModel.listrectangle140List.observe(this) {
//      listrectangle140Adapter.updateData(it)
//    }
    binding.selectionListTwoVM = viewModel
    window.statusBarColor= ContextCompat.getColor(this,R.color.statusbar2)
  }

  override fun setUpClicks(): Unit {
    binding.imageArrowleft.setOnClickListener {
      finish()
    }
  }


  private fun fetchData(auditionId:Int){
    val serviceGenerator= ApiManager.apiInterface
    val accessToken=sessionManager.fetchAuthToken()
    val authorization="Token $accessToken"
    val call=serviceGenerator.get_selection_list_by_id(authorization,auditionId)

    call.enqueue(object : retrofit2.Callback<SelectionListResponse>{
      @SuppressLint("SuspiciousIndentation")
      override fun onResponse(
        call: Call<SelectionListResponse>,
        response: Response<SelectionListResponse>
      ) {
        val responsedata=response.body()


        // Define the corner radius in pixels (converted from dp)
        val cornerRadiusInPixels = 15 // Change to your dimension resource

        // Create a RequestOptions object with the RoundedCorners transformation
        val requestOptions = RequestOptions()
          .transform(RoundedCorners(cornerRadiusInPixels))
        if((responsedata!=null)&&(responsedata.message=="success")) {
          val selectiondata = response.body()

          if (selectiondata != null) {
            val data = selectiondata.data // Store the data list in a variable for easy access

            if (data.isNotEmpty()) {
              binding.moviename.text = data[0].movieName
              binding.txtDate1.text = data[0].appliedDate
              binding.txtTime1.text = data[0].timingsFrom
              binding.txtVenue1.text = data[0].venue
              val uniquePositions = data.map { it.auditionPositions }.distinct()
              binding.txtRole1.text = uniquePositions.joinToString(separator = ", ", prefix = "[ ", postfix = " ]")


              Glide.with(this@SelectionListTwoActivity)
                .load(data[0].moviePoster)
                .apply(requestOptions)
                .into(binding.imageRectangle106)

              binding.recyclerListrectangle140.apply {
                layoutManager = LinearLayoutManager(
                  this@SelectionListTwoActivity,
                  LinearLayoutManager.VERTICAL,
                  false
                )
                val audtioAdapter = Listrectangle140Adapter(data.map { it.artist })
                adapter = audtioAdapter
              }
            } else {
              // Handle the case where data is empty
              Toast.makeText(this@SelectionListTwoActivity,"Wait Till Selection Procedure Complete",Toast.LENGTH_LONG).show()
            }
          } else {
            // Handle the case where response.body() is null
          }
        }


      }

      override fun onFailure(call: Call<SelectionListResponse>, t: Throwable) {
        t.printStackTrace()
        Log.e("error", t.message.toString())
      }
    })
  }
  fun onClickRecyclerListrectangle140(
    view: View,
    position: Int,
    item: Listrectangle141RowModel
  ): Unit {
    when(view.id) {
    }
  }

  companion object {
    const val TAG: String = "SELECTION_LIST_TWO_ACTIVITY"


    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SelectionListTwoActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
