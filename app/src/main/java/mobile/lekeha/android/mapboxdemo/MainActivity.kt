package mobile.lekeha.android.mapboxdemo

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import mobile.lekeha.android.mapboxdemo.context.MapBoxFragment
import mobile.lekeha.android.mapboxdemo.databinding.ActivityMainBinding
import mobile.lekeha.android.mapboxdemo.internal.module.ApplicationModule
import mobile.lekeha.android.mapboxdemo.internal.module.NavigationModule
import mobile.lekeha.android.mapboxdemo.model.MapViewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private val appProvider by lazy {
        ProviderFactory.model(ApplicationModule(this.application))
    }

    private val navigationProvider by lazy {
        ProviderFactory.model(NavigationModule(this))
    }

    private val mapViewModelFactory: MapViewModel.Factory by lazy {
        MapViewModel.Factory(
                appProvider.getLocationEngine()
        )
    }

    private lateinit var viewModel: MapViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding =
                DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)
        activityMainBinding.setLifecycleOwner(this)

        viewModel = ViewModelProviders.of(this, mapViewModelFactory)
                .get(MapViewModel::class.java)

        showMapWithPermissionCheck(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }


    @NeedsPermission(
            value = [
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ])
    fun showMap(savedInstanceState: Bundle?) {
        navigationProvider.getNavigator().navigateToFragment(this) {
            replace(R.id.content, MapBoxFragment.newInstance())
        }
        viewModel.setLocationPermissionGranted(true)
    }


    @OnPermissionDenied(
            value = [
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ])
    fun showMapDenied() {
        if (!activityMainBinding.viewStubPermissionDenied.isInflated) {
            val view = activityMainBinding.viewStubPermissionDenied.viewStub?.inflate()
        }
    }
}
