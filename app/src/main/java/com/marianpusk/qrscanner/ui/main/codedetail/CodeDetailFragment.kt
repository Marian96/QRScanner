package com.marianpusk.qrscanner.ui.main.codedetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.QRCodesDatabase
import com.marianpusk.qrscanner.databinding.CodeDetailFragmentBinding
import com.marianpusk.qrscanner.databinding.ScannerFragmentBinding
import com.marianpusk.qrscanner.ui.main.home.MainViewModel
import com.marianpusk.qrscanner.ui.main.home.MainViewModelFactory

class CodeDetailFragment : Fragment() {

    companion object {
        fun newInstance() = CodeDetailFragment()
    }

    private lateinit var viewModel: MainViewModel
    lateinit var codeValue: String
    var codeId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<CodeDetailFragmentBinding>(inflater, R.layout.code_detail_fragment, container, false)
        val application = requireNotNull(this.activity).application


        val dataSource = QRCodesDatabase.getInstance(application).qrcodeDatabase
        val viewModelFactory = MainViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel::class.java)

        binding.setLifecycleOwner(this)


        val arguments = CodeDetailFragmentArgs.fromBundle(requireArguments())
        codeValue = arguments.codeValue
        codeId = arguments.codeId


        binding.codeValue.text = codeValue
        val qrCodeImage = binding.codeImage

        val qrEncoder = QRGEncoder(codeValue,null,QRGContents.Type.TEXT,100)
        try {

          val qrBits = qrEncoder.bitmap
            binding.codeImage.setImageBitmap(qrBits)

        }catch (e: Exception){
            e.printStackTrace()
        }

        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.code_detail_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.share_single -> viewModel.shareSingle(activity,codeValue)
            R.id.delete_item -> deleteCode()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteCode(){
        this.findNavController().navigate(R.id.action_codeDetailFragment_to_nav_home)
        viewModel.onDelete(codeId)
    }



}