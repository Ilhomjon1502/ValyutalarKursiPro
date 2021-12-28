package uz.ilhomjon.valyutalarkursipro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_calculator.*
import uz.ilhomjon.valyutalarkursipro.R
import uz.ilhomjon.valyutalarkursipro.adapter.KartaViewPagerAdapter
import uz.ilhomjon.valyutalarkursipro.adapter.SpinnerAdapter
import uz.ilhomjon.valyutalarkursipro.databinding.FragmentCalculatorBinding
import uz.ilhomjon.valyutalarkursipro.models.Valyuta
import uz.ilhomjon.valyutalarkursipro.utils.MyViewModels
import java.lang.Exception

class CalculatorFragment : Fragment() {

    lateinit var binding: FragmentCalculatorBinding
    lateinit var myViewModel: MyViewModels
    lateinit var list: ArrayList<Valyuta>
    lateinit var spinnerAdapter: SpinnerAdapter
    private val TAG = "CalculatorFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(layoutInflater)

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

            list.add(Valyuta(list.last().date, "1", "UZS", "1", "1", "O'zbek so'mi"))

            spinnerAdapter = SpinnerAdapter(list)
            binding.spinner1.adapter = spinnerAdapter
            binding.spinner2.adapter = spinnerAdapter

            spinner_1.setSelection(list.size - 2)
            spinner_2.setSelection(list.size - 1)

            binding.tvSotishC.text = list[binding.spinner1.selectedItemPosition].nbu_cell_price
            binding.tvOlishC.text = list[binding.spinner1.selectedItemPosition].nbu_buy_price
        })

        binding.imageChangeV.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(context, R.anim.card_anim)
            binding.cardContainer.startAnimation(anim)
            val sip = binding.spinner1.selectedItemPosition
            binding.spinner1.setSelection(binding.spinner2.selectedItemPosition)
            binding.spinner2.setSelection(sip)
            calculation()
        }

        binding.edt1.addTextChangedListener {
            calculation()
        }

        return binding.root
    }

    fun calculation(){
        if (binding.edt1.text.isNotEmpty()) {

            var somOlish =
                list[binding.spinner1.selectedItemPosition].nbu_buy_price.toDouble() * binding.edt1.text.toString()
                    .toDouble()
            var somSotish =
                list[binding.spinner1.selectedItemPosition].nbu_cell_price.toDouble() * binding.edt1.text.toString()
                    .toDouble()

            Log.d(TAG, "onCreateView: somOlish = $somOlish")
            Log.d(TAG, "onCreateView: somSotish = $somSotish")

            var sTv =
                (somSotish / list[binding.spinner2.selectedItemPosition].nbu_cell_price.toDouble())
            var oTv =
                (somOlish / list[binding.spinner2.selectedItemPosition].nbu_buy_price.toDouble())

            if (sTv.toString().contains('E') || oTv.toString().contains('E')) {
                binding.tvOlishC.text = oTv.toInt().toString()
                binding.tvSotishC.text = sTv.toInt().toString()
            } else {
                try {
                    binding.tvSotishC.text =
                        sTv.toString().subSequence(0, sTv.toString().indexOf(".") + 3)
                            .toString()
                    binding.tvOlishC.text =
                        oTv.toString().subSequence(0, oTv.toString().indexOf(".") + 3)
                            .toString()
                } catch (e: Exception) {
                    binding.tvSotishC.text = sTv.toString()
                    binding.tvOlishC.text = oTv.toString()
                }
            }

        } else {
            Toast.makeText(context, "Avval biron-bir qiymat yozing...", Toast.LENGTH_SHORT)
                .show()
        }
    }

}