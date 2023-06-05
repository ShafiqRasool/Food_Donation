package com.riphah.food.model

import android.os.Parcel
import android.os.Parcelable

data class ReceiverModel(
    var name: String?,
    var phone: String?,
    var location: String?,
    var occupation: String?,
    var imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this("", "", "", "", null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(location)
        parcel.writeString(occupation)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReceiverModel> {
        override fun createFromParcel(parcel: Parcel): ReceiverModel {
            return ReceiverModel(parcel)
        }

        override fun newArray(size: Int): Array<ReceiverModel?> {
            return arrayOfNulls(size)
        }
    }
}
