

package com.cbsb.backup.exporter;

import java.util.Vector;

import com.cbsb.backup.Strings;

public class EverythingExporter extends Exporter {	
	public static final int ID = 0;
	
	private int currentNameId;
	
	private Vector<Exporter> exporters;
	
	private Vector<Exporter> failedExporters;
	
	public EverythingExporter(ExportTask exportTask) {
		super(exportTask);
		exporters = Exporter.getAllExporters(exportTask);
		failedExporters = new Vector<Exporter>();
	}

	@Override
	public void cancel() {
		// do not care about which exporter is running
		super.cancel();
		for (Exporter exporter : exporters) {
			exporter.cancel();
		}
	}

	@Override
	public int export(String filename) throws Exception {
		int result = 0;
		
		for (Exporter exporter : exporters) {
			currentNameId = exporter.getTranslatedContentName();
			exportTask.progress(ExportTask.MESSAGE_TYPE, exporter.getId()); 
			try {
				result += export(exporter);
			} catch (Throwable t) {
				failedExporters.add(exporter);
			}
		}
		
		return result;
	}
	
	private int export(Exporter exporter) throws Exception {
		return exporter.export();
	}

	@Override
	public String getContentName() {
		return Strings.EMPTY; // since we do not use the filename at all
	}

	@Override
	public String[] getExportedFilenames() {
		Vector<String> exportedFilenames = new Vector<String>();
		
		for (Exporter exporter : exporters) {
			if (!failedExporters.contains(exporter)) {
				String[] filenames = exporter.getExportedFilenames();
				
				for (String filename : filenames) {
					exportedFilenames.add(filename);
				}
			}
		}
		return exportedFilenames.toArray(new String[0]);
	}
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getTranslatedContentName() {
		return currentNameId;
	}

}
