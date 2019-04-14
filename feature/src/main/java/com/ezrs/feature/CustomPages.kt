package com.ezrs.feature




enum class CustomPagers constructor(var mTitleResId: Int, var mLayoutResId: Int) {
    CLAN_NEW(0, R.layout.clan_new),
    CLAN_USERS(1, R.layout.clan_users),
    CLAN_CONDITIONS(2, R.layout.clan_conditions);


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
