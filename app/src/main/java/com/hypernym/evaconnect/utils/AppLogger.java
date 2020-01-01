/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.hypernym.evaconnect.utils;


import android.util.Log;

/**
 * Created by hina saeed on 17/12/19.
 */

public final class AppLogger {
    private static final String TAG = AppLogger.class.getName();
    private static AppLogger logger;

    private AppLogger() {

    }

    public static AppLogger get() {
        if (logger == null) {
            logger = _get();
        }
        return logger;
    }

    private static synchronized AppLogger _get() {
        if (logger == null) {
            logger = new AppLogger();
        }
        return logger;
    }

    public void logException(Throwable t) {
        if (t != null) {
            Log.e(TAG, t.toString(), t);
            try {
                // Crashlytics.logException(t);
            } catch (Throwable _t) {
                Log.e(TAG, _t.toString(), t);
            }
        }
    }


}
