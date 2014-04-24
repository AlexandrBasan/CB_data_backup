

package com.cbsb.backup.exporter;

import java.io.IOException;
import java.io.Writer;

import android.database.Cursor;
import android.provider.MediaStore.Audio;
import android.text.TextUtils;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class PlaylistExporter extends SimpleExporter {
	public static final int ID = 5;
	
	public static final int NAMEID = R.string.playlists;
	
	private static final String QUERY_ID = Audio.Media._ID+"=?";
	
	private static final String ENDQUOTETAG = "\"/>";
	
	private static final String[] PROJECTION_AUDIOID = new String[] {Audio.Playlists.Members.AUDIO_ID};
	
	private static final String[] PROJECTION_DATA = new String[] {Audio.Media.DATA};
	
	private int idPosition;
	
	public PlaylistExporter(ExportTask exportTask) {
		super(Strings.TAG_PLAYLIST, Audio.Playlists.EXTERNAL_CONTENT_URI, exportTask);
		idPosition = -1;
	}

	@Override
	public String getContentName() {
		return Strings.PLAYLISTS;
	}

	@Override
	public void addText(Cursor cursor, Writer writer) throws IOException {
		if (idPosition < 0) {
			idPosition = cursor.getColumnIndex(Audio.Playlists._ID);
		}
		writer.write('\n');
		
		Cursor audioIdCursor = context.getContentResolver().query(Audio.Playlists.Members.getContentUri(Strings.EXTERNAL, cursor.getLong(idPosition)), PROJECTION_AUDIOID, null, null, Audio.Playlists.Members.PLAY_ORDER);
		
		while(!canceled && audioIdCursor.moveToNext()) {
			Cursor audioFileCursor = context.getContentResolver().query(Audio.Media.EXTERNAL_CONTENT_URI, PROJECTION_DATA, QUERY_ID, new String[] {audioIdCursor.getString(0)}, null);
			
			if (audioFileCursor.moveToNext()) {
				writer.write('<');
				writer.write(Strings.TAG_FILE);
				writer.write(' ');
				writer.write(Audio.Media.DATA);
				writer.write(EQUALS);
				writer.write(TextUtils.htmlEncode(audioFileCursor.getString(0)));
				writer.write(ENDQUOTETAG);
			}
			audioFileCursor.close();
		}
		audioIdCursor.close();
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
