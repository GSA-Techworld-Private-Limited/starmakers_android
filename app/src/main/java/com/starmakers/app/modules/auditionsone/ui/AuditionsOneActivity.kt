package com.starmakers.app.modules.auditionsone.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.starmakers.app.R
import com.starmakers.app.appcomponents.base.BaseActivity
import com.starmakers.app.databinding.ActivityAuditionsOneBinding
import com.starmakers.app.modules.auditionsone.`data`.viewmodel.AuditionsOneVM
import com.starmakers.app.responses.Campaign
import com.starmakers.app.responses.CampaignDataById
import com.starmakers.app.responses.CampaignResponse
import com.starmakers.app.responses.ProfileResponse
import com.starmakers.app.service.ApiManager
import com.starmakers.app.service.CircleTransformation
import com.starmakers.app.service.SessionManager
import retrofit2.Call
import retrofit2.Response
import kotlin.String
import kotlin.Unit

class AuditionsOneActivity :
    BaseActivity<ActivityAuditionsOneBinding>(R.layout.activity_auditions_one) {
  private val viewModel: AuditionsOneVM by viewModels<AuditionsOneVM>()


  private lateinit var sessionManager: SessionManager
  override fun onInitialized(): Unit {
    sessionManager=SessionManager(this)
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    binding.auditionsOneVM = viewModel


    val campaignId=intent.getIntExtra("campaignId",-1)
    getCampaignById(campaignId)
    window.statusBarColor= ContextCompat.getColor(this,R.color.statusbar2)
  }


  fun getCampaignById(campaignID: Int){
    val serviceGenerator= ApiManager.apiInterface
    val accessToken=sessionManager.fetchAuthToken()
    val authorization="Token $accessToken"
    val call=serviceGenerator.getCampaignById(authorization,campaignID)

    call.enqueue(object : retrofit2.Callback<CampaignDataById>{
      override fun onResponse(
        call: Call<CampaignDataById>,
        response: Response<CampaignDataById>
      ) {
        val customerResponse=response.body()

        if((customerResponse!=null) && (customerResponse.status=="success")){


          binding.txtMovieKantara1.text=customerResponse.campaignData!!.campaignsName
          binding.txtGenreMystery1.text=customerResponse.campaignData!!.genere
          binding.txtStoryline1.text=customerResponse.campaignData!!.oneLineStory
          binding.txtAppxBudget1.text=customerResponse.campaignData!!.appxBudget
          binding.txtReleasingDate1.text=customerResponse.campaignData!!.movieStartDate

          binding.txtPrice1.text=customerResponse.financeData!!.collectedBudgetAmount
          binding.txtPriceOne1.text=customerResponse.financeData!!.spentAmount

          binding.txtPriceTwo1.text=customerResponse.financeData!!.requiredAdditionalBudget
          binding.txtPriceThree1.text=customerResponse.financeData!!.totalSpentAmount

          binding.txtPriceFour1.text=customerResponse.financeData!!.aboveTheBudgetAmount
          binding.txtPriceFive1.text=customerResponse.financeData!!.belowTheBudgetAmount

          binding.txtShareamountif1.text=customerResponse.financeData!!.shareAmount
          binding.txtPriceSix1.text=customerResponse.financeData!!.shareAmount

          binding.txtPriceSeven1.text=customerResponse.financeData!!.availableAmount

          val file = customerResponse.campaignData!!.moviePoster // Assuming postModel.profile is a File object

          val imgUrl= file?.let { ApiManager.getImageUrl(it) }
          Picasso.get()
            .load(imgUrl)
            .into(binding.imageRectangleTen)
        }
      }

      override fun onFailure(call: Call<CampaignDataById>, t: Throwable) {
        t.printStackTrace()
        Log.e("error", t.message.toString())
      }
    })
  }
  override fun setUpClicks(): Unit {
    binding.imageArrowleft.setOnClickListener {
      finish()
    }
  }

  companion object {
    const val TAG: String = "AUDITIONS_ONE_ACTIVITY"


    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, AuditionsOneActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
