

package com.cbsb.backup.parser;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.cbsb.backup.BackupTask;
import com.cbsb.backup.Strings;

public abstract class SimpleParser extends Parser {
	protected String[] values;
	
	protected boolean tagEntered;
	
	private String tag;
	
	private String[] fields;
	
	private Uri contentUri;
	
	private String[] existanceFields;
	
	private int[] existancePositions;
	
	private int existanceLength;
	
	private String[] existanceValues;
	
	private int position;
	
	/** Update the value if exists; only use it with existanceFields */
	private boolean updateOnExist;
	
	/* vectorized nx2 matrix - key-value based */
	private String[] ignoreValues;
	
	public SimpleParser(Context context, String tag, String[] fields, Uri contentUri, ImportTask importTask, String[] existanceFields, boolean updateOnExist, String[] ignoreValues) {
		super(context, importTask);
		this.tag = tag;
		this.fields = fields;
		this.importTask = importTask;
		values = new String[fields.length];
		tagEntered = false;
		this.contentUri = contentUri;
		this.importTask = importTask;
		this.existanceFields = existanceFields;
		if (existanceFields != null) {
			existanceLength = existanceFields.length;
			
			existanceValues = new String[existanceLength];
			existancePositions = new int[existanceLength];
			
			for (int n = 0; n < existanceLength; n++) {
				existancePositions[n] = Strings.indexOf(fields, existanceFields[n]);
			}
			this.updateOnExist = updateOnExist;
		} else {
			this.updateOnExist = false;
		}
		canceled = false;
		position = 0;
		if (ignoreValues != null && ignoreValues.length % 2 != 0) {
			throw new IllegalArgumentException();
		}
		this.ignoreValues = ignoreValues;
	}
	
	public SimpleParser(Context context, String tag, String[] fields, Uri contentUri, ImportTask importTask, String[] existanceFields, boolean updateOnExist) {
		this(context, tag, fields, contentUri, importTask, existanceFields, updateOnExist, null);
	}
	
	public SimpleParser(Context context, String tag, String[] fields, Uri contentUri, ImportTask importTask, String[] existanceFields, String[] ignoreValues) {
		this(context, tag, fields, contentUri, importTask, existanceFields, false, ignoreValues);
	}
	
	public SimpleParser(Context context, String tag, String[] fields, Uri contentUri, ImportTask importTask, String[] existanceFields) {
		this(context, tag, fields, contentUri, importTask, existanceFields, null);
	}
	
	public SimpleParser(Context context, String tag, String[] fields, Uri contentUri, ImportTask importTask) {
		this(context, tag, fields, contentUri, importTask, null);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (!canceled && !tagEntered) {
			if (tag.equals(localName)) {
				tagEntered = true;
				for (int n = 0, i = values.length; n < i; n++) {
					values[n] = attributes.getValue(Strings.EMPTY, fields[n]);
				}
				startMainElement();
			} else {
				String count = attributes.getValue(Strings.EMPTY, COUNT);
				
				if (count != null) {
					try {
						importTask.progress(BackupTask.MESSAGE_COUNT, Integer.parseInt(count));
					} catch (Exception e) {
						
					}
				}
			}
		}
	}
	
	/*
	 * Override to use.
	 */
	public void startMainElement() {
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (!canceled && tagEntered && tag.equals(localName)) {
			tagEntered = false;
			
			Vector<Integer> availableIndices = getAvailableIndices(values);
			
			int length = availableIndices.size();
			
			String[] availableValues = new String[length];
			
			for (int n = 0; n < length; n++) {
				availableValues[n] = values[availableIndices.get(n)];
			}
			
			if (ignoreValues != null) {
				for (int n = 0, i = ignoreValues.length / 2; n < i; n++) {
					for (int k = 0; k < length; k++) {
						if (fields[availableIndices.get(k)].equalsIgnoreCase(ignoreValues[n*2]) && availableValues[k].equalsIgnoreCase(ignoreValues[n*2+1])) {
							addSkippedEntry();
							return;
						}
					}
				}
			}
			
			Cursor cursor = null;
			
			if (existanceFields == null) {
				cursor = context.getContentResolver().query(contentUri, null, generateWhereQuery(fields, availableIndices), availableValues, null);
			} else {
				for (int n = 0; n < existanceLength; n++) {
					existanceValues[n] = values[existancePositions[n]];
				}
				cursor = context.getContentResolver().query(contentUri, null, generateWhereQuery(existanceFields), existanceValues, null);
			}
			
			ContentValues contentValues = new ContentValues();
			
			for (int n = 0; n < length; n++) {
				contentValues.put(fields[availableIndices.get(n)], availableValues[n]);
			}
			addExtraContentValues(contentValues);
			
			if (!cursor.moveToFirst()) {
				context.getContentResolver().insert(contentUri, contentValues);
			} else if (updateOnExist) {
				/** Update the existing values */
				context.getContentResolver().update(contentUri, contentValues, generateWhereQuery(existanceFields), existanceValues);
			}
			cursor.close();
			importTask.progress(BackupTask.MESSAGE_PROGRESS, ++position);
		}
	}
	
	/*
	 * Override to use.
	 */
	public void addExtraContentValues(ContentValues contentValues) {
		
	}
	
	private static String generateWhereQuery(String[] fields, Vector<Integer> availableIndices) {
		int length = availableIndices.size();
		
		if (length > 0) {
			StringBuilder builder = new StringBuilder(fields[availableIndices.get(0)]);
			
			builder.append(Strings.DB_ARG);
			for (int n = 1; n < length; n++) {
				builder.append(Strings.AND);
				builder.append(fields[availableIndices.get(n)]);
				builder.append(Strings.DB_ARG);
			}
			return builder.toString();
		} else {
			return null;
		}
	}
	
	private static String generateWhereQuery(String[] fields) {
		int length = fields.length;
		
		if (length > 0) {
			StringBuilder builder = new StringBuilder(fields[0]);
			
			builder.append(Strings.DB_ARG);
			for (int n = 1; n < length; n++) {
				builder.append(Strings.AND);
				builder.append(fields[n]);
				builder.append(Strings.DB_ARG);
			}
			return builder.toString();
		} else {
			return null;
		}
	}
	
	private static Vector<Integer> getAvailableIndices(String[] values) {
		int length = values != null ? values.length : 0;
		
		Vector<Integer> result = new Vector<Integer>(length);
		
		for (int n = 0; n < length; n++) {
			if (values[n] != null) {
				result.add(n);
			}
		}
		return result;
	}
	
}
