package com.unreal.memeit

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

    }

    private fun loadMeme(){

        progressBar.visibility = View.VISIBLE           // show progress bar while meme is loading

        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme" // Link of API from which meme gonna be loaded

        // Request a json response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    currentImageUrl = response.getString("url") //Put meme url in this variable

                    //Image Processing using GLIDE LIBRARY
                    Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE  //Disappearing progresBar
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                    }).into(img) //Load that meme using [var currentImgUrl]
            },
            Response.ErrorListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest) //add json object in queue
    }

    fun nextMeme(view: View) {
        loadMeme()
    }

    fun shareMeme(view: View) {
        val shareintent = Intent(Intent.ACTION_SEND)
        shareintent.type = "text/plain"
        //what's actually going send after executing share option (1.Msg for receiver  &  2.URL of content)⤵
        shareintent.putExtra(Intent.EXTRA_TEXT, "Hey! Check out this cool meme I got from Reddit $currentImageUrl")
        //Intent.createChooser() for sharing panel/intent we see after selecting share option⤵
        val chooser = Intent.createChooser(shareintent, "Share this meme using...")
        startActivity(chooser)
    }
}
