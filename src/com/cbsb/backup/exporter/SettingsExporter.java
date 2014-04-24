

package com.cbsb.backup.exporter;

import android.provider.Settings;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class SettingsExporter extends SimpleExporter {
	public static final int ID = 6;
	
	public static final int NAMEID = R.string.settings;
	
	public SettingsExporter(ExportTask exportTask) {
		super(Strings.TAG_SETTING, Settings.System.CONTENT_URI, exportTask);
	}

	@Override
	public String getContentName() {
		return Strings.SETTINGS;
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
