package mobile.lekeha.android.mapboxdemo.internal.module

import android.content.Context
import android.support.v4.app.FragmentActivity
import mobile.lekeha.android.mapboxdemo.Navigator

class NavigationModule(private val activity: FragmentActivity) {

    private val navigator: Navigator = Navigator(activity)

    internal fun provideContext(): Context {
        return activity
    }

    internal fun provideNavigator() = navigator
}