package com.ezrs.feature




enum class CustomPagers constructor(var mTitleResId: Int, var mLayoutResId: Int) {

    RED(0, R.layout.fragment_tab1),
    REDD(1, R.layout.fragment_tab1);


    fun CustomPagerEnum(titleResId: Int, layoutResId: Int) {
        mTitleResId = titleResId
        mLayoutResId = layoutResId
    }

    fun getTitleResId(): Int {
        return mTitleResId
    }

    fun getLayoutResId(): Int {
        return mLayoutResId
    }


}
