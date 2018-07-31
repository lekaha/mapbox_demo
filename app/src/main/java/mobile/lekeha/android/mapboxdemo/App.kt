package mobile.lekeha.android.mapboxdemo

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class App: Application() {

    val mapbox by lazy {
        val accessToken = getString(R.string.mb_accessToken)
        Mapbox.getInstance(this, accessToken)
    }

    override fun onCreate() {
        super.onCreate()
        mapbox.apply {
            // TODO: Mapbox instance created
        }

        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            plant{ debugTree() }
        }
    }
}

@PublishedApi
internal inline fun log(block: () -> Unit) = block()
inline fun plant(tree: () -> Timber.Tree) = log { Timber.plant(tree()) }
inline fun debugTree() = Timber.DebugTree()