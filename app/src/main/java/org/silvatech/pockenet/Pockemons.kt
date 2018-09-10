package org.silvatech.pockenet

import android.location.Location

class Pockemon{

    var name:String?=null
    var descr:String?=null
    var image:Int?=null
    var power:Double?=null
    var latitude:Double?=null
    var longitude:Double?=null
    var location: Location?=null
    var IsCatch:Boolean?=false


    constructor(image:Int,name:String,descr:String,power:Double,latitude:Double,longitude:Double){
        this.name=name
        this.descr=descr
        this.image=image
        this.power=power
//        this.latitude=latitude
//        this.longitude=longitude
        this.location = Location(name)
        this.location!!.latitude=latitude
        this.location!!.longitude=longitude
        this.IsCatch=false
    }

}

