package com.marianpusk.qrscanner.ui.main.scanner

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.QRCodesDatabase
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import com.marianpusk.qrscanner.databinding.ScannerFragmentBinding


class ScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    val MY_CAMERA_PERMISSION_REQUEST = 111

    companion object {
        fun newInstance() =
            ScannerFragment()
    }

    private lateinit var viewModel: ScannerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding = DataBindingUtil.inflate<ScannerFragmentBinding>(inflater, R.layout.scanner_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val application = this.requireActivity().application

        val dataSource = QRCodesDatabase.getInstance(application).qrcodeDatabase
        val viewModelFactory = ScannerViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(ScannerViewModel::class.java)

        val activity = requireActivity()
        val scannerView = view?.findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(activity, scannerView!!)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {

                val codeValue = it.text
                val qrCode = QRCodeEntity()
                val current = System.currentTimeMillis()

                qrCode.timestamp = current
                qrCode.value = codeValue

                viewModel.insertQRCode(qrCode)

                this.findNavController().navigate(R.id.action_scannerFragment_to_nav_home)
            }
        }
        scannerView.setOnClickListener {
            checkPermission()        }

        //super.onViewCreated(view, savedInstanceState)
    }



    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),MY_CAMERA_PERMISSION_REQUEST)
        }else {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == MY_CAMERA_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            codeScanner.startPreview()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}