

package com.cbsb.backup.exporter;

import android.provider.Browser;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class BookmarkExporter extends SimpleExporter {
	public static final int ID = 1;
	
	public static final int NAMEID = R.string.bookmarks;

	public BookmarkExporter(ExportTask exportTask) {
		super(Strings.TAG_BOOKMARK, Browser.BOOKMARKS_URI, Browser.BookmarkColumns.BOOKMARK+"=1", exportTask);
	}

	@Override
	public String getContentName() {
		return Strings.BOOKMARKS;
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getTranslatedContentName() {
		return NAMEID;
	}
	
}
