

package com.cbsb.backup.parser;

import android.content.Context;
import android.provider.Settings;
import com.cbsb.backup.Strings;

public class SettingsParser extends SimpleParser {

	public SettingsParser(Context context, ImportTask importTask) {
		super(context, Strings.TAG_SETTING, new String[] {
				Settings.System.NAME,
				Settings.System.VALUE
		}, Settings.System.CONTENT_URI, importTask, new String[] {
				Settings.System.NAME}, true);
	}

}
