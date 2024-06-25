package com.example.notes.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentAddBinding
import com.example.notes.models.GetDetails
import com.example.notes.models.PostDetails
import com.example.notes.retrofit.ApiClient
import com.example.notes.utils.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFragment : Fragment() {

    private val binding by lazy { FragmentAddBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MySharedPreference.init(requireContext())
        binding.apply {
            val key = arguments?.getInt("key")
            if (key == 0) {
                btnSave.setOnClickListener {
                    if (binding.title.text.toString().isNotEmpty() || binding.deck.text.toString()
                            .isNotEmpty() || binding.date.text.toString().isNotEmpty()
                    ) {
                        val details = PostDetails(
                            binding.deck.text.toString(),
                            binding.date.text.toString(),
                            binding.title.text.toString()
                        )
                        ApiClient.apiService.addReja("Bearer ${MySharedPreference.token}", details)
                            .enqueue(object : Callback<Any> {
                                override fun onResponse(p0: Call<Any>, p1: Response<Any>) {
                                    if (p1.isSuccessful) {
                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                        findNavController().popBackStack()
                                    } else {
                                        val dialog = AlertDialog.Builder(context)
                                        dialog.setTitle("Xatolik")
                                        dialog.setMessage("${p1.errorBody()?.string()}")
                                        dialog.show()
                                    }
                                }

                                override fun onFailure(p0: Call<Any>, p1: Throwable) {
                                    Toast.makeText(context, "Error ${p1.message}", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            })
                    } else {
                        Toast.makeText(context, "Bo'limlardan biri bo'sh", Toast.LENGTH_SHORT).show()
                    }

                }
            }else if (key == 1){
                val getDetails = arguments?.getSerializable("getDetails") as GetDetails
                binding.title.setText(getDetails.sarlavha)
                binding.deck.setText(getDetails.batafsil)
                binding.date.setText(getDetails.muddat)
                btnSave.setOnClickListener {
                    if (binding.title.text.toString().isNotEmpty() || binding.deck.text.toString()
                            .isNotEmpty() || binding.date.text.toString().isNotEmpty()){
                        val details = PostDetails(
                            binding.deck.text.toString(),
                            binding.date.text.toString(),
                            binding.title.text.toString()
                        )
                        ApiClient.apiService.editReja("Bearer ${MySharedPreference.token}",getDetails.id,details)
                            .enqueue(object :Callback<Any>{
                                override fun onResponse(p0: Call<Any>, p1: Response<Any>) {
                                    if (p1.isSuccessful){
                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                        findNavController().popBackStack()
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "${p1.errorBody()?.string()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(p0: Call<Any>, p1: Throwable) {
                                    Toast.makeText(context, "Error ${p1.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }
            }else if (key == 2){
                val getDetails = arguments?.getSerializable("user") as GetDetails
                binding.title.setText(getDetails.sarlavha)
                binding.deck.setText(getDetails.batafsil)
                binding.date.setText(getDetails.muddat)
                binding.btnSave.visibility = View.GONE
                binding.toolbar.text = "My Note"

            }


        }
        return binding.root
    }

}