package mobile.lekeha.android.mapboxdemo.context

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.style.expressions.Expression.exponential
import com.mapbox.mapboxsdk.style.expressions.Expression.get
import com.mapbox.mapboxsdk.style.expressions.Expression.interpolate
import com.mapbox.mapboxsdk.style.expressions.Expression.stop
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius
import com.mapbox.mapboxsdk.style.sources.VectorSource
import kotlinx.android.synthetic.main.fragment_mapbox.*
import mobile.lekeha.android.mapboxdemo.BaseFragment
import mobile.lekeha.android.mapboxdemo.ProviderFactory
import mobile.lekeha.android.mapboxdemo.R
import mobile.lekeha.android.mapboxdemo.internal.module.ApplicationModule
import mobile.lekeha.android.mapboxdemo.model.MapViewModel

class MapBoxFragment: BaseFragment() {

    companion object {
        fun newInstance() = MapBoxFragment()
    }

    private val appProvider by lazy {
        activity?.run { ProviderFactory.model(ApplicationModule(application)) }
    }

    private lateinit var map: MapboxMap
    private lateinit var locationPlugin: LocationLayerPlugin

    override fun getLayoutId() = R.layout.fragment_mapbox

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        appCompatActivity?.also {
            ViewModelProviders.of(it)
                    .get(MapViewModel::class.java).apply {
                        observeLocationPermissionGranted(this@MapBoxFragment, Observer {
                            mapView.getMapAsync {
//                                val vectorSource = VectorSource("trees-source",
//                                        "http://api.mapbox.com/v4/lekaha.c1zfy9aw.json?access_token=" +
//                                                getString(R.string.mb_accessToken))
//                                it.addSource(vectorSource)
//                                val circleLayer = CircleLayer("trees-style", "trees-source")
//                                circleLayer.sourceLayer = "street-trees-DC-9gvg5l"
//                                circleLayer.withProperties(
//                                        circleOpacity(0.6f),
//                                        circleColor(Color.parseColor("#111")),
//                                        circleRadius(
//                                                interpolate(exponential(1.0f), get("DBH"),
//                                                        stop(0, 0f),
//                                                        stop(1, 1f),
//                                                        stop(110, 11f)
//                                                )))
//
//
//                                it.addLayer(circleLayer)

                                enableLocationPlugin(it, locationEngine)
                                setCameraPosition(getLastLocation())

                                observeCurrentLocation(this@MapBoxFragment, Observer {
                                    it?.apply(::setCameraPosition)
                                })
                            }
                        })



                        initializeLocationEngine()
                        lifecycle::addObserver
                    }
            appProvider?.let {
                mapView.setStyleUrl(it.getMapboxStyleUrl())
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationPlugin(map: MapboxMap, locationEngine: LocationEngine) {
        this.map = map
        locationPlugin = LocationLayerPlugin(mapView, map, locationEngine)
        locationPlugin.renderMode = RenderMode.COMPASS
        lifecycle.addObserver(locationPlugin)
    }


    private fun setCameraPosition(location: Location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude), 13.0))
    }
}