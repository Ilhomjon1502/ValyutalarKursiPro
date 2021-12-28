package uz.ilhomjon.valyutalarkursipro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.databinding.ItemRvAllMoneyBinding
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

class RvAllValyutaAdapter(val context: Context, val list: List<Valyuta>, var onCLick:RvClick) : RecyclerView.Adapter<RvAllValyutaAdapter.Vh>() {

    inner class Vh(var itemRv: ItemRvAllMoneyBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(valyuta: Valyuta) {
            val anim = AnimationUtils.loadAnimation(context, R.anim.rv_item_anim)
            itemRv.root.startAnimation(anim)
            itemRv.tvNameItem.text = valyuta.code
            if (valyuta.nbu_buy_price!=""){
                itemRv.tvOlishNarxi.text = valyuta.nbu_buy_price
            }else{
                itemRv.tvOlishNarxi.text = valyuta.cb_price
            }
            if (valyuta.nbu_cell_price!=""){
                itemRv.tvSotishNarxi.text = valyuta.nbu_cell_price
            }else{
                itemRv.tvSotishNarxi.text = valyuta.cb_price
            }
            itemRv.imageCalculation.setOnClickListener {
                onCLick.onCalculator(valyuta)
            }
            val f1 = "https://nbu.uz/local/templates/nbu/images/flags/"
            val f2 = ".png"
            Picasso.get().load("${f1}${list[position].code}$f2").into(itemRv.imageMoney)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvAllMoneyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface RvClick{
        fun onCalculator(valyuta: Valyuta)
    }
}