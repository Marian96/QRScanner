package com.marianpusk.qrscanner.ui.main.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.QRCodesDatabase
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import com.marianpusk.qrscanner.databinding.MainFragmentBinding

class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = DataBindingUtil.inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false)
        val application = requireNotNull(this.activity).application
//        binding.recycleView.setBackgroundColor(Color.WHITE)

        val dataSource = QRCodesDatabase.getInstance(application).qrcodeDatabase
        val viewModelFactory = MainViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel::class.java)

        binding.setLifecycleOwner(this)


        val add = binding.add
        add.setOnClickListener{
            this.findNavController().navigate(R.id.action_mainFragment_to_scannerFragment)
//            var code1 = QRCodeEntity()
//            var code2 = QRCodeEntity()
//            code1.value = "abcdeafgh"
//            code2.value = "123455t"
//            viewModel.insertQRCode(code1)
//            viewModel.insertQRCode(code2)
        }

        val qrcodeAdapter = QRCodeRecycleAdapter(QRCodeListener {
            id ->
        },DeleteCodeListener{
            id -> viewModel.onDelete(id)
        })

        binding.recycleView.adapter = qrcodeAdapter

        viewModel.qrCodes.observe(viewLifecycleOwner, Observer {
            it?.let {
                qrcodeAdapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        setHasOptionsMenu(true)
        inflater.inflate(R.menu.main_fragment_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val itemLayout = layoutInflater.inflate(R.layout.codes,null)
        val image = itemLayout.findViewById<ImageView>(R.id.delete_image)

        when(item.itemId){

            R.id.share_item -> showShareDialog()
            R.id.deleteAll -> viewModel.clear()
            R.id.delete -> image.visibility - View.VISIBLE
        }

        if(item.itemId == R.id.share_item){

        showShareDialog()

        }

        if (item.itemId == R.id.deleteAll){
            viewModel.clear()
        }




        return super.onOptionsItemSelected(item)
    }

    private fun showShareDialog(){
        val dialog = ShareDialogFragment(viewModel,requireActivity())


        dialog.show(requireActivity().supportFragmentManager,"customDialog")



    }



}