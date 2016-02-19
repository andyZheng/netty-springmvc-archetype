/**
 *
 */
package com.platform.common.web;

public class Result extends RuntimeException {

	private static final long serialVersionUID = -372469097250506251L;
	public static final int TYPE_SUCCESS = 1;
	public static final int TYPE_FAIL = 0;
	public int type;

	public static void success(final String message) {
		throw new Result(TYPE_SUCCESS, message);
	}

	public static void fail(final String message) {
		throw new Result(TYPE_FAIL, message);
	}
	
	public static void fail(final String message, final Exception e) {
		throw new Result(TYPE_FAIL, message, e);
	}

	private Result(final int type, final String message) {
		super(message);
		this.type = type;
	}

	private Result(final int type, final String message, final Exception e) {
		super(message, e);
		this.type = type;
	}
}
