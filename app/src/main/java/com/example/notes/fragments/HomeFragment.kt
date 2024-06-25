package com.example.notes.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.adapter.DetailsAdapter
import com.example.notes.databinding.FragmentHomeBinding
import com.example.notes.models.GetDetails
import com.example.notes.models.GetUserDetails
import com.example.notes.retrofit.ApiClient
import com.example.notes.utils.MySharedPreference
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), DetailsAdapter.RvAction {
    lateinit var detailsAdapter: DetailsAdapter

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onResume()

        binding.apply {
            MySharedPreference.init(requireContext())
            ApiClient.apiService.getUserDetails("Bearer ${MySharedPreference.token}")
                .enqueue(object : retrofit2.Callback<GetUserDetails> {
                    override fun onResponse(
                        p0: Call<GetUserDetails>,
                        p1: Response<GetUserDetails>
                    ) {
                        if (p1.isSuccessful) {
                            val user = p1.body()
                            Log.d(TAG, "onResponse: $user")
                            myNav.findViewById<TextView>(R.id.tvUsername).text = user?.username

                        } else {
                            val dialog = AlertDialog.Builder(context)
                            dialog.setTitle("Error")
                            dialog.setMessage(p1.errorBody()?.string())
                            dialog.show()
                        }
                    }

                    override fun onFailure(p0: Call<GetUserDetails>, p1: Throwable) {
                        Toast.makeText(context, "${p1.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.addFragment, bundleOf("key" to 0))
            }
            drawerIcon.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            myNav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.btn_delete -> {
                        ApiClient.apiService.deleteUser("Bearer ${MySharedPreference.token}")
                            .enqueue(object : retrofit2.Callback<Any> {
                                override fun onResponse(p0: Call<Any>, p1: Response<Any>) {

                                }

                                override fun onFailure(p0: Call<Any>, p1: Throwable) {
                                    MySharedPreference.token = "null"
                                    findNavController().popBackStack()
                                    findNavController().navigate(R.id.signFragment)
                                    Toast.makeText(context, "O'chirildi", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }

                    R.id.btn_log_out -> {
                        MySharedPreference.token = "null"
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.signFragment)
                    }
                }
                true
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        MySharedPreference.init(requireContext())
        ApiClient.apiService.getRejalar("Bearer ${MySharedPreference.token}")
            .enqueue(object : retrofit2.Callback<List<GetDetails>> {
                override fun onResponse(
                    p0: Call<List<GetDetails>>,
                    p1: Response<List<GetDetails>>
                ) {
                    if (p1.isSuccessful) {

                        detailsAdapter = DetailsAdapter(this@HomeFragment, p1.body() as ArrayList<GetDetails>)
                        binding.rvNotes.adapter = detailsAdapter
                    } else {
                        val dialog = AlertDialog.Builder(context)
                        dialog.setTitle("Xatolik")
                        dialog.setMessage(p1.errorBody()?.string())
                        dialog.show()
                    }
                }

                override fun onFailure(p0: Call<List<GetDetails>>, p1: Throwable) {
                    val dialog = AlertDialog.Builder(context)
                    dialog.setTitle("Get Error")
                    dialog.setMessage(p1.message)
                    dialog.show()
                }
            })
    }

    override fun moreClick(getDetails: GetDetails, position: Int, imageView: ImageView) {
        val menu = PopupMenu(requireContext(), imageView)
        menu.inflate(R.menu.more_menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    findNavController().navigate(
                        R.id.addFragment,
                        bundleOf("key" to 1, "getDetails" to getDetails)
                    )
                }

                R.id.delete -> {
                    MySharedPreference.init(requireContext())
                    ApiClient.apiService.deleteReja(
                        "Bearer ${MySharedPreference.token}",
                        getDetails.id
                    )
                        .enqueue(object : retrofit2.Callback<Any> {
                            override fun onResponse(p0: Call<Any>, p1: Response<Any>) {
                                Log.d(TAG, "onResponse: ${p1.message()}")
                                onResume()
                            }

                            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                                Log.d(TAG, "onFailure: ${p1.message}")
                            }
                        })
                }
            }

            true
        }
        menu.show()
    }

    override fun itemClick(getDetails: GetDetails) {
        findNavController().navigate(R.id.addFragment, bundleOf("key" to 2, "user" to getDetails))
    }




}