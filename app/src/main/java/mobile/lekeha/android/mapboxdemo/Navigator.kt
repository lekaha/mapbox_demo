package mobile.lekeha.android.mapboxdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import ext.start
import ext.startForResult
import ext.startWithUri
import ext.transact

/**
 * Navigator is responsible for navigating as to a Fragment or a Activity
 * and dealing with transition behaviors e.g., transition animation.
 */
class Navigator constructor(activityContext: Context) {

    /**
     * navigate to a Fragment with [AppCompatActivity] and the transactions
     * in [FragmentTransaction]
     */
    fun navigateToFragment(
            context: AppCompatActivity,
            transactions: FragmentTransaction.() -> Unit
    ) {
        context.transact {
            transactions()
        }
    }
}

inline fun <reified T : AppCompatActivity> Navigator.navigateToActivity(
        context: AppCompatActivity,
        noinline intent: Intent.() -> Unit = {}
) = context.start<T> {
    intent()
}

inline fun <reified T : AppCompatActivity> Navigator.navigateToActivityWithResult(
        context: AppCompatActivity,
        resultCode: Int = -1,
        options: Bundle = Bundle(),
        noinline intent: Intent.() -> Unit = {}
) = context.startForResult<T>(resultCode, options) {
    intent()
}

inline fun <reified T : AppCompatActivity> Navigator.navigateToActivity(
        fragment: Fragment,
        noinline intent: Intent.() -> Unit = {}
) = (fragment.activity)?.let {
    (fragment.activity as? AppCompatActivity)?.start<T>(intent)
}

inline fun <reified T : AppCompatActivity> Navigator.navigateToUri(
        fragment: Fragment,
        action: String,
        uri: Uri,
        packageName: String
) = (fragment.activity)?.let {
    (fragment.activity as? AppCompatActivity)?.startWithUri<T>(action, uri) {
        setPackage(packageName)
    }
}