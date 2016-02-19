package com.platform.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {

	public static Matcher matcher(final String regex, final String value) {
		final Pattern _pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		return _pattern.matcher(value);
	}

	public static boolean isExactlyMatches(final String regex, final String value) {
		final Pattern _pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		return _pattern.matcher(value).matches();
	}

	public static boolean isMatches(final String regex, final String value) {
		final Pattern _pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher _matcher = _pattern.matcher(value);
		return _matcher.find();
	}

	public static boolean isMatches(final String[] regexs, final String value) {
		for (final String regex : regexs) {
			final Pattern _pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			final Matcher _matcher = _pattern.matcher(value);
			if (_matcher.find()) {
				return true;
			}
		}
		return false;
	}

	public static void main(final String args[]) {
		
		Matcher matcher = RegexpUtils.matcher("(\\d{4}_\\d{2}_\\d{2}_\\d{2}_\\d{2}_\\d{2})\\$\\$(\\d{4}-\\d{2}-\\d{2})_(\\d{4}-\\d{2}-\\d{2}).csv", "2012_04_18_17_05_35$$2012-04-18_2012-04-18.csv");
		System.out.println(matcher.find());
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));
	}
}
