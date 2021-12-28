package uz.ilhomjon.valyutalarkursipro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_vp_karta.view.*
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

class KartaViewPagerAdapter(val list: List<Valyuta>) : PagerAdapter(){
    override fun getCount(): Int = list.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(container.context).inflate(R.layout.item_vp_karta, container, false)

        layoutInflater.tv_date.text = list[position].date
        layoutInflater.tv_olish_narxi.text = list[position].nbu_cell_price
        layoutInflater.tv_sotish_narxi.text = list[position].nbu_buy_price

        val flagUrl = "https://nbu.uz/local/templates/nbu/images/flags/UZ.png"
        val f1 = "https://nbu.uz/local/templates/nbu/images/flags/"
        val f2 = ".png"

        Picasso.get().load("${f1}${list[position].code}$f2").into(layoutInflater.image_dollor)

        container.addView(layoutInflater)

        return layoutInflater
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view= `object` as View
        container.removeView(view)
    }
}