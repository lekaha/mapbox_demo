package mobile.lekeha.android.mapboxdemo

import mobile.lekeha.android.mapboxdemo.internal.module.ApplicationModule
import mobile.lekeha.android.mapboxdemo.internal.module.NavigationModule

object ProviderFactory {

    private var providers = mutableListOf<Provider>()

    internal fun model(applicationModule: ApplicationModule) =
            get(ProviderApplicationModule::class.java) ?:
            ProviderApplicationModule(applicationModule).also {
                providers.add(it)
            }

    internal fun model(navigationModule: NavigationModule) =
            get(ProviderNavigationModule::class.java) ?:
            ProviderNavigationModule(navigationModule).also {
                providers.add(it)
            }

    fun <T: Provider> get(providerClass: Class<T>): T? {
        providers.forEach {
            if (providerClass.isAssignableFrom(it.javaClass)) {
                return it as T
            }
        }

        return null
    }

    interface Provider

    class ProviderApplicationModule(private val applicationModule: ApplicationModule): Provider {

        fun getFusedLocationProviderClient() =
                applicationModule.provideFusedLocationProviderClient(
                        applicationModule.provideContext()
                )
        fun getGeoDataClient() =
                applicationModule.provideGeoDataClient(
                        applicationModule.provideContext()
                )
        fun getGeocoder() =
                applicationModule.provideGeocoder(
                        applicationModule.provideContext()
                )

        fun getMapboxStyleUrl() =
                applicationModule.provideMapboxStyleUrl(
                        applicationModule.provideContext()
                )

        fun getMapbox() = applicationModule.provideMapbox()

        fun getLocationEngine() = applicationModule.provideLocationEngine(
                applicationModule.provideContext()
        )
    }

    class ProviderNavigationModule(private val navigationModule: NavigationModule): Provider {

        fun getNavigator() = navigationModule.provideNavigator()
    }

}