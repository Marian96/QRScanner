package com.marianpusk.qrscanner.ui.main.generatecodes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.QRCodesDatabase
import com.marianpusk.qrscanner.databinding.FragmentGenerateQRBinding
import com.marianpusk.qrscanner.databinding.FragmentMyCodesBinding
import com.marianpusk.qrscanner.ui.main.home.MainFragmentDirections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyCodesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCodesFragment : Fragment() {

    private lateinit var viewModel: GenerateQrViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentMyCodesBinding>(inflater, R.layout.fragment_my_codes, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = QRCodesDatabase.getInstance(application).generatedCodes
        val viewModelFactory = GenerateQrViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(GenerateQrViewModel::class.java)

        binding.setLifecycleOwner(this)

        val generatedCodeAdapter = GeneratedCodeRecycleAdapter(GeneratedCodeListener{
            value, id ->
            this.findNavController().navigate(MyCodesFragmentDirections.actionNavMyCodesToGeneratedCodeFragment(value,id))
        })

        binding.generatedRecycleView.adapter = generatedCodeAdapter

        viewModel.qrCodes.observe(viewLifecycleOwner, Observer {
            it?.let {
                generatedCodeAdapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_codes_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

//            R.id.share_single -> viewModel.shareSingle(activity,codeValue)
            R.id.delete_generated -> viewModel.clear()
        }
        return super.onOptionsItemSelected(item)
    }


}