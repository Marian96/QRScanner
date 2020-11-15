package com.marianpusk.qrscanner.ui.main.generatecodes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.QRCodesDatabase
import com.marianpusk.qrscanner.databinding.CodeDetailFragmentBinding
import com.marianpusk.qrscanner.databinding.FragmentGeneratedCodeBinding
import com.marianpusk.qrscanner.ui.main.generatecodes.GeneratedCodeFragmentArgs.Companion.fromBundle

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GeneratedCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GeneratedCodeFragment : Fragment() {

    private lateinit var viewModel: GenerateQrViewModel
    private var codeValue = ""
    private var codeId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGeneratedCodeBinding>(inflater, R.layout.fragment_generated_code, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = QRCodesDatabase.getInstance(application).generatedCodes
        val viewModelFactory = GenerateQrViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(GenerateQrViewModel::class.java)

        binding.setLifecycleOwner(this)

        val arguments = GeneratedCodeFragmentArgs.fromBundle(requireArguments())
        codeValue = arguments.codeValue
        codeId = arguments.id

        val qrEncoder = QRGEncoder(codeValue,null, QRGContents.Type.TEXT,100)
        try {

            val qrBits = qrEncoder.bitmap
            binding.generatedCodeImage.setImageBitmap(qrBits)

        }catch (e: Exception){
            e.printStackTrace()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.generated_code_detail_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

//            R.id.share_single -> viewModel.shareSingle(activity,codeValue)
            R.id.delete_generated_detail -> deleteGeneratedCode()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteGeneratedCode(){
        viewModel.onDelete(codeId)
    }


}