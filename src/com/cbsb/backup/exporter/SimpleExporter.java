

package com.cbsb.backup.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.cbsb.backup.BackupTask;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public abstract class SimpleExporter extends Exporter {
	protected static final String EQUALS = "=\"";
	
	protected Context context;
	
	private String tag;
	
	private String[] fields;
	
	private Uri contentUri;
	
	private boolean checkFields;
	
	private String selection;
	
	private String filename;
	
	private String sortOrder;
	
	private String[] optionalFields;
	
	public SimpleExporter(String tag, String[] fields, Uri contentUri, boolean checkFields, String selection, String sortOrder, ExportTask exportTask, String[] optionalFields) {
		super(exportTask);
		this.context = exportTask.getContext();
		this.tag = tag;
		this.fields = fields;
		this.contentUri = contentUri;
		this.selection = selection;
		this.checkFields = checkFields;
		this.sortOrder = sortOrder;
		this.optionalFields = optionalFields;
	}
	
	public SimpleExporter(String tag, String[] fields, Uri contentUri, boolean checkFields, String selection, ExportTask exportTask) {
		this(tag, fields, contentUri, checkFields, selection, null, exportTask, null);
	}
	
	public SimpleExporter(String tag, String[] fields, Uri contentUri, boolean checkFields, ExportTask exportTask) {
		this(tag, fields, contentUri, checkFields, null, exportTask);
	}
	
	public SimpleExporter(String tag, Uri contentUri, String selection, ExportTask exportTask) {
		this(tag, null, contentUri, false, selection, exportTask);
	}
	
	public SimpleExporter(String tag, Uri contentUri, ExportTask exportTask) {
		this(tag, contentUri, null, exportTask);
	}
	
	public final int export(String filename) throws Exception {
		this.filename = filename;
		
		Cursor cursor = context.getContentResolver().query(contentUri, null, selection, null, sortOrder);
		
		if (checkFields && fields != null) {
			if (cursor == null || !checkFieldNames(cursor.getColumnNames(), fields)) {
				throw new Exception(context.getString(R.string.error_unsupporteddatabasestructure));
			}
			if (optionalFields != null && optionalFields.length > 0) {
				fields = determineFields(cursor.getColumnNames(), fields, optionalFields);
			}
		} else if (fields == null) {
			if (cursor == null) {
				this.filename = null;
				return 0;
			} else {
				fields = cursor.getColumnNames();
			}
		}
		
		int count = cursor.getCount();
		
		if (count == 0) {
			this.filename = null;
			return 0;
		}
		
		exportTask.progress(BackupTask.MESSAGE_COUNT, count);
		exportTask.progress(BackupTask.MESSAGE_PROGRESS, 0);

		int length = fields.length;
    	
    	int[] positions = new int[length];
    	
    	for (int n = 0; n < length; n++) {
    		positions[n] = cursor.getColumnIndex(fields[n]);
    	}
    	
    	BufferedWriter writer = new BufferedWriter(new PrintWriter(filename, Strings.UTF8));
    	
    	writeXmlStart(writer, tag, count);
    	
    	int position = 0;
    	
    	while (!canceled && cursor.moveToNext()) {
    		writer.write('<');
    		writer.write(tag);
    		for (int n = 0; n < length; n++) {
    			try {
    				String string = cursor.getString(positions[n]);
        			
        			if (string != null && !BaseColumns._ID.equals(fields[n])) {
        				writer.write(' ');
        				writer.write(fields[n]);
        				writer.write(EQUALS);
            			writer.write(TextUtils.htmlEncode(string));
            			writer.write('"');
        			}
    			} catch (Exception e) {
    				// if there is blob data
    			}
    		}
    		writer.write('>');
    		addText(cursor, writer);
    		writer.write(ENDTAG_START);
    		writer.write(tag);
    		writer.write(TAG_END);
    		
    		exportTask.progress(BackupTask.MESSAGE_PROGRESS, ++position);
    	}
    	cursor.close();
    	
    	writeXmlEnd(writer, tag);
		writer.close();
    	
    	if (!canceled) {
    		return count;
    	} else {
    		new File(filename).delete();
    		this.filename = null;
    		return -1;
    	}
	}
	
	private String[] determineFields(String[] columnNames, String[] fields, String[] optionalFields) {
		Vector<String> result = new Vector<String>();
		
		for (String field : fields) {
			result.add(field);
		}
		for (String field : optionalFields) {
			if (Strings.indexOf(columnNames, field) > -1) {
				result.add(field);
			}
		}
		return result.toArray(new String[0]);
	}

	protected boolean checkFieldNames(String[] availableFieldNames, String[] neededFieldNames) {
		for (int n = 0, i = neededFieldNames != null ? neededFieldNames.length : 0; n < i; n++) {
    		if (Strings.indexOf(availableFieldNames, neededFieldNames[n]) == -1) {
    			return false;
    		}
    	}
    	return true;
	}
	
	/*
	 * Override to use
	 */
	public void addText(Cursor cursor, Writer writer) throws IOException {
		
	}
	
	@Override
	public String[] getExportedFilenames() {
		return new String[] {filename};
	}
	
	public String getFilename() {
		return filename;
	}

}
