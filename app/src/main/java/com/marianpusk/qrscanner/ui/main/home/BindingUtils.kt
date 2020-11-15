package com.marianpusk.qrscanner.ui.main.home

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.marianpusk.qrscanner.R
import com.marianpusk.qrscanner.database.entities.GeneratedCodeEntity
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("codeImage")
fun ImageView.setImage(item: QRCodeEntity?) {
    item?.let {
        setImageResource(R.drawable.ic_delete_forever)
    }
}

@BindingAdapter("codeValue")
fun TextView.setPlanName(item: QRCodeEntity?) {
    item?.let {
        text = item.value
    }
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return format.format(date)
}

@BindingAdapter("date")
    fun TextView.setDate(item: QRCodeEntity?){
        item?.let {

            text = convertLongToTime(item.timestamp)
        }
    }

@BindingAdapter("arrow")
fun ImageView.setArrowImage(item: QRCodeEntity?){
    item?.let{
        setImageResource(R.drawable.arrow_right)
    }

}

@BindingAdapter("newCode")
fun TextView.setCodeValue(item: GeneratedCodeEntity?) {
    item?.let {
        text = item.value
    }
}

@BindingAdapter("newArrow")
fun ImageView.setArrowImage(item: GeneratedCodeEntity?){
    item?.let{
        setImageResource(R.drawable.arrow_right)
    }

}

