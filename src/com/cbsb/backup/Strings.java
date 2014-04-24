

package com.cbsb.backup;

import com.cbsb.backup.R;

public final class Strings {
	public static final String BODY = "body";
	
	public static final String TAG_MESSAGE = "message";
	
	public static final String TAG_CALL = "call";
	
	public static final String FILE_EXTENSION = ".xml";
	
	public static final String FILE_SUFFIX = "_export_";
	
	public static final String CALLLOGS = "calllogs";
	
	public static final String MESSAGES = "messages";
	
	public static final String PLAYLISTS = "playlists";
	
	public static final String SETTINGS = "settings";
	
	public static final String COUNT = "count=\"";
	
	public static final String DATE = "date=\"";

	public static final String TAG_BOOKMARK = "bookmark";
	
	public static final String BOOKMARKS = "bookmarks";
	
	public static final String TAG_WORD = "word";
	
	public static final String TAG_SETTING = "setting";
	
	public static final String USERDICTIONARY = "userdictionary";
	
	public static final String EXPORTTYPE = "exporttype";
	
	public static final String PREFERENCE_LICENSEACCEPTED = "license.accepted";
	
	public static final String PREFERENCE_CYCLECOUNT = "cyclecount";
	
	public static final String PREFERENCE_STORAGELOCATION = "storage.location";
	
	public static final String THREENEWLINES = "\n\n\n";
	
	public static final String EMPTY = "";
	
	public static final String COMMA = ", ";
	
	public static final String TAG_PLAYLIST = "playlist";
	
	public static final String TAG_FILE = "file";
	
	public static final String DB_ARG = "=?";
	
	public static final String AND = " and ";
	
	public static final String EXTERNAL = "external";
	
	public static final String ZERO = "0";
	
	public static final String NOT_FOUR = "!= 4";
	
	public static final String FOUR = "4";
	
	public static final String[] SMS_FIELDS = new String[] {
		"date",
		"address",
		"type",
		"read",
		"status",
		"service_center"
	};

	public static final String[] SMS_FIELDS_OPTIONAL = new String[] {
		"locked",
		"date_sent",
		"seen",
		"error_code"
	};

	public static final String UTF8 = "utf-8";

	public static int indexOf(String[] array, String string) {
    	for (int n = 0, i = array != null ? array.length : 0; n < i; n++) {
    		if (array[n] != null && array[n].equals(string)) {
    			return n;
    		}
    	}
    	return -1;
    }
	
}
