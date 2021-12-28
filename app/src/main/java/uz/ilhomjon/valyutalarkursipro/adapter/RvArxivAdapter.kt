package uz.ilhomjon.valyutalarkursipro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.databinding.ItemRvTarixBinding
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

class RvArxivAdapter(var context: Context, val list: List<Valyuta>) : RecyclerView.Adapter<RvArxivAdapter.Vh>() {

    inner class Vh(var itemRv: ItemRvTarixBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(valyuta: Valyuta) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.rv_item_anim)
            itemRv.root.startAnimation(animation)
            itemRv.tvDate.text = valyuta.date.subSequence(0, valyuta.date.indexOf(" "))
            itemRv.tvTime.text = valyuta.date.subSequence(valyuta.date.indexOf(" "), valyuta.date.length)

            if (valyuta.nbu_buy_price!="" || valyuta.nbu_cell_price!="") {
                itemRv.tvSot.text = valyuta.nbu_cell_price
                itemRv.tvOl.text = valyuta.nbu_buy_price
            }else{
                itemRv.tvOl.text = valyuta.cb_price
                itemRv.tvSot.text = valyuta.cb_price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvTarixBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}