package org.nds.dbdroid.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Logger {

	private static Boolean androidLoggable = null;
	private Log log;

	private Logger(Class<?> clazz) {
		try {
			if (!android.util.Log.isLoggable("Logger", android.util.Log.ASSERT)) {
				androidLoggable = true;
			} else {
				androidLoggable = false;
			}
		} catch (Throwable tr) {
			androidLoggable = false;
		}
		if (androidLoggable != null && !androidLoggable) {
			log = LogFactory.getLog(clazz);
		}
	}

	public static Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}

	// //////// TRACE //////////

	public void trace(String tag, String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.v(tag, msg, tr);
		} else {
			log.trace(msg, tr);
		}
	}

	public void trace(String tag, String msg) {
		if (androidLoggable) {
			android.util.Log.v(tag, msg);
		} else {
			log.trace(msg);
		}
	}

	public void trace(String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.v("Logger", msg, tr);
		} else {
			log.trace(msg, tr);
		}
	}

	public void trace(String msg) {
		if (androidLoggable) {
			android.util.Log.v("Logger", msg);
		} else {
			log.trace(msg);
		}
	}

	// //////// DEBUG //////////

	public void debug(String tag, String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.d(tag, msg, tr);
		} else {
			log.debug(msg, tr);
		}
	}

	public void debug(String tag, String msg) {
		if (androidLoggable) {
			android.util.Log.d(tag, msg);
		} else {
			log.debug(msg);
		}
	}

	public void debug(String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.d("Logger", msg, tr);
		} else {
			log.debug(msg, tr);
		}
	}

	public void debug(String msg) {
		if (androidLoggable) {
			android.util.Log.d("Logger", msg);
		} else {
			log.debug(msg);
		}
	}

	// //////// INFO //////////

	public void info(String tag, String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.i(tag, msg, tr);
		} else {
			log.info(msg, tr);
		}
	}

	public void info(String tag, String msg) {
		if (androidLoggable) {
			android.util.Log.i(tag, msg);
		} else {
			log.info(msg);
		}
	}

	public void info(String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.i("Logger", msg, tr);
		} else {
			log.info(msg, tr);
		}
	}

	public void info(String msg) {
		if (androidLoggable) {
			android.util.Log.i("Logger", msg);
		} else {
			log.info(msg);
		}
	}

	// //////// WARN //////////

	public void warn(String tag, String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.w(tag, msg, tr);
		} else {
			log.warn(msg, tr);
		}
	}

	public void warn(String tag, String msg) {
		if (androidLoggable) {
			android.util.Log.w(tag, msg);
		} else {
			log.warn(msg);
		}
	}

	public void warn(String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.w("Logger", msg, tr);
		} else {
			log.warn(msg, tr);
		}
	}

	public void warn(String msg) {
		if (androidLoggable) {
			android.util.Log.w("Logger", msg);
		} else {
			log.warn(msg);
		}
	}

	// //////// ERROR //////////

	public void error(String tag, String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.e(tag, msg, tr);
		} else {
			log.error(msg, tr);
		}
	}

	public void error(String tag, String msg) {
		if (androidLoggable) {
			android.util.Log.e(tag, msg);
		} else {
			log.error(msg);
		}
	}

	public void error(String msg, Throwable tr) {
		if (androidLoggable) {
			android.util.Log.e("Logger", msg, tr);
		} else {
			log.error(msg, tr);
		}
	}

	public void error(String msg) {
		if (androidLoggable) {
			android.util.Log.e("Logger", msg);
		} else {
			log.error(msg);
		}
	}
}
