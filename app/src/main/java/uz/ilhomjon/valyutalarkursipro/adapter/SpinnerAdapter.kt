package uz.ilhomjon.valyutalarkursipro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_spinner_c.view.*
import kotlinx.android.synthetic.main.item_vp_karta.view.*
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

class SpinnerAdapter(var list: List<Valyuta>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView: View
        if (convertView == null) {
            itemView =
                LayoutInflater.from(parent?.context).inflate(R.layout.item_spinner_c, parent, false)
        } else {
            itemView = convertView
        }

        itemView.tv_item_spinner.text = list[position].code

        val f1 = "https://nbu.uz/local/templates/nbu/images/flags/"
        val f2 = ".png"
        if (position == list.size - 1) {
            itemView.image_spinner_item.setImageResource(R.drawable.flag_uzb)
        }else{
            Picasso.get().load("${f1}${list[position].code}$f2").into(itemView.image_spinner_item)
        }


        return itemView
    }
}