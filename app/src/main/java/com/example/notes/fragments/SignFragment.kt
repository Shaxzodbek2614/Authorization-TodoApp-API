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
import com.example.notes.databinding.FragmentSignBinding
import com.example.notes.models.PostRequestUser
import com.example.notes.models.PostResponseToken
import com.example.notes.retrofit.ApiClient
import com.example.notes.utils.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignFragment : Fragment() {

    private val binding by lazy { FragmentSignBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context?.let { MySharedPreference.init(it) }
        if (MySharedPreference.token!="null"){
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
            return binding.root
        }
        binding.apply {
            btnRegistr.setOnClickListener {
                findNavController().navigate(R.id.registrFragment)
            }
            btnSign.setOnClickListener {
               progressBar.visibility = View.VISIBLE
                btnSign.isEnabled = false
                btnRegistr.isEnabled = false
                val postRequestUser = PostRequestUser(
                    tvPassword.text.toString(),
                    tvUsername.text.toString()
                )
                ApiClient.apiService.loginUser(postRequestUser)
                    .enqueue(object :Callback<PostResponseToken>{
                        override fun onResponse(
                            p0: Call<PostResponseToken>,
                            p1: Response<PostResponseToken>
                        ) {
                            progressBar.visibility = View.GONE
                            btnSign.isEnabled = true
                            btnRegistr.isEnabled = true
                            if (p1.isSuccessful){
                                val t = p1.body()?.access
                                MySharedPreference.token = t!!
                                Toast.makeText(context, p1.message(), Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                                findNavController().navigate(R.id.homeFragment)
                            }else{
                                val dialog = AlertDialog.Builder(context)
                                dialog.setTitle("Error")
                                dialog.setMessage(p1.errorBody()?.string())
                                dialog.show()
                            }
                        }

                        override fun onFailure(p0: Call<PostResponseToken>, p1: Throwable) {
                            Toast.makeText(context, p1.message, Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                            btnSign.isEnabled = true
                            btnRegistr.isEnabled = true
                        }
                    })
            }
        }
        return binding.root
    }

}