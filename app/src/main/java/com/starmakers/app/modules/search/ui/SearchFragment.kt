package com.starmakers.app.modules.search.ui

import android.app.appsearch.SearchResult
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.starmakers.app.R
import com.starmakers.app.appcomponents.base.BaseFragment
import com.starmakers.app.databinding.FragmentSearchBinding
import com.starmakers.app.modules.artistandauditioninfo.InfoActivityForSearch
import com.starmakers.app.modules.artistbookongfour.ui.ArtistBookongFourActivity
import com.starmakers.app.modules.artistmembership.ui.ArtistMembershipActivity
import com.starmakers.app.modules.campaignone.ui.CampaignOneActivity
import com.starmakers.app.modules.financialoverview.ui.GridrectangletenAdapter
import com.starmakers.app.modules.search.`data`.viewmodel.SearchVM
import com.starmakers.app.responses.Artists
import com.starmakers.app.responses.CampaignResponse
import com.starmakers.app.responses.Campaigns
import com.starmakers.app.responses.SearchResponses
import com.starmakers.app.service.ApiManager
import com.starmakers.app.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.String
import kotlin.Unit

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) , SearchAdapter.OnItemClickListener{
  private val viewModel: SearchVM by viewModels<SearchVM>()

  private lateinit var sessionManager:SessionManager
  private val recentSearches = mutableListOf<String>()
  override fun onInitialized(): Unit {
    viewModel.navArguments = arguments
    sessionManager=SessionManager(requireActivity())


    loadRecentSearches() // Load recent searches from SharedPreferences

    setupRecyclerView()

    setupRecentSearches()
    // Set up a text change listener on the EditText
    binding.btnSearchHere.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Not needed
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Not needed
      }

      override fun afterTextChanged(s: Editable?) {
        // Perform search when text changes
        performSearch(s.toString().trim())
        //binding.searchguide.visibility= View.GONE
        binding.recentSearch.visibility=View.GONE
      }
    })

    binding.searchVM = viewModel
  }


  override fun onItemClick(item: Any) {
    // Handle item click here, navigate to the specified activity with the info from the clicked item
    // You can pass necessary data using Intent extras
    if (item is Artists) {
      // Handle artist item click
      val intent = Intent(requireActivity(), ArtistBookongFourActivity::class.java)
      intent.putExtra("profileDataId", item.id)
      startActivity(intent)
    } else if (item is Campaigns) {
      val intent = Intent(requireActivity(), CampaignOneActivity::class.java)
      intent.putExtra("itemId", item.id.toString())
      startActivity(intent)
    }
  }

  private fun setupRecyclerView() {
    val adapter = SearchAdapter(emptyList(), this)
    binding.recyclerforSearch.adapter = adapter
    binding.recyclerforSearch.layoutManager = GridLayoutManager(requireActivity(),3)
  }


  private fun updateSearchResults(items: List<Any>) {
    val adapter = binding.recyclerforSearch.adapter as SearchAdapter
    adapter.apply {
      this.items = items
      notifyDataSetChanged()
    }
  }









  private fun performSearch(query: String) {
    if (query.isNotEmpty()) {
      if (recentSearches.contains(query)) {
        recentSearches.remove(query) // Remove the query if it already exists to avoid duplicates
      }
      recentSearches.add(0, query) // Add the search query to the top of recent searches
      if (recentSearches.size > 10) {
        recentSearches.subList(10, recentSearches.size).clear() // Keep only the latest 10 searches
      }

      saveRecentSearches() // Save recent searches to SharedPreferences
      updateRecentSearches() // Update the UI with the recent searches

      val apiService= ApiManager.apiInterface
      apiService.search(query).enqueue(object : Callback<SearchResponses> {
        override fun onResponse(call: Call<SearchResponses>, response: Response<SearchResponses>) {
          if (response.isSuccessful) {
            val searchResponses = response.body()
            val items = mutableListOf<Any>()
            searchResponses.let {
              items.addAll(searchResponses?.artists ?: emptyList())
              items.addAll(searchResponses?.campaigns ?: emptyList())
            }

            updateSearchResults(items)
          } else {
            Log.e(TAG, "Unsuccessful response: ${response.code()}")
            // Handle unsuccessful response
          }
        }

        override fun onFailure(call: Call<SearchResponses>, t: Throwable) {
          // Handle network errors
        }
      })
    }
  }


  private fun setupRecentSearches() {
    // Initialize the recent searches view
    updateRecentSearches()
  }


  private fun updateRecentSearches() {
    val recentSearchesContainer = binding.linearRecentSearches
    recentSearchesContainer.removeAllViews()

    val searchesToShow = if (recentSearches.isEmpty()) {
      listOf("Shah Rukh Khan", "Ramoji Film City") // Default suggestions
    } else {
      recentSearches
    }

    for (search in searchesToShow) {
      val textView = TextView(requireContext()).apply {
        text = search
        textSize = 16f
        setPadding(16, 16, 16, 16)
      }
      textView.setOnClickListener {
        binding.btnSearchHere.setText(search)
        performSearch(search)
      }
      recentSearchesContainer.addView(textView)
    }
  }

  private fun saveRecentSearches() {
    val sharedPreferences = requireActivity().getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      putStringSet("recent_searches", recentSearches.toSet())
      apply()
    }
  }

  private fun loadRecentSearches() {
    val sharedPreferences = requireActivity().getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
    val savedSearches = sharedPreferences.getStringSet("recent_searches", emptySet())
    recentSearches.clear()
    recentSearches.addAll(savedSearches ?: emptySet())
  }






  override fun setUpClicks(): Unit {
  }

  companion object {
    const val TAG: String = "SEARCH_FRAGMENT"


    fun getInstance(bundle: Bundle?): SearchFragment {
      val fragment = SearchFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}
