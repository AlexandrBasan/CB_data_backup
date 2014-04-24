

package com.cbsb.backup.parser;

import android.content.Context;
import android.provider.CallLog;
import com.cbsb.backup.BackupActivity;
import com.cbsb.backup.Strings;

public class CallLogParser extends SimpleParser {
	public CallLogParser(Context context, ImportTask importTask) {
		super(context, Strings.TAG_CALL, new String[] {CallLog.Calls.CACHED_NAME,
				CallLog.Calls.CACHED_NUMBER_LABEL,
				CallLog.Calls.CACHED_NUMBER_TYPE,
				CallLog.Calls.DATE,
				CallLog.Calls.DURATION,
				CallLog.Calls.NEW,
				CallLog.Calls.NUMBER,
				CallLog.Calls.TYPE
		}, CallLog.Calls.CONTENT_URI, importTask, null, BackupActivity.ICS ? new String[] {CallLog.Calls.TYPE, Strings.FOUR} : null);
	}

}
