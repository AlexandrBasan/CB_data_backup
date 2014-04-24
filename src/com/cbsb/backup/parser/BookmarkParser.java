

package com.cbsb.backup.parser;

import android.content.Context;
import android.provider.Browser;
import com.cbsb.backup.Strings;

public class BookmarkParser extends SimpleParser {

	public BookmarkParser(Context context, ImportTask importTask) {
		super(context, Strings.TAG_BOOKMARK, new String[] {Browser.BookmarkColumns.BOOKMARK,
				Browser.BookmarkColumns.CREATED,
				Browser.BookmarkColumns.DATE,
				Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL,
				Browser.BookmarkColumns.VISITS
		}, Browser.BOOKMARKS_URI, importTask, new String[] {Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL, 
				Browser.BookmarkColumns.BOOKMARK});
	}

}
