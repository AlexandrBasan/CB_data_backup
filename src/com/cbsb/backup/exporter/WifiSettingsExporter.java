
package com.cbsb.backup.exporter;

import com.cbsb.backup.R;
import android.content.Context;

public class WifiSettingsExporter extends Exporter {
	public static final int ID = 7;
	
	public static final int NAMEID = R.string.wifisettings;
	
	private Context context;
	
	private String filename;
	
	public WifiSettingsExporter(ExportTask exportTask) {
		super(exportTask);
		this.context = exportTask.getContext();
	}

	@Override
	public int export(String filename) throws Exception {
		// TODO Auto-generated method stub
		this.filename = filename;
		
		return 0;
	}

	@Override
	public String getContentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getExportedFilenames() {
		return new String[] {filename};
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getTranslatedContentName() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEncrypted() {
		return true;
	}

}
