package uz.ilhomjon.valyutalarkursipro

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.tab_layout_main.view.*
import uz.ilhomjon.valyutalarkursipro.adapter.ViewPagerMainAdapter
import uz.ilhomjon.valyutalarkursipro.databinding.ActivityMainBinding
import uz.ilhomjon.valyutalarkursipro.models.Valyuta
import uz.ilhomjon.valyutalarkursipro.utils.MyViewModels
import android.content.Intent
import android.widget.Toast
import uz.ilhomjon.valyutalarkursipro.db.AppDatabase
import java.lang.Exception


//api link: https://nbu.uz/uz/exchange-rates/json/
//api flag link: https://nbu.uz/local/templates/nbu/images/flags/CHF.png

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"
    lateinit var appDatabase: AppDatabase
    lateinit var listValyuta: ArrayList<Valyuta>

    lateinit var viewPagerMainAdapter: ViewPagerMainAdapter
    lateinit var myViewModel: MyViewModels

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BindingObject.activityBinding = ActivityMainBinding.inflate(layoutInflater)
        binding = BindingObject.activityBinding!!
        setContentView(binding.root)

        binding.appBarMain.menuImage.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.START)
        }

        binding.navView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                val id: Int = item.getItemId()
                when (id) {
                    R.id.menu_share -> {
                        try {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                            var shareMessage = "\nAndroid loyiha\n\n"
                            shareMessage =
                                """
                                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                                
                                
                                """.trimIndent()
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                            startActivity(Intent.createChooser(shareIntent, "choose one"))
                        } catch (e: Exception) {
                            //e.toString();
                        }
                    }
                    R.id.menu_app_info -> Toast.makeText(
                        this@MainActivity,
                        "Dastur Ilhomjon Ibragimov tomonidan ishlab chiqildi",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                binding.drawerLayout.close()
                return true
            }
        })

        loadValyutaInAPI()

        viewPagerMainAdapter = ViewPagerMainAdapter(supportFragmentManager)

        binding.appBarMain.viewPagerBasic.adapter = viewPagerMainAdapter
        binding.appBarMain.tabLayoutMain.setupWithViewPager(binding.appBarMain.viewPagerBasic)
        setTabs()

        binding.appBarMain.tabLayoutMain.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val position = tab?.position
                when (position) {
                    0 -> customView?.image_tab_item_main?.setImageResource(R.drawable.ic_bosh_oyna_selected)
                    1 -> {
                        customView?.image_tab_item_main?.setImageResource(R.drawable.ic_all_valyuta_selected)
                        binding.appBarMain.searchBasic.visibility = View.VISIBLE
                    }
                    2 -> customView?.image_tab_item_main?.setImageResource(R.drawable.ic_calculator_selected)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val position = tab?.position
                when (position) {
                    0 -> customView?.image_tab_item_main?.setImageResource(R.drawable.ic_bosh_oyna_unselected)
                    1 -> {
                        customView?.image_tab_item_main?.setImageResource(R.drawable.ic_all_valyuta_unselected)
                        binding.appBarMain.searchBasic.visibility = View.GONE
                    }
                    2 -> customView?.image_tab_item_main?.setImageResource(R.drawable.ic_calculator_unselected)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun loadValyutaInAPI() {
        myViewModel = ViewModelProvider(this@MainActivity).get(MyViewModels::class.java)
        myViewModel.getUsers().observe(this@MainActivity, Observer {
            Log.d(TAG, "onCreate: $it")
            Log.d(TAG, "onCreate: ================================")
            appDatabase = AppDatabase.getInstance(this)
            listValyuta = ArrayList()
            listValyuta.addAll(appDatabase.valyutaDao().getAllValyutaModel())

            if (listValyuta.isNotEmpty()) {
                if (listValyuta.last().date != it.last().date) {
                    for (valyuta in it) {
                        appDatabase.valyutaDao().addValyutaModel(valyuta)
                        Log.d(TAG, "loadValyutaInAPI: $valyuta ===1")
                    }
                }
            } else {
                for (valyuta in it) {
                    appDatabase.valyutaDao().addValyutaModel(valyuta)
                    Log.d(TAG, "loadValyutaInAPI: $valyuta ===2")
                }
            }
        })

    }

    private fun setTabs() {
        val tabCount = tab_layout_main.tabCount

        for (i in 0 until tabCount) {
            val tabView = LayoutInflater.from(this).inflate(R.layout.tab_layout_main, null, false)
            val tab = tab_layout_main.getTabAt(i)
            tab?.customView = tabView

            when (i) {
                0 -> tabView.image_tab_item_main.setImageResource(R.drawable.ic_bosh_oyna_selected)
                1 -> {
                    tabView.image_tab_item_main.setImageResource(R.drawable.ic_all_valyuta_unselected)
                    binding.appBarMain.searchBasic.visibility = View.GONE
                }
                2 -> tabView.image_tab_item_main.setImageResource(R.drawable.ic_calculator_unselected)
            }
        }
    }
}
object BindingObject{
    var activityBinding:ActivityMainBinding?= null
}