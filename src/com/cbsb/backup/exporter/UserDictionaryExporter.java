

package com.cbsb.backup.exporter;

import android.provider.UserDictionary;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class UserDictionaryExporter extends SimpleExporter {
	public static final int ID = 4;
	
	public static final int NAMEID = R.string.userdictionary;

	public UserDictionaryExporter(ExportTask exportTask) {
		super(Strings.TAG_WORD, UserDictionary.Words.CONTENT_URI, exportTask);
	}

	@Override
	public String getContentName() {
		return Strings.USERDICTIONARY;
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
