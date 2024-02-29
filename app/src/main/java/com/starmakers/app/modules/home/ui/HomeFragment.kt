package com.starmakers.app.modules.home.ui

import SliderrectangleelevenAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Api
import com.squareup.picasso.Picasso
import com.starmakers.app.R
import com.starmakers.app.appcomponents.base.BaseFragment
import com.starmakers.app.databinding.FragmentHomeBinding
import com.starmakers.app.modules.artistbookongfive.ui.ArtistBookongFiveActivity
import com.starmakers.app.modules.artistbookongone.ui.ArtistBookongOneActivity
import com.starmakers.app.modules.artistmembership.ui.ArtistMembershipActivity
import com.starmakers.app.modules.auditions.ui.AuditionsActivity
import com.starmakers.app.modules.campaignone.ui.CampaignOneActivity
import com.starmakers.app.modules.financialoverview.ui.GridrectangletenAdapter
import com.starmakers.app.modules.frame311.ui.Frame311Activity
import com.starmakers.app.modules.home.`data`.model.ImageSliderSliderrectangleelevenModel
import com.starmakers.app.modules.home.`data`.model.SpinnerGroup122Model
import com.starmakers.app.modules.home.`data`.viewmodel.HomeVM
import com.starmakers.app.modules.membershipoptioncomingsoon.ComingSoon
import com.starmakers.app.modules.studiobookong.ui.StudioBookongAdapter
import com.starmakers.app.modules.studiobookong1.ui.StudioBookong1Activity
import com.starmakers.app.responses.BannerResponses
import com.starmakers.app.responses.CampaignResponse
import com.starmakers.app.responses.CrowdResponses
import com.starmakers.app.responses.FundingDemoVideos
import com.starmakers.app.responses.ProfileResponse
import com.starmakers.app.service.ApiManager
import com.starmakers.app.service.CircleTransformation
import com.starmakers.app.service.SessionManager
import org.koin.android.ext.android.bind
import retrofit2.Call
import retrofit2.Response
import kotlin.String
import kotlin.Unit
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
//  private val imageUri: Uri =
//      Uri.parse("https://firebasestorage.googleapis.com/v0/b/starmakerz.appspot.com/o/img_rectangle11.png?alt=media&token=20cd0197-3af2-40da-bc53-a950f7ef933f&_gl=1*1kqjzek*_ga*NjczMjE3ODc4LjE2OTg3NDE5ODA.*_ga_CW55HF8NVT*MTY5ODc1NDMzMy4zLjEuMTY5ODc1NDgxMy4yNS4wLjA.")
//  private val imageUri1: Uri =
//    Uri.parse("https://firebasestorage.googleapis.com/v0/b/starmakerz.appspot.com/o/image5.png?alt=media&token=bb5b116c-9863-4e1f-992e-4fdefd605780&_gl=1*kgctb8*_ga*NjczMjE3ODc4LjE2OTg3NDE5ODA.*_ga_CW55HF8NVT*MTY5ODc1NDMzMy4zLjEuMTY5ODc1NDkzMi41OS4wLjA.")
//  private val imageUri2: Uri =
//    Uri.parse("https://firebasestorage.googleapis.com/v0/b/starmakerz.appspot.com/o/image3.png?alt=media&token=7cef87b6-c751-40e7-a0ad-0287e1e614cc&_gl=1*117l19v*_ga*NjczMjE3ODc4LjE2OTg3NDE5ODA.*_ga_CW55HF8NVT*MTY5ODc1NDMzMy4zLjEuMTY5ODc1NDk1My4zOC4wLjA.")
//  private val imageUri3: Uri =
//    Uri.parse("https://firebasestorage.googleapis.com/v0/b/starmakerz.appspot.com/o/image2.png?alt=media&token=aa8af948-f15e-403c-bf13-8903cba245e3&_gl=1*1jov726*_ga*NjczMjE3ODc4LjE2OTg3NDE5ODA.*_ga_CW55HF8NVT*MTY5ODc1NDMzMy4zLjEuMTY5ODc1NDk3My4xOC4wLjA.")


  private lateinit var sessionManager: SessionManager


  private val imageSliderItems: ArrayList<CrowdResponses> = arrayListOf()

  private lateinit var videoAdapter: VideoAdapter



