package com.example.iptv.Views.Adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.iptv.Models.DetailItem
import com.example.iptv.R

class DetailAdapter(data: MutableList<DetailItem>):
    BaseQuickAdapter<DetailItem, BaseViewHolder>(R.layout.item_des_detail, data) {
    override fun convert(helper: BaseViewHolder, item: DetailItem?) {
        helper.setText(R.id.item_des, item!!.key)
            .setText(R.id.item_value, item.value)
    }
}