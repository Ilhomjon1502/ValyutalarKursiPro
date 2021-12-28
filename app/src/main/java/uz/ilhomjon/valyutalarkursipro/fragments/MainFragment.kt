package uz.ilhomjon.valyutalarkursipro.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.item_tab_basic_name.view.*
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.adapter.KartaViewPagerAdapter
import uz.ilhomjon.valyutalarkursipro.adapter.RvArxivAdapter
import uz.ilhomjon.valyutalarkursipro.databinding.FragmentMainBinding
import uz.ilhomjon.valyutalarkursipro.databinding.ItemTabBasicNameBinding
import uz.ilhomjon.valyutalarkursipro.db.AppDatabase
import uz.ilhomjon.valyutalarkursipro.models.Valyuta
import uz.ilhomjon.valyutalarkursipro.utils.MyViewModels

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var kartaViewPagerAdapter: KartaViewPagerAdapter
    lateinit var myViewModel:MyViewModels
    lateinit var list:ArrayList<Valyuta>
    lateinit var appDatabase: AppDatabase
    private val TAG = "MainFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModels::class.java)
        myViewModel.getUsers().observe(viewLifecycleOwner, {
            list = ArrayList<Valyuta>()
            list.addAll(it)
            kartaViewPagerAdapter = KartaViewPagerAdapter(list)
            binding.viewPagerKarta.adapter = kartaViewPagerAdapter

            binding.tabIndicatorValyuta.setupWithViewPager(binding.viewPagerKarta)
            binding.tabValutaName.setupWithViewPager(binding.viewPagerKarta)
            setTab()

        })

        setRvArxiv()

        return binding.root
    }

    private fun setRvArxiv() {
        appDatabase = AppDatabase.getInstance(binding.root.context)
        var listD = ArrayList<Valyuta>()
        listD.addAll(appDatabase.valyutaDao().getAllValyutaModel())
        if (listD.isNotEmpty()){

            var valyuta = listD.first()
            var lrv = ArrayList<Valyuta>()
            for (vd in listD) {
                if (vd.code==valyuta.code){
                    lrv.add(vd)
                }
            }
            val rvArxivAdapter = RvArxivAdapter(binding.root.context, lrv)
            binding.rvArxiv.adapter = rvArxivAdapter

            binding.viewPagerKarta.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    var valyuta = list[position]
                    var lrv = ArrayList<Valyuta>()
                    for (vd in listD) {
                        if (vd.code==valyuta.code){
                            lrv.add(vd)
                        }
                    }
                    val rvArxivAdapter = RvArxivAdapter(binding.root.context, lrv)
                    binding.rvArxiv.adapter = rvArxivAdapter
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }

    private fun setTab() {
        val tabCount = binding.tabValutaName.tabCount

        for (i in 0 until tabCount){
            val tabView = ItemTabBasicNameBinding.inflate(layoutInflater)
            val tab = binding.tabValutaName.getTabAt(i)
            tab?.customView = tabView.root

            tabView.tvTabBasic.text = list[i].code

            if (i == 0){
                tabView.tabItemContainer.background = resources.getDrawable(R.drawable.ic_fon_tab)
                tabView.tvTabBasic.setTextColor(Color.WHITE)
            }else{
                tabView.tabItemContainer.background = null
                tabView.tvTabBasic.setTextColor(Color.GRAY)
            }
        }

        binding.tabValutaName.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.tab_item_container?.background = resources.getDrawable(R.drawable.ic_fon_tab)
                customView?.tv_tab_basic?.setTextColor(Color.WHITE)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.tab_item_container?.background = null
                customView?.tv_tab_basic?.setTextColor(Color.GRAY)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}