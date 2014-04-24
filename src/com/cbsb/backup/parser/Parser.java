
package com.cbsb.backup.parser;

import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import com.cbsb.backup.Strings;
import com.cbsb.backup.exporter.BookmarkExporter;
import com.cbsb.backup.exporter.CallLogExporter;
import com.cbsb.backup.exporter.PlaylistExporter;
import com.cbsb.backup.exporter.SMSExporter;
import com.cbsb.backup.exporter.SettingsExporter;
import com.cbsb.backup.exporter.UserDictionaryExporter;

public abstract class Parser extends DefaultHandler {
	protected static final String COUNT = "count";
	
	protected Context context;
	
	protected ImportTask importTask;
	
	protected boolean canceled;
	
	private StringBuilder hintStringBuilder;
	
	private int skipped;
	
	public Parser(Context context, ImportTask importTask) {
		this.context = context;
		this.importTask = importTask;
		skipped = 0;
	}

	public final void cancel() {
		canceled = true;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public static Parser createParserByFilename(String filename, Context context, ImportTask importTask) {
		filename = filename.substring(filename.lastIndexOf('/')+1);
		
		if (filename.startsWith(Strings.CALLLOGS)) {
			return new CallLogParser(context, importTask);
		} else if (filename.startsWith(Strings.MESSAGES)) {
			return new MessageParser(context, importTask);
		} else if (filename.startsWith(Strings.BOOKMARKS)) {
			return new BookmarkParser(context, importTask);
		} else if (filename.startsWith(Strings.USERDICTIONARY)) {
			return new UserDictionaryParser(context, importTask);
		} else if (filename.startsWith(Strings.PLAYLISTS)) {
			return new PlaylistParser(context, importTask);
		} else if (filename.startsWith(Strings.SETTINGS)) {
			return new SettingsParser(context, importTask);
		}
		return null;
	}

	public static int getTranslatedParserName(String filename) {
		filename = filename.substring(filename.lastIndexOf('/')+1);
		
		if (filename.startsWith(Strings.CALLLOGS)) {
			return CallLogExporter.NAMEID;
		} else if (filename.startsWith(Strings.MESSAGES)) {
			return SMSExporter.NAMEID;
		} else if (filename.startsWith(Strings.BOOKMARKS)) {
			return BookmarkExporter.NAMEID;
		} else if (filename.startsWith(Strings.USERDICTIONARY)) {
			return UserDictionaryExporter.NAMEID;
		} else if (filename.startsWith(Strings.PLAYLISTS)) {
			return PlaylistExporter.NAMEID;
		} else if (filename.startsWith(Strings.SETTINGS)) {
			return SettingsExporter.NAMEID;
		}
		return android.R.string.unknownName;
	}
	
	public void addHint(CharSequence charSequence) {
		if (hintStringBuilder == null) {
			hintStringBuilder = new StringBuilder(charSequence);
		} else {
			hintStringBuilder.append('\n');
			hintStringBuilder.append(charSequence);
		}
	}
	
	public StringBuilder getHints() {
		return hintStringBuilder;
	}
	
	public boolean hasHints() {
		return hintStringBuilder != null && hintStringBuilder.length() > 0;
	}
	
	public void addSkippedEntry() {
		skipped++;
	}
	
	public int getSkippedEntryCount() {
		return skipped;
	}

}
