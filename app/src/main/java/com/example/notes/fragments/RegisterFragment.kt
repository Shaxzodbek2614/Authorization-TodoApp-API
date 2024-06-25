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
import com.example.notes.databinding.FragmentRegisterBinding
import com.example.notes.models.PostRequestUser
import com.example.notes.models.PostResponseToken
import com.example.notes.retrofit.ApiClient
import com.example.notes.utils.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterFragment : Fragment() {

    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.apply {
            MySharedPreference.init(requireContext())
            btnSignUp.setOnClickListener {
                if (tvUsername.text.isEmpty() || tvPassword.text.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                progressBar.visibility = View.VISIBLE
                btnSignUp.isEnabled = false
                val postRequestUser = PostRequestUser(
                    binding.tvPassword.text.toString(),
                    binding.tvUsername.text.toString()
                )
                ApiClient.apiService.registerUser(postRequestUser)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(p0: Call<String>, p1: Response<String>) {
                            if (p1.isSuccessful) {
                                progressBar.visibility = View.GONE
                                btnSignUp.isEnabled = true
                                ApiClient.apiService.loginUser(postRequestUser)
                                    .enqueue(object :Callback<PostResponseToken>{
                                        override fun onResponse(
                                            p0: Call<PostResponseToken>,
                                            p1: Response<PostResponseToken>
                                        ) {
                                            progressBar.visibility = View.GONE
                                            btnSignUp.isEnabled = true
                                            if (p1.isSuccessful){
                                                val t = p1.body()?.access
                                                MySharedPreference.token = t!!
                                                Toast.makeText(context, "Muvaffaqqiyatli o'tildi", Toast.LENGTH_SHORT).show()
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
                                            Toast.makeText(context, "p1.message", Toast.LENGTH_SHORT).show()
                                            progressBar.visibility = View.GONE
                                            btnSignUp.isEnabled = true
                                        }
                                    })
                            }else{
                                val dialog = AlertDialog.Builder(context)
                                dialog.setTitle("Error")
                                dialog.setMessage(p1.errorBody()?.string())
                                dialog.show()
                                progressBar.visibility = View.GONE
                                btnSignUp.isEnabled = true
                            }
                        }

                        override fun onFailure(p0: Call<String>, p1: Throwable) {
                            Toast.makeText(context, "Error salom", Toast.LENGTH_SHORT).show()
                        }
                    })

            }
        }
        return binding.root
    }


}