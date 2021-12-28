package uz.ilhomjon.valyutalarkursipro.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.ilhomjon.valyutalarkursipro.fragments.AllValyutaFragment
import uz.ilhomjon.valyutalarkursipro.fragments.CalculatorFragment
import uz.ilhomjon.valyutalarkursipro.fragments.MainFragment

class ViewPagerMainAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0-> MainFragment()
            1-> AllValyutaFragment()
            2-> CalculatorFragment()
            else -> MainFragment()
        }
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return categoryList[position].title
//    }
}