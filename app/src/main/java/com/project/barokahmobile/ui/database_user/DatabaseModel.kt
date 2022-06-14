package com.project.barokahmobile.ui.database_user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DatabaseModel(
    var uid : String? = null,
    var phone : String? = null,
    var name : String? = null,
    var email : String? = null,
    var password : String? = null,
    var address : String? = null,
    var role : String? = null,
    var population : String? = null,
) : Parcelable