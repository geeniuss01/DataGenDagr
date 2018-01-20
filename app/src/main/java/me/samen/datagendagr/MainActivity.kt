package me.samen.datagendagr

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userApi: UserApi

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerUsrComp.builder().build().inject(this)
        listV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = Adapter(userApi.users().blockingFirst().toMutableList(), this@MainActivity)
        }
        swipeRefresh.setOnRefreshListener {
            (listV.adapter as Adapter).prepend(userApi.users().blockingFirst())
            swipeRefresh.isRefreshing = false
            (listV.layoutManager as LinearLayoutManager).scrollToPosition(0)
        }

    }
}


class VH(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(user: User) {
        with(itemView) {
            usernameTV.text = user.name
            book1TV.text = if(user.purchases.isNotEmpty()) "${user.purchases[0].isbn} ${user.purchases[0].name}" else "<None>"
            book2TV.text = if(user.purchases.size>1) "${user.purchases[1].isbn} ${user.purchases[1].name}" else ""
        }
    }
}

class Adapter(private val users: MutableList<User>,private val ctx: Context): RecyclerView.Adapter<VH>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_list, parent, false)
        return VH(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: VH?, position: Int)  {
        holder?.bind(users[position])
    }

    fun prepend(items: List<User>) {
        users.addAll(0, items)
        notifyItemRangeInserted(0, items.size)
    }
}