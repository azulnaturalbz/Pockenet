package org.silvatech.pockenet

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        LoadPockemon()
    }

    var ACCESS_LOCATION = 123

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)
                return
            }
        }

        GetUserLocation()
    }

    fun GetUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()
        //TODO Will implement later

        var myLocation = MylocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
        var myThread = myThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            ACCESS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, "We cannot access your location: ", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }


    var location: Location? = null

    //Get User Location
    inner class MylocationListener : LocationListener {


        constructor() {
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(p0: Location?) {
            location = p0
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onStatusChanged(p0: String?, status: Int, extras: Bundle?) {

            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    var oldLocation:Location?=null

    inner class myThread : Thread {
        constructor() : super() {
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }

        override fun run() {
            while (true) {
                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation=location

                    runOnUiThread {
                        mMap!!.clear()
                        // Add a marker in Sydney and move the camera
                        val bzecity = LatLng(location!!.latitude,location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(bzecity)
                                .title("Me")
                                .snippet(" Here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bzecity, 18f))


                        //show pockemon
                        for(i in 0..listPockemon.size-1){
                            var newPockemon=listPockemon[i]

                            if(newPockemon.IsCatch==false){
                                val pockemonLoc = LatLng(newPockemon.location!!.latitude,newPockemon.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                        .position(pockemonLoc)
                                        .title(newPockemon.name!!)
                                        .snippet(newPockemon.descr!! +", power: ${newPockemon.power}")
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!))


                                )

                                if(location!!.distanceTo(newPockemon.location)<2){
                                    newPockemon.IsCatch=true
                                    listPockemon[i]=newPockemon
                                    playPower+=newPockemon.power!!
                                    Toast.makeText(applicationContext,"You catched a new pockemon and your power is : $playPower", Toast.LENGTH_LONG).show()
                                }
                            }

                        }
                    }
                    Thread.sleep(1000)

                } catch (ex: Exception) {

                }
            }
        }
    }

    var playPower=0.0
    var listPockemon=ArrayList<Pockemon>()

    fun LoadPockemon(){


        listPockemon.add(Pockemon(R.drawable.charmander,
                "Charmander","Hello I am from Japan", 55.0,17.490266,  -88.198394))
        listPockemon.add(Pockemon(R.drawable.bulbasaur,
                "Bulbasaur","Bulbasaur living on Racoon Street ext.", 90.45,17.491397, -88.200073))
        listPockemon.add(Pockemon(R.drawable.squirtle,
                "Squirtle", "Squirtle livin on Pen Road.",33.21,17.488516,-88.200459))
    }
}
