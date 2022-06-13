package com.project.barokahmobile.ui.info_pakan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoPakanModel(
    var name : String? = null,
    var uid : String? = null,
    var description : String? = null,
    var date : String? = null,
    var image : String? = null,
) : Parcelable
