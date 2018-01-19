package me.samen.datagendagr

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var user: Provider<User>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerUsrComp.builder().build().inject(this)
        txtVw.text = "Click Here !!!"
        txtVw.setOnClickListener {
            if(it is TextView) it.text = "${it.text}\n${moreData()}"
        }
    }

    private fun moreData(): String {
        val usr = user.get()
        val books = usr.purchases.joinToString { it.isbn }
        return """
                User: ${usr.name}
                Books : ${if(books.isEmpty()) "<none>" else books}
                ---------------------
            """.trimIndent()
    }
}
