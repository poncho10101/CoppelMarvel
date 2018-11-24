package com.coppel.alfonsosotelo.coppelmarvel.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.coppel.alfonsosotelo.coppelmarvel.exceptions.ApiRequestException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.security.MessageDigest
import com.coppel.alfonsosotelo.coppelmarvel.R
import io.reactivex.Completable
import java.text.DateFormat
import java.util.*
import androidx.lifecycle.Observer as LifeCycleObserver

/**
 * Convert a string to md5
 * @sample "1542919528000f78932ddf35c24cda8c51758d1e21e7faf83f3e26bdaf3ecef2575e517198a87c62888af".toMd5Hash()
 *          returns "8c0dd9822443884b6575129b2e26a603"
 */
fun String.toMd5Hash(): String {
    return try {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        val result = StringBuilder()
        bytes.forEach {
            result.append(String.format("%02X", it))
        }
        result.toString().toLowerCase()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Get error custom message from error type
 * add new error messages here
 */
fun Throwable.getErrorMessage(ctx: Context): String {
    var message = this.message!!

    when {
        this is ApiRequestException -> {
            //throwable message
        }
        this is HttpException -> when(this.code()/100){
            3 -> message = ctx.getString(R.string.error_300_redirects_message, this.code())
            4 -> {
                message = when(this.code()) {
                    400 -> ctx.getString(R.string.error_bad_request_message)
                    401 -> ctx.getString(R.string.error_unauthorized_message)
                    403 -> ctx.getString(R.string.error_forbidden_message)
                    404 -> ctx.getString(R.string.error_not_found_message)
                    405 -> ctx.getString(R.string.error_not_allowed_message)
                    406 -> ctx.getString(R.string.error_not_acceptable_message)
                    else -> ctx.getString(R.string.error_400_client_message, this.code())
                }
            }
            5 -> message = ctx.getString(R.string.error_500_server_message, this.code())
        }
        this is ConnectException -> message = ctx.getString(R.string.error_connection_message)
    }

    return message
}


/**
 * Format a Date to String with phone user Locale
 * @sample Date(1543027045).formatWithLocale(ctx)
 * @return "2018/11/23 07:36 pm"
 * @param ctx context to get phone Locale
 * @param isDate if is true return the date
 * @param isTime if is true return the time
 * @param dateFormatStyle return the date format style pattern, use DateFormat constants only
 * @param timeFormatStyle return the date format style pattern, use DateFormat constants only
 */
fun Date.formatWithLocale(ctx: Context, isDate: Boolean = true, isTime: Boolean = true, dateFormatStyle: Int = DateFormat.SHORT, timeFormatStyle: Int = DateFormat.SHORT): String {
    val locale = ctx.getLocale()
    val dateFormat: DateFormat
    dateFormat = if (isDate && isTime) {
        DateFormat.getDateTimeInstance(dateFormatStyle, timeFormatStyle, locale)
    } else if (isDate) {
        DateFormat.getDateInstance(dateFormatStyle, locale)
    } else if (isTime) {
        DateFormat.getTimeInstance(timeFormatStyle, locale)
    } else {
        throw RuntimeException("At least date or time needs to be true")
    }

    return dateFormat.format(this)
}

/**
 * Format a String to Date with phone user Locale
 * @sample "2018/11/23 07:36 pm".unformatDateWithLocale(ctx)
 * @return Date(1543027045)
 * @param ctx context to get phone Locale
 * @param isDate if is true the string need to have the date
 * @param isTime if is true the string need to have the time
 * @param dateFormatStyle read from string the date format style pattern, use DateFormat constants only
 * @param timeFormatStyle read from string the date format style pattern, use DateFormat constants only
 */
fun String.unformatDateWithLocale(ctx: Context, isDate: Boolean = true, isTime: Boolean = true, dateFormatStyle: Int = DateFormat.SHORT, timeFormatStyle: Int = DateFormat.SHORT): Date {
    val locale = ctx.getLocale()
    val dateFormat = if (isDate && isTime) {
        DateFormat.getDateTimeInstance(dateFormatStyle, timeFormatStyle, locale)
    } else if (isDate) {
        DateFormat.getDateInstance(dateFormatStyle, locale)
    } else if (isTime) {
        DateFormat.getTimeInstance(timeFormatStyle, locale)
    } else {
        throw RuntimeException("At least date or time needs to be true")
    }

    return dateFormat.parse(this)
}

/**
 * Obtain Locale from the Phone
 */
fun Context.getLocale(): Locale {
    return ConfigurationCompat.getLocales(this.resources.configuration)[0]
}



/**
 * Activity and Context Extensions
 */

/**
 * Start an activity from a fragment
 * @sample this@Fragment.startActivity<Sample>()
 * @param extras if you want to put extras bundle
 */
inline fun <reified T: Activity> Fragment.startActivity(extras: Bundle? = null) {
    startActivity(Intent(context, T::class.java).apply {
        extras?.let{ putExtras(it) }
    })
}

/**
 * Start an activity from a context
 * @sample this@Activity.startActivity<Sample>()
 * @param extras if you want to put extras bundle
 */
inline fun <reified T: Activity> Context.startActivity(extras: Bundle? = null) {
    startActivity(Intent(this, T::class.java).apply {
        extras?.let{ putExtras(it) }
    })
}



/**
 * Views Extensions
 */

/**
 * Add a snackbar to a View
 *
 * @param actionListener lambda to receive actionText click
 */
fun View.setSnackbar(message: CharSequence, @StringRes actionText: Int? = null, duration: Int = Snackbar.LENGTH_INDEFINITE, actionListener: ((View) -> Unit)? = null): Snackbar {
    return Snackbar.make(
        this,
        message,
        duration.takeIf { actionText != null || duration != Snackbar.LENGTH_INDEFINITE } ?: Snackbar.LENGTH_LONG
    ).apply {
        actionText?.let { setAction(it, actionListener) }

        show()
    }
}

/**
 * Add an error message to the parent (TextInputLayout) if exists else set error to itself
 */
fun TextInputEditText.setLayoutError(message: CharSequence?){
    if (parent?.parent is TextInputLayout) {
        (parent.parent as TextInputLayout).error = message
    } else {
        error = message
    }
}




/**
 * RX Extensions
 */

/**
 * Add disposable instance to a CompositeDisposable
 */
fun Disposable.addToDisposables(compositeDisposables: CompositeDisposable) = compositeDisposables.add(this)

/**
 * Schedulers to a reactiveX Single instance
 */
fun <T> Single<T>.setDefaultSchedulers(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * Schedulers to a reactiveX Completable instance
 */
fun Completable.setDefaultSchedulers(): Completable {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * Schedulers to a reactiveX Observable instance
 */
fun <T> Observable<T>.setDefaultSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}





/**
 * ViewModels Extensions
 */

/**
 * Add default value to a MutableLiveData
 * @sample MutableLiveData<Int>().default(1)
 */
fun <T> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

/**
 * Get viewmodel without constructor parameterse from an Activity
 * @sample this@Activity.withViewModel<ActivityViewModel>()
 */
inline fun <reified T: ViewModel> FragmentActivity.getViewModel()
        = ViewModelProviders.of(this)[T::class.java]

/**
 * Get viewmodel without constructor parameterse from an Activity
 * with a body
 * @param lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel<ActivityViewModel> { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> FragmentActivity.withViewModel(body: T.() -> Unit): T {
    val vm = getViewModel<T>()
    vm.body()
    return vm
}

/**
 * Get viewmodel with constructor parameterse from an Activity
 * @sample this@Activity.withViewModel({ActivityViewModel(param)})
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    @Suppress("UNCHECKED_CAST")
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

/**
 * Get viewmodel with constructor parameterse from an Activity
 * with a body
 * @param lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel({ActivityViewModel(param)}) { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> FragmentActivity.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit
): T {
    val vm = getViewModel(factory)
    vm.body()
    return vm
}

/**
 * Get viewmodel without constructor parameterse from a Fragment
 * @sample this@Activity.withViewModel<ActivityViewModel>()
 */
inline fun <reified T: ViewModel> Fragment.getViewModel()
        = ViewModelProviders.of(this)[T::class.java]

/**
 * Get viewmodel without constructor parameterse from an Activity
 * with a body
 * @param lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel<ActivityViewModel> { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> Fragment.withViewModel(body: T.() -> Unit): T {
    val vm = getViewModel<T>()
    vm.body()
    return vm
}

/**
 * Get viewmodel with constructor parameterse from a Fragment
 * @sample this@Activity.withViewModel({ActivityViewModel(param)})
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    @Suppress("UNCHECKED_CAST")
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

/**
 * Get viewmodel with constructor parameterse from a Fragment
 * with a body
 * @param lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel({ActivityViewModel(param)}) { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> Fragment.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit
): T {
    val vm = getViewModel(factory)
    vm.body()
    return vm
}

/**
 * Observe a LiveData from a LifeCycleOwner (Activity, Fragment)
 * @param liveData the live data to observe
 * @param body lambda called when the livedata changes
 * @sample this@Activity.observe(viewModel.data) { data: LiveData ->
 *      doSomething(data)
 * }
 */
fun <T: Any, L: LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit)
        = liveData.observe(this, LifeCycleObserver(body))

/**
 * Add one element to a list in MutabeLiveData
 */
fun <T> MutableLiveData<List<T>>.addElement(element: T) {
    addElements(listOf(element))
}

/**
 * Add a list of elements to a list in MutabeLiveData
 */
fun <T> MutableLiveData<List<T>>.addElements(element: List<T>) {
    this.postValue(
        (this.value?.toMutableList() ?: mutableListOf()).apply {
            addAll(element)
        }
    )
}