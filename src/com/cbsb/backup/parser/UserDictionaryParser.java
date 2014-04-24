

package com.cbsb.backup.parser;

import android.content.Context;
import android.provider.UserDictionary;
import com.cbsb.backup.Strings;

public class UserDictionaryParser extends SimpleParser {

	public UserDictionaryParser(Context context, ImportTask importTask) {
		super(context, Strings.TAG_WORD, new String[] {UserDictionary.Words.APP_ID,
				UserDictionary.Words.CONTENT_ITEM_TYPE,
				UserDictionary.Words.CONTENT_TYPE,
				UserDictionary.Words.FREQUENCY,
				UserDictionary.Words.LOCALE,
				UserDictionary.Words.WORD,
		}, UserDictionary.Words.CONTENT_URI, importTask);
	}

}