//  private val imageSliderSliderrectangleelevenItems:
//      ArrayList<ImageSliderSliderrectangleelevenModel> =
//      arrayListOf(ImageSliderSliderrectangleelevenModel(imageRectangleEleven =
//  imageUri.toString()),ImageSliderSliderrectangleelevenModel(imageRectangleEleven =
//  imageUri1.toString()),ImageSliderSliderrectangleelevenModel(imageRectangleEleven =
//      imageUri2.toString()),ImageSliderSliderrectangleelevenModel(imageRectangleEleven =
//      imageUri3.toString()))


  private val viewModel: HomeVM by viewModels<HomeVM>()

  override fun onInitialized(): Unit {
    viewModel.navArguments = arguments
    sessionManager=SessionManager(requireActivity())


    val recyclerView=binding.recyclerviewforfundingvideos

    // Initialize the adapter with an empty list initially
    videoAdapter = VideoAdapter(emptyList())

    // Set the adapter to RecyclerView
    recyclerView.adapter = videoAdapter

    fetchData()


    getBanner()

    fundingVideos()


    getCrowdFundingZone()

    val sliderAdapter = SliderrectangleelevenAdapter(imageSliderItems, true) { itemId ->
      val intent = Intent(requireActivity(), CampaignOneActivity::class.java)
      intent.putExtra("itemId", itemId) // Pass the id to the next activity
      startActivity(intent)
    }


    binding.imageSliderSliderrectangleeleven.adapter = sliderAdapter
    binding.imageSliderSliderrectangleeleven.onIndicatorProgress = { selectingPosition, progress ->
      Log.d("IndicatorProgress", "SelectingPosition: $selectingPosition, Progress: $progress")
     // binding.indicatorVolume.onPageScrolled(selectingPosition, progress)
    }
    binding.indicatorVolume.updateIndicatorCounts(binding.imageSliderSliderrectangleeleven.indicatorCount)

    binding.homeVM = viewModel
  }

  override fun onPause(): Unit {
    binding.imageSliderSliderrectangleeleven.pauseAutoScroll()
    super.onPause()
  }

  override fun onResume(): Unit {
    super.onResume()
    binding.imageSliderSliderrectangleeleven.resumeAutoScroll()
  }

  override fun setUpClicks(): Unit {
    binding.imageMenu.setOnClickListener{
      val i =Intent(requireActivity(),Frame311Activity::class.java)
      startActivity(i)
    }


    binding.profilePicture.setOnClickListener {
      val i = Intent(requireActivity(),ArtistBookongOneActivity::class.java)
      startActivity(i)
    }
    binding.linearColumnuntitleddesign.setOnClickListener {
      val i=Intent(requireActivity(), ArtistMembershipActivity::class.java)
      startActivity(i)
    }

    binding.linearColumn.setOnClickListener {
      val i =Intent(requireActivity(), ArtistBookongFiveActivity::class.java)
      startActivity(i)
    }

    binding.linearColumnOne.setOnClickListener {
      val i=Intent(requireActivity(), AuditionsActivity::class.java)
      startActivity(i)
    }

    binding.linearColumnTwo.setOnClickListener {
      val i=Intent(requireActivity(), StudioBookong1Activity::class.java)
      startActivity(i)
    }

  }


  fun getCrowdFundingZone(){
    val serviceGenerator = ApiManager.apiInterface
    val accessToken = sessionManager.fetchAuthToken()
    val authorization = "Token $accessToken"
    val call = serviceGenerator.getCrowdFundingImages(authorization)

    call.enqueue(object : retrofit2.Callback<List<CrowdResponses>> {
      override fun onResponse(
        call: Call<List<CrowdResponses>>,
        response: Response<List<CrowdResponses>>
      ) {
        val crowdFundingImages = response.body()
        Log.d("Server Response", crowdFundingImages.toString())

        crowdFundingImages?.let { list ->
          imageSliderItems.clear() // Clear previous data
          imageSliderItems.addAll(list) // Add new crowd funding images
          binding.imageSliderSliderrectangleeleven.adapter?.notifyDataSetChanged()
        }
      }

      override fun onFailure(call: Call<List<CrowdResponses>>, t: Throwable) {
        t.printStackTrace()
        Log.e("error", t.message.toString())
      }
    })
  }


  fun getBanner(){
    val serviceGenerator= ApiManager.apiInterface
    val accessToken=sessionManager.fetchAuthToken()
    val authorization="Token $accessToken"
    val call=serviceGenerator.getBanners(authorization)

    call.enqueue(object : retrofit2.Callback<List<BannerResponses>>{
      override fun onResponse(
        call: Call<List<BannerResponses>>,
        response: Response<List<BannerResponses>>
      ) {
        val customerResponse=response.body()

        if(customerResponse!=null){

          val image=customerResponse[0].image
            val file = ApiManager.getImageUrl(image!!)
            Picasso.get().load(file).into(binding.imageRectangle153)
        }
      }

      override fun onFailure(call: Call<List<BannerResponses>>, t: Throwable) {
        t.printStackTrace()
        Log.e("error", t.message.toString())
      }
    })
  }


  fun fundingVideos(){
    val serviceGenerator= ApiManager.apiInterface
    val accessToken=sessionManager.fetchAuthToken()
    val authorization="Token $accessToken"
    val call=serviceGenerator.getFundingVideos(authorization)

    call.enqueue(object : retrofit2.Callback<List<FundingDemoVideos>>{
      override fun onResponse(
        call: Call<List<FundingDemoVideos>>,
        response: Response<List<FundingDemoVideos>>
      ) {
        val customerResponse=response.body()

        if (customerResponse != null) {
          // Update the adapter with the list of videos
          //videoAdapter.updateData(customerResponse)

          binding.recyclerviewforfundingvideos.apply {
            layoutManager=
              LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
            val audtioAdapter= VideoAdapter(customerResponse)
            binding.recyclerviewforfundingvideos.adapter=audtioAdapter
          }

        }
      }

      override fun onFailure(call: Call<List<FundingDemoVideos>>, t: Throwable) {
        t.printStackTrace()
        Log.e("error", t.message.toString())
      }
    })
  }
  private fun fetchData(){
    val serviceGenerator = ApiManager.apiInterface
    val accessToken = sessionManager.fetchAuthToken()
    val authorization = "Token $accessToken"
    val call = serviceGenerator.getProfile(authorization)

    call.enqueue(object : retrofit2.Callback<ProfileResponse>{
      override fun onResponse(
        call: Call<ProfileResponse>,
        response: Response<ProfileResponse>
      ) {
        val customerResponse = response.body()

        if(customerResponse != null) {
          binding.txtRahul.text = customerResponse.name
          sessionManager.saveuserId(customerResponse.id.toString())

          // Check if artistPictures is not null and not empty
          if (!customerResponse.artistPictures.isNullOrEmpty()) {
            // Load the first artist picture if available
            val profilePicture: ImageView = binding.profilePicture
            val image = customerResponse.artistPictures[0].artistPicture
            val file = ApiManager.getImageUrl(image!!)
            Picasso.get().load(file).transform(CircleTransformation()).placeholder(R.drawable.img_ellipse32).into(profilePicture)
          } else {
            // Check if profile picture is not empty
            val profilePicture: ImageView = binding.profilePicture
            val image = customerResponse.profile
            if (!image.isNullOrEmpty()) {
              val file = ApiManager.getImageUrl(image)
              Picasso.get().load(file).transform(CircleTransformation()).placeholder(R.drawable.img_ellipse32).into(profilePicture)
            } else {
              // Load a default picture if both artistPictures and profile are empty
              Picasso.get().load(R.drawable.default_profile_pic).transform(CircleTransformation()).placeholder(R.drawable.img_ellipse32).into(profilePicture)
            }
          }
        }
      }

      override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
        t.printStackTrace()
        Log.e("error", t.message.toString())
      }
    })
  }



  companion object {
    const val TAG: String = "HOME_FRAGMENT"


    fun getInstance(bundle: Bundle?): HomeFragment {
      val fragment = HomeFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}
