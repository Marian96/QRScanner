package com.marianpusk.qrscanner.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.marianpusk.qrscanner.R
import kotlinx.android.synthetic.main.share_dialog.view.*

class ShareDialogFragment(val viewModel: MainViewModel, val fragmentActivity: FragmentActivity) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.share_dialog,container,false)


        fun getOption(){

        }

        rootView.positive_button.setOnClickListener {
            val csvButton = rootView.export_csv
            val textButton = rootView.export_text
            val nieco = rootView.radio_group.checkedRadioButtonId
            var dialogLayout = layoutInflater.inflate(R.layout.share_dialog,null)

            val radioButton = dialogLayout.findViewById<RadioButton>(nieco)

            if (radioButton.text == "CSV"){

                dismiss()
                viewModel.writeFile(fragmentActivity)
                viewModel.shareCsv(fragmentActivity)

            }
            else if (radioButton.text == "Text"){
                viewModel.shareText(fragmentActivity)
                dismiss()
            }

            else dismiss()


//            }else if (textButton.isActivated){
//                dismiss()
//            }else{
//                dismiss()
//            }
        }

        rootView.negative_button.setOnClickListener {
            dismiss()
        }


        return rootView
    }
}