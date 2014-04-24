
package com.cbsb.backup.exporter;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import android.content.Context;
import com.cbsb.backup.BackupActivity;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public abstract class Exporter {
	private static final String XML_START = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	
	private static final String S_DATE = "s date=\"";
	
	private static final String _COUNT = "\" count=\"";
	
	protected static final String TAG_END_QUOTE = "\">\n";
	
	private static final String TAGS_END = "s>\n";
	
	protected static final String ENDTAG_START = "</";
	
	protected static final String TAG_END = ">\n";
	
	public static class ExporterInfos {
		public int[] ids;
		
		public String[] names;
		
		public ExporterInfos(int[] ids, String[] names) {
			this.ids = ids;
			this.names = names;
		}
		
		public int length() {
			return ids != null ? ids.length : 0;
		}
		
	}
	
	protected ExportTask exportTask;
	
	protected boolean canceled;
	
	public Exporter(ExportTask exportTask) {
		this.exportTask = exportTask;
		canceled = false;
	}
	
	protected abstract int export(String filename) throws Exception;
	
	public int export() throws Exception {
		if (!BackupActivity.DIR.exists() && !BackupActivity.DIR.mkdir()) {
			throw new Exception(exportTask.getContext().getString(R.string.error_couldnotcreatebackupfolder, BackupActivity.DIR.toString()));
		}
		return export(new StringBuilder(BackupActivity.DIR.toString()).append('/').append(getContentName()).append(Strings.FILE_SUFFIX).append(System.currentTimeMillis()).append(Strings.FILE_EXTENSION).toString());
	}
	
	public abstract String getContentName();
	
	public abstract int getTranslatedContentName();
	
	public void cancel() {
		canceled = true;
	}
	
	public abstract String[] getExportedFilenames();
	
	public abstract int getId();
	
	public boolean isEncrypted() {
		return false;
	}
	
	public static Exporter getById(int id, ExportTask exportTask) {
		switch (id) {
			case BookmarkExporter.ID:
				return new BookmarkExporter(exportTask);
			case CallLogExporter.ID:
				return new CallLogExporter(exportTask);
			case SMSExporter.ID:
				return new SMSExporter(exportTask);
			case UserDictionaryExporter.ID:
				return new UserDictionaryExporter(exportTask);
			case PlaylistExporter.ID:
				return new PlaylistExporter(exportTask);
			case SettingsExporter.ID:
				return new SettingsExporter(exportTask);
			case WifiSettingsExporter.ID:
				return new WifiSettingsExporter(exportTask);
			case EverythingExporter.ID:
				return new EverythingExporter(exportTask);
		}
		return null;
	}
	
	public static ExporterInfos getExporterInfos(Context context) {
		Vector<Integer> ids = new Vector<Integer>(10);
		
		Vector<String> names = new Vector<String>(10);
		
		ids.add(BookmarkExporter.ID);
		names.add(context.getString(BookmarkExporter.NAMEID));
		ids.add(CallLogExporter.ID);
		names.add(context.getString(CallLogExporter.NAMEID));
		ids.add(SMSExporter.ID);
		names.add(context.getString(SMSExporter.NAMEID));
		ids.add(UserDictionaryExporter.ID);
		names.add(context.getString(UserDictionaryExporter.NAMEID));
		ids.add(PlaylistExporter.ID);
		names.add(context.getString(PlaylistExporter.NAMEID));
		ids.add(SettingsExporter.ID);
		names.add(context.getString(SettingsExporter.NAMEID));
		if (BackupActivity.CANHAVEROOT) {
			ids.add(WifiSettingsExporter.ID);
			names.add(context.getString(WifiSettingsExporter.NAMEID));
		}
		
		int[] intIds = new int[ids.size()];
		
		for (int n = 0, i = ids.size(); n < i; n++) {
			intIds[n] = ids.get(n);
		}
		return new ExporterInfos(intIds, names.toArray(new String[0]));
	}
	
	public static Vector<Exporter> getAllExporters(ExportTask exportTask) {
		Vector<Exporter> result = new Vector<Exporter>(10);
		
		result.add(new BookmarkExporter(exportTask));
		result.add(new CallLogExporter(exportTask));
		result.add(new SMSExporter(exportTask));
		result.add(new UserDictionaryExporter(exportTask));
		result.add(new PlaylistExporter(exportTask));
		result.add(new SettingsExporter(exportTask));
		
		if (BackupActivity.CANHAVEROOT) {
			result.add(new WifiSettingsExporter(exportTask));
		}
		
		return result;
	}
	
	public static void writeXmlStart(Writer writer, String tag, int count) throws IOException {
		writer.write(XML_START);
    	
    	writer.write('<');
    	writer.write(tag);
    	writer.write(S_DATE);
    	writer.write(Long.toString(System.currentTimeMillis()));
    	writer.write(_COUNT);
    	writer.write(Integer.toString(count));
    	writer.write(TAG_END_QUOTE);
	}
	
	public static void writeXmlEnd(Writer writer, String tag) throws IOException {
		writer.write(ENDTAG_START);
		writer.write(tag);
		writer.write(TAGS_END);
	}
	
}
