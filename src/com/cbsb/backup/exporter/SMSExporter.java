

package com.cbsb.backup.exporter;

import java.io.IOException;
import java.io.Writer;

import android.database.Cursor;
import android.text.TextUtils;
import com.cbsb.backup.BackupActivity;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class SMSExporter extends SimpleExporter {
	public static final int ID = 3;
	
	public static final int NAMEID = R.string.smsmessages;
	
	private int bodyPosition;

	public SMSExporter(ExportTask exportTask) {
		super(Strings.TAG_MESSAGE, Strings.SMS_FIELDS, BackupActivity.SMS_URI, true, null, "date", exportTask, Strings.SMS_FIELDS_OPTIONAL);
		bodyPosition = -1;
	}
	
	@Override
	public void addText(Cursor cursor, Writer writer) throws IOException {
		if (bodyPosition == -1) {
			bodyPosition = cursor.getColumnIndex(Strings.BODY);
		}
		
		String body = cursor.getString(bodyPosition);
		
		if (body != null) {
			writer.write(TextUtils.htmlEncode(body));
		}
	}

	@Override
	public boolean checkFieldNames(String[] availableFieldNames, String[] neededFieldNames) {
		return super.checkFieldNames(availableFieldNames, neededFieldNames) 
		       && Strings.indexOf(availableFieldNames, Strings.BODY) > -1;
	}

	@Override
	public String getContentName() {
		return Strings.MESSAGES;
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
