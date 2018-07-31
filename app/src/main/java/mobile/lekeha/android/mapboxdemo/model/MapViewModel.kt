package mobile.lekeha.android.mapboxdemo.model

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.location.Location
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority

@SuppressLint("MissingPermission")
class MapViewModel(
        val locationEngine: LocationEngine
): BaseViewModel() {

    var locationPermissionGranted: MutableLiveData<Boolean> = MutableLiveData()
    var currentLocation: MutableLiveData<Location> = MutableLiveData()

    private val locationEngineListener = object: LocationEngineListener {

        override fun onLocationChanged(location: Location?) {

            location?.let { currentLocation.value = it }
        }

        override fun onConnected() {

            locationEngine.requestLocationUpdates()
        }
    }

    fun observeLocationPermissionGranted(owner: LifecycleOwner, observer: Observer<Boolean>) {

        locationPermissionGranted.observe(owner, observer)
    }

    fun initializeLocationEngine() {

        locationEngine.priority = LocationEnginePriority.HIGH_ACCURACY
        locationEngine.activate()
        locationEngine.addLocationEngineListener(locationEngineListener)
    }

    fun getLastLocation() = locationEngine.lastLocation

    fun observeCurrentLocation(owner: LifecycleOwner, observer: Observer<Location>) {

        currentLocation.observe(owner, observer)
    }

    fun setLocationPermissionGranted(isGranted: Boolean) {

        locationPermissionGranted.value = isGranted
    }

    class Factory(
            private val locationEngine: LocationEngine
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
                return MapViewModel(locationEngine) as T
            }

            throw IllegalArgumentException("Illegal ViewModel class")
        }

    }
}