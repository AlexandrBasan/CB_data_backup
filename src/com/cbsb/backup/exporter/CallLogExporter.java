

package com.cbsb.backup.exporter;

import android.provider.CallLog;
import com.cbsb.backup.BackupActivity;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class CallLogExporter extends SimpleExporter {
	public static final int ID = 2;
	
	public static final int NAMEID = R.string.calllogs;

	public CallLogExporter(ExportTask exportTask) {
		super(Strings.TAG_CALL, CallLog.Calls.CONTENT_URI, BackupActivity.ICS ? CallLog.Calls.TYPE+Strings.NOT_FOUR : null, exportTask);
	}

	@Override
	public String getContentName() {
		return Strings.CALLLOGS;
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
