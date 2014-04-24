
package com.cbsb.backup.parser;

import java.util.Vector;

import org.xml.sax.SAXException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.cbsb.backup.BackupActivity;
import com.cbsb.backup.Strings;

public class MessageParser extends SimpleParser {
	private static final Uri SMSCONVERSATIONSUPDATE_URI = Uri.parse("content://sms/conversations/-1");
	
	private StringBuilder messageStringBuilder;
	
	public MessageParser(Context context, ImportTask importTask) {
		super(context, Strings.TAG_MESSAGE, determineFields(context), BackupActivity.SMS_URI, importTask, Strings.SMS_FIELDS);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (tagEntered) {
			messageStringBuilder.append(ch, start, length);
		}
	}
	
	@Override
	public void startMainElement() {
		messageStringBuilder = new StringBuilder();
	}
	
	@Override
	public void addExtraContentValues(ContentValues contentValues) {
		contentValues.put(Strings.BODY, messageStringBuilder.toString());
	}

	@Override
	public void endDocument() throws SAXException {
		context.getContentResolver().delete(SMSCONVERSATIONSUPDATE_URI, null, null);
	}
	
	private static String[] determineFields(Context context) {
		Cursor cursor = context.getContentResolver().query(SMSCONVERSATIONSUPDATE_URI, null, null, null, BaseColumns._ID+" DESC LIMIT 0");
		
		String[] availableFields = cursor.getColumnNames();
		
		cursor.close();
		
		Vector<String> fields = new Vector<String>();
		
		for (String field : Strings.SMS_FIELDS) {
			if (Strings.indexOf(Strings.SMS_FIELDS, field) == -1) {
				throw new IllegalArgumentException();
			} else {
				fields.add(field);
			}
		}
		for (String field : Strings.SMS_FIELDS) {
			if (Strings.indexOf(Strings.SMS_FIELDS_OPTIONAL, field) > -1) {
				fields.add(field);
			}
		}
		
		return fields.toArray(availableFields);
	}
	
}
