package uz.ilhomjon.valyutalarkursipro.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_calculator.*
import uz.ilhomjon.valyutalarkursipro.BindingObject
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.adapter.KartaViewPagerAdapter
import uz.ilhomjon.valyutalarkursipro.adapter.RvAllValyutaAdapter
import uz.ilhomjon.valyutalarkursipro.adapter.SpinnerAdapter
import uz.ilhomjon.valyutalarkursipro.databinding.FragmentAllValyutaBinding
import uz.ilhomjon.valyutalarkursipro.databinding.FragmentCalculatorBinding
import uz.ilhomjon.valyutalarkursipro.models.Valyuta
import uz.ilhomjon.valyutalarkursipro.utils.MyViewModels
import java.lang.Exception

class AllValyutaFragment : Fragment() {

    lateinit var binding: FragmentAllValyutaBinding
    lateinit var rvAllValyutaAdapter: RvAllValyutaAdapter
    lateinit var myViewModel: MyViewModels
    lateinit var list: ArrayList<Valyuta>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllValyutaBinding.inflate(layoutInflater)

        BindingObject.activityBinding?.appBarMain?.searchBasic?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var l = ArrayList<Valyuta>()
                list.forEach {
                    if (it.code.toLowerCase().contains(newText?.toLowerCase()!!.toRegex())) {
                        l.add(it)
                    }
                }
                rvSetAdapter(l)
                return true
            }
        })

        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModels::class.java)
        myViewModel.getUsers().observe(viewLifecycleOwner, {
            list = ArrayList<Valyuta>()
            list.addAll(it)
            list.forEach {
                if (it.nbu_cell_price == "")
                    it.nbu_cell_price = it.cb_price
                if (it.nbu_buy_price == "")
                    it.nbu_buy_price = it.cb_price
            }

            rvSetAdapter(list)
        })


        return binding.root
    }

    fun rvSetAdapter(list: List<Valyuta>) {
        rvAllValyutaAdapter =
            RvAllValyutaAdapter(binding.root.context, list, object : RvAllValyutaAdapter.RvClick {
                override fun onCalculator(valyuta: Valyuta) {
                    var list2 = ArrayList<Valyuta>()
                    list2.addAll(list)
                    list2.add(Valyuta(list.last().date, "1", "UZS", "1", "1", "o'zbek so'mi"))

                    val index = list2.indexOf(valyuta)
                    val alerDialog = AlertDialog.Builder(context, R.style.NewDialog).create()
                    val fragmentCCalculatorBinding =
                        FragmentCalculatorBinding.inflate(layoutInflater)
                    alerDialog.setView(fragmentCCalculatorBinding.root)

//                    fragmentCCalculatorBinding.vContainer.visibility = View.GONE
//                    fragmentCCalculatorBinding.cardContainer.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    fragmentCCalculatorBinding.vContainer1.visibility = View.VISIBLE

                    val spinnerAdapter = SpinnerAdapter(list2)
                    fragmentCCalculatorBinding.spinner1.adapter = spinnerAdapter
                    fragmentCCalculatorBinding.spinner2.adapter = spinnerAdapter

                    fragmentCCalculatorBinding.spinner1.setSelection(index)
                    fragmentCCalculatorBinding.spinner2.setSelection(list2.size - 1)

                    if (valyuta.nbu_cell_price != "" || valyuta.nbu_buy_price != "") {
                        fragmentCCalculatorBinding.tvOlishC.text = valyuta.nbu_buy_price
                        fragmentCCalculatorBinding.tvSotishC.text = valyuta.nbu_cell_price
                    } else {
                        fragmentCCalculatorBinding.tvOlishC.text = valyuta.cb_price
                        fragmentCCalculatorBinding.tvSotishC.text = valyuta.cb_price
                    }
                    fragmentCCalculatorBinding.imageChangeV.setOnClickListener {
                        val anim = AnimationUtils.loadAnimation(context, R.anim.card_anim)
                        fragmentCCalculatorBinding.cardContainer.startAnimation(anim)
                        val sip = fragmentCCalculatorBinding.spinner1.selectedItemPosition
                        fragmentCCalculatorBinding.spinner1.setSelection(fragmentCCalculatorBinding.spinner2.selectedItemPosition)
                        fragmentCCalculatorBinding.spinner2.setSelection(sip)

                        calculationF(fragmentCCalculatorBinding, list2)
                    }

                    fragmentCCalculatorBinding.edt1.addTextChangedListener {
                        calculationF(fragmentCCalculatorBinding, list2)
                    }

                    alerDialog.show()
                }
            })
        binding.rvAllValyuta.adapter = rvAllValyutaAdapter
    }

    fun calculationF(fragmentCCalculatorBinding:FragmentCalculatorBinding, list2:ArrayList<Valyuta>){
        if (fragmentCCalculatorBinding.edt1.text.isNotEmpty()) {
            var somOlish =
                list2[fragmentCCalculatorBinding.spinner1.selectedItemPosition].nbu_buy_price.toDouble() * fragmentCCalculatorBinding.edt1.text.toString()
                    .toDouble()
            var somSotish =
                list2[fragmentCCalculatorBinding.spinner1.selectedItemPosition].nbu_cell_price.toDouble() * fragmentCCalculatorBinding.edt1.text.toString()
                    .toDouble()

            var sTv =
                (somSotish / list2[fragmentCCalculatorBinding.spinner2.selectedItemPosition].nbu_cell_price.toDouble())
            var oTv =
                (somOlish / list2[fragmentCCalculatorBinding.spinner2.selectedItemPosition].nbu_buy_price.toDouble())

            if (sTv.toString().contains('E') || oTv.toString().contains('E')) {
                fragmentCCalculatorBinding.tvOlishC.text = oTv.toInt().toString()
                fragmentCCalculatorBinding.tvSotishC.text = sTv.toInt().toString()
            } else {
                try {
                    fragmentCCalculatorBinding.tvSotishC.text =
                        sTv.toString().subSequence(0, sTv.toString().indexOf(".") + 3)
                            .toString()
                    fragmentCCalculatorBinding.tvOlishC.text =
                        oTv.toString().subSequence(0, oTv.toString().indexOf(".") + 3)
                            .toString()
                } catch (e: Exception) {
                    fragmentCCalculatorBinding.tvSotishC.text = sTv.toString()
                    fragmentCCalculatorBinding.tvOlishC.text = oTv.toString()
                }
            }
        } else {
            Toast.makeText(
                context,
                "Avval biron-bir qiymat yozing...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}