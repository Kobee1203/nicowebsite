package org.nds.dbdroid.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import android.util.Config;

public class Logger {

    private static final String TAG = "Logger";

    private final Log log;

    private static Boolean verboseLoggable;
    private static Boolean debugLoggable;
    private static Boolean infoLoggable;
    private static Boolean warnLoggable;
    private static Boolean errorLoggable;

    private Logger(Class<?> clazz) {
        try {
            if (verboseLoggable == null) {
                verboseLoggable = android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
            }
        } catch (Throwable t) {
            verboseLoggable = false;
        }
        try {
            if (debugLoggable == null) {
                debugLoggable = android.util.Log.isLoggable(TAG, android.util.Log.DEBUG);
            }
        } catch (Throwable t) {
            debugLoggable = false;
        }
        try {
            if (infoLoggable == null) {
                infoLoggable = android.util.Log.isLoggable(TAG, android.util.Log.INFO);
            }
        } catch (Throwable t) {
            infoLoggable = false;
        }
        try {
            if (warnLoggable == null) {
                warnLoggable = android.util.Log.isLoggable(TAG, android.util.Log.WARN);
            }
        } catch (Throwable t) {
            warnLoggable = false;
        }
        try {
            if (errorLoggable == null) {
                errorLoggable = android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
            }
        } catch (Throwable t) {
            errorLoggable = false;
        }

        log = LogFactory.getLog(clazz);
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    // //////// TRACE //////////

    public void trace(String tag, String msg, Throwable tr) {
        if (Config.LOGD && verboseLoggable) {
            android.util.Log.v(tag, msg, tr);
        } else {
            log.trace(msg, tr);
        }
    }

    public void trace(String tag, String msg) {
        if (Config.LOGD && verboseLoggable) {
            android.util.Log.v(tag, msg);
        } else {
            log.trace(msg);
        }
    }

    public void trace(String msg, Throwable tr) {
        if (Config.LOGD && verboseLoggable) {
            android.util.Log.v(TAG, msg, tr);
        } else {
            log.trace(msg, tr);
        }
    }

    public void trace(String msg) {
        if (Config.LOGD && verboseLoggable) {
            android.util.Log.v(TAG, msg);
        } else {
            log.trace(msg);
        }
    }

    // //////// DEBUG //////////

    public void debug(String tag, String msg, Throwable tr) {
        if (Config.LOGD && debugLoggable) {
            android.util.Log.d(tag, msg, tr);
        } else {
            log.debug(msg, tr);
        }
    }

    public void debug(String tag, String msg) {
        if (Config.LOGD && debugLoggable) {
            android.util.Log.d(tag, msg);
        } else {
            log.debug(msg);
        }
    }

    public void debug(String msg, Throwable tr) {
        if (Config.LOGD && debugLoggable) {
            android.util.Log.d(TAG, msg, tr);
        } else {
            log.debug(msg, tr);
        }
    }

    public void debug(String msg) {
        if (Config.LOGD && debugLoggable) {
            android.util.Log.d(TAG, msg);
        } else {
            log.debug(msg);
        }
    }

    // //////// INFO //////////

    public void info(String tag, String msg, Throwable tr) {
        if (Config.LOGD && infoLoggable) {
            android.util.Log.i(tag, msg, tr);
        } else {
            log.info(msg, tr);
        }
    }

    public void info(String tag, String msg) {
        if (Config.LOGD && infoLoggable) {
            android.util.Log.i(tag, msg);
        } else {
            log.info(msg);
        }
    }

    public void info(String msg, Throwable tr) {
        if (Config.LOGD && infoLoggable) {
            android.util.Log.i(TAG, msg, tr);
        } else {
            log.info(msg, tr);
        }
    }

    public void info(String msg) {
        if (Config.LOGD && infoLoggable) {
            android.util.Log.i(TAG, msg);
        } else {
            log.info(msg);
        }
    }

    // //////// WARN //////////

    public void warn(String tag, String msg, Throwable tr) {
        if (Config.LOGD && warnLoggable) {
            android.util.Log.w(tag, msg, tr);
        } else {
            log.warn(msg, tr);
        }
    }

    public void warn(String tag, String msg) {
        if (Config.LOGD && warnLoggable) {
            android.util.Log.w(tag, msg);
        } else {
            log.warn(msg);
        }
    }

    public void warn(String msg, Throwable tr) {
        if (Config.LOGD && warnLoggable) {
            android.util.Log.w(TAG, msg, tr);
        } else {
            log.warn(msg, tr);
        }
    }

    public void warn(String msg) {
        if (Config.LOGD && warnLoggable) {
            android.util.Log.w(TAG, msg);
        } else {
            log.warn(msg);
        }
    }

    // //////// ERROR //////////

    public void error(String tag, String msg, Throwable tr) {
        if (Config.LOGD && errorLoggable) {
            android.util.Log.e(tag, msg, tr);
        } else {
            log.error(msg, tr);
        }
    }

    public void error(String tag, String msg) {
        if (Config.LOGD && errorLoggable) {
            android.util.Log.e(tag, msg);
        } else {
            log.error(msg);
        }
    }

    public void error(String msg, Throwable tr) {
        if (Config.LOGD && errorLoggable) {
            android.util.Log.e(TAG, msg, tr);
        } else {
            log.error(msg, tr);
        }
    }

    public void error(String msg) {
        if (Config.LOGD && errorLoggable) {
            android.util.Log.e(TAG, msg);
        } else {
            log.error(msg);
        }
    }
}
