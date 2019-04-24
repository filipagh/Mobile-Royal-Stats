package com.ezrs.feature

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class CustomPagerAdapter(private val mContext: Context) : PagerAdapter() {


    override fun instantiateItem(collection: ViewGroup, position: Int): Any {

        val customPagerEnum = CustomPagers.values()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(customPagerEnum.getLayoutResId(), collection, false) as ViewGroup
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return CustomPagers.values().size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val customPagerEnum = CustomPagers.values()[position]
        return mContext.getString(customPagerEnum.getTitleResId())
    }

}
