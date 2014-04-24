

package com.cbsb.backup.exporter;

import java.io.File;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import com.cbsb.backup.BackupFilesListAdapter;
import com.cbsb.backup.BackupTask;
import com.cbsb.backup.R;
import com.cbsb.backup.Strings;

public class ExportTask extends BackupTask<Integer, Integer> {
	private Exporter exporter;
	
	private Exception exception;
	
	private BackupFilesListAdapter listAdapter;
	
	public ExportTask(ProgressDialog progressDialog, BackupFilesListAdapter listAdapter) {
		super(progressDialog);
		this.listAdapter = listAdapter;
		
		progressDialog.setButton(Dialog.BUTTON_POSITIVE, null, (OnClickListener) null); // disables the positive button
		progressDialog.setTitle(R.string.dialog_export);
	}
	
	public Context getContext() {
		return progressDialog.getContext();
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		exporter = Exporter.getById(params[0], this);
		publishProgress(MESSAGE_TYPE, params[0]);
		try {
			return exporter.export(); // checks itself for cancellation 
		} catch (Exception e) {
			exception = e;
			return -1;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		progressDialog.setProgress(0);
		progressDialog.dismiss();
		if (result > 0) {
			String[] exportedFilenames = exporter.getExportedFilenames();
			
			Toast.makeText(progressDialog.getContext(), String.format(progressDialog.getContext().getString(R.string.message_exportedto), concat(exportedFilenames)), Toast.LENGTH_LONG).show();
			
			for (int n = 0, i = exportedFilenames.length; n < i; n++) {
				if (exportedFilenames[n] != null) {
					listAdapter.add(new File(exportedFilenames[n]));
				}
			}
			
		} else if (result == 0) {
			Toast.makeText(progressDialog.getContext(), R.string.hint_noexportdata, Toast.LENGTH_LONG).show();
		} else if (result == -1 && exception != null) {
			Toast.makeText(progressDialog.getContext(), String.format(progressDialog.getContext().getString(R.string.error_somethingwentwrong), exception.getMessage()), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}
	
	@Override
	protected void onCancelled() {
		if (exporter != null) {
			exporter.cancel();
		}
		progressDialog.cancel();
		progressDialog.setProgress(0);
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		progressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (values[0] == MESSAGE_TYPE) {
			switch (values[1]) {
				case EverythingExporter.ID:{
					// the case for "everything" is not needed since this is just a set of all available exports
					break;
				}
				default: {
					progressDialog.setMessage(String.format(progressDialog.getContext().getString(R.string.hint_exporting), progressDialog.getContext().getString(exporter.getTranslatedContentName())));
					break;
				}
			}
			
		} else {
			super.onProgressUpdate(values);
		}
		
	}
	
	private static StringBuilder concat(String[] strings) {
		StringBuilder builder = new StringBuilder();
		
		boolean first = true;
		
		for (int n = 0, i = strings != null ? strings.length : 0; n < i; n++) {
			if (strings[n] != null) {
				if (first == true) {
					first = false;
				} else {
					builder.append(Strings.COMMA);
				}
				builder.append(strings[n]);
			}
			
		}
		return builder;
	}

}
