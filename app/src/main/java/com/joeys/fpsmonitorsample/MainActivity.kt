package com.joeys.fpsmonitorsample

import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.drakeet.multitype.ItemViewBinder
import com.drakeet.multitype.MultiTypeAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var screenWidth = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private val adapter = MultiTypeAdapter()

    private fun initView() {
        screenWidth = resources.displayMetrics.widthPixels
        adapter.register(MyBinder(screenWidth))
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.items =
            listOf(
                Entity(R.drawable.pic1, "pic1"),
                Entity(R.drawable.pic2, "pic2"),
                Entity(R.drawable.pic3, "pic3"),
                Entity(R.drawable.pic4, "pic4"),
                Entity(R.drawable.pic5, "pic5"),
                Entity(R.drawable.pic6, "pic6"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic5, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic2, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic1, "pic7"),
                Entity(R.drawable.pic4, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic7, "pic7"),
                Entity(R.drawable.pic8, "pic8")
            )
        recyclerview.adapter = adapter


        btnJank.setOnClickListener {
            Thread.sleep(600)
        }
    }


    class MyBinder(val screenWidth: Int) : ItemViewBinder<Entity, MyBinder.VH>() {

        override fun onBindViewHolder(holder: VH, item: Entity) {
            Thread.sleep(30)
            Glide.with(holder.mIv)
                .load(item.pic)
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        holder.mIv.layoutParams.apply {
                            width = screenWidth
                            height =
                                resource.intrinsicHeight * screenWidth / resource.intrinsicWidth
                        }
                        holder.mIv.setImageDrawable(resource)

                    }

                })
            holder.mTv.text = item.text
        }

        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
            return VH(inflater.inflate(R.layout.item_layout, parent, false))
        }

        class VH(view: View) : RecyclerView.ViewHolder(view) {
            val mIv = view.findViewById<ImageView>(R.id.iv)
            val mTv = view.findViewById<TextView>(R.id.tv)
        }
    }


    class Entity(
        @DrawableRes
        var pic: Int = R.drawable.ic_launcher_background,
        var text: String = ""
    )
}