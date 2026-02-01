package com.jotlist.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for JotList.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class JotListApplication : Application()
