package com.example.studybuddy.ui.components

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

//@Composable
//fun FragmentContainer(
//    fragmentClass: Class<out Fragment>,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//
//    // Ensure the context is a FragmentActivity
//    if (context is FragmentActivity) {
//        AndroidView(
//            modifier = modifier,
//            factory = { ctx ->
//                FrameLayout(ctx).apply {
//                    id = View.generateViewId()
//                    val fragmentManager = context.supportFragmentManager
//                    val fragmentTransaction = fragmentManager.beginTransaction()
//
//                    // Attach the fragment if not already attached
//                    if (fragmentManager.findFragmentById(this.id) == null) {
//                        val fragmentInstance = fragmentClass.newInstance()
//                        fragmentTransaction.replace(this.id, fragmentInstance, fragmentClass.simpleName)
//                        fragmentTransaction.commitNow()
//                    }
//                }
//            }
//        )
//    } else {
//        Log.e("FragmentContainer", "Provided context is not a FragmentActivity.")
//        throw IllegalArgumentException("FragmentContainer requires a FragmentActivity context.")
//    }
//}
//
//
//// Extension function to attach a Fragment
//private fun FragmentManager.attachFragment(
//    containerId: Int,
//    fragmentClass: Class<out Fragment>,
//    lifecycleOwner: LifecycleOwner
//) {
//    // Only add the fragment if it's not already attached
//    if (findFragmentById(containerId) == null) {
//        beginTransaction()
//            .replace(containerId, fragmentClass.newInstance(), fragmentClass.simpleName)
//            .setPrimaryNavigationFragment(fragmentClass.newInstance())
//            .commitNowAllowingStateLoss()
//    }
//}


@Composable
fun <T : Fragment> FragmentContainer(
    fragmentClass: Class<T>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()
            }
        },
        modifier = modifier
    ) { frameLayout ->
        val fragment = fragmentClass.newInstance()
        val fragmentManager = (context as androidx.fragment.app.FragmentActivity).supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameLayout.id, fragment)
        transaction.commitAllowingStateLoss()
    }
}

