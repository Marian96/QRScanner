package com.marianpusk.qrscanner.ui.main.generatecodes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.QRCodesDatabase
import com.marianpusk.qrscanner.database.entities.GeneratedCodeEntity
import com.marianpusk.qrscanner.databinding.FragmentGenerateQRBinding
import com.marianpusk.qrscanner.databinding.MainFragmentBinding
import com.marianpusk.qrscanner.ui.main.home.MainViewModel
import com.marianpusk.qrscanner.ui.main.home.MainViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateQRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateQRFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var viewModel: GenerateQrViewModel

     var name: String = ""
     var value:String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGenerateQRBinding>(inflater, R.layout.fragment_generate_q_r, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = QRCodesDatabase.getInstance(application).generatedCodes
        val viewModelFactory = GenerateQrViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(GenerateQrViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.name.addTextChangedListener(object :TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(p3 > 0) {

                    name = binding.name.text.toString()
                }
            }
        })

        binding.value.addTextChangedListener(object :TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(p3 > 0) {

                    value = binding.value.text.toString()
                }
            }
        })

        binding.generate.setOnClickListener {

            name = binding.name.text.toString()
            value = binding.value.text.toString()

            if(value.isNotEmpty()){

                val qrEncoder = QRGEncoder(value,null, QRGContents.Type.TEXT,500)
                try {

                    val qrBits = qrEncoder.bitmap
                    binding.generatedCodeImage.setImageBitmap(qrBits)

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            else {
                Toast.makeText(context,"The value must not be empty",Toast.LENGTH_SHORT).show()
            }




        }


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.generate_code_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.save_code){

            if (name.isNotEmpty() && value.isNotEmpty()){
                val newCode = GeneratedCodeEntity()
                val currentTime = System.currentTimeMillis()

                newCode.value = name
                newCode.value = value
                newCode.timestamp = currentTime
                viewModel.insertQRCode(newCode)
                this.findNavController().navigate(R.id.action_nav_generate_code_to_nav_my_codes)
            }
            else {
                Toast.makeText(context,"Fields must not be empty",Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }


}