package mobile.lekeha.android.mapboxdemo.internal.module

import android.app.Application
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.mapboxsdk.Mapbox
import mobile.lekeha.android.mapboxdemo.R
import java.util.*

/**
 * Module used to provide dependencies at an application-level.
 */
open class ApplicationModule(private val application: Application) {

    private val mapbox: Mapbox

    init {
        val accessToken = application.getString(R.string.mb_accessToken)
        mapbox = Mapbox.getInstance(application, accessToken)
    }

    internal fun provideContext(): Context {
        return application
    }

//    internal fun providePreferencesHelper(context: Context) =
//            PreferencesHelper(context)
//
//    internal fun provideDbOpenHelper(context: Context) = DbOpenHelper(context)
//
//    internal fun provideJobExecutor() = JobExecutor()
//
//    internal fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor
//
//    internal fun provideUiThread() = UiThread()
//
//    internal fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = uiThread

    internal fun provideMapboxStyleUrl(context: Context) = context.getString(R.string.mb_styleUrl)

    internal fun provideMapbox() = mapbox

    internal fun provideFusedLocationProviderClient(context: Context) =
            LocationServices.getFusedLocationProviderClient(context)

    internal fun provideGeoDataClient(context: Context) =
            Places.getGeoDataClient(context, null)

    internal fun provideGeocoder(context: Context) =
            Geocoder(context, Locale.getDefault())

    internal fun provideLocationEngine(context: Context) =
            LocationEngineProvider(context).obtainBestLocationEngineAvailable()
}
