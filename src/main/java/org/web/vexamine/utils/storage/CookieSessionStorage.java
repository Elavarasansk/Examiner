package org.web.vexamine.utils.storage;

import org.springframework.util.ObjectUtils;

/**
 * The Class CookieSessionStorage.
 */
public final class CookieSessionStorage {

	/** The Constant THREAD_LOCAL. */
	private static final ThreadLocal<CookieSession> THREAD_LOCAL = new ThreadLocal();

	/**
	 * Instantiates a new cookie session storage.
	 */
	private CookieSessionStorage() {

	}

	/**
	 * Sets the.
	 *
	 * @param context the context
	 */
	public static void set(CookieSession context) {
		THREAD_LOCAL.set(context);
	}

	/**
	 * Unset.
	 */
	public static void unset() {
		THREAD_LOCAL.remove();
	}

	/**
	 * Gets the.
	 *
	 * @return the cookie session
	 */
	public static CookieSession get() {
		if(ObjectUtils.isEmpty(THREAD_LOCAL.get())) {
			return  null;
		}

		return new CookieSession(THREAD_LOCAL.get());
	}

	/**
	 * Gets the current user name.
	 *
	 * @return the current user name
	 */
	public static String getCurrentUserName() {
		return get().getUserName();
	}
}
