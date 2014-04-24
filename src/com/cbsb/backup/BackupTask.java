

package com.cbsb.backup;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.WindowManager;
import com.cbsb.backup.R;

public abstract class BackupTask<A, B> extends AsyncTask<A, Integer, B> {
	public static final int MESSAGE_TYPE = 0;
	
	public static final int MESSAGE_COUNT = 1;
	
	public static final int MESSAGE_PROGRESS = 2;
	
	protected ProgressDialog progressDialog;
	
	public BackupTask(final ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
		progressDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					progressDialog.setProgress(0);
					cancel(true);
				}
				return true;
			}
		});
		progressDialog.setButton(Dialog.BUTTON_NEGATIVE, progressDialog.getContext().getString(android.R.string.cancel), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				progressDialog.setProgress(0);
				cancel(true);
			}
		});
	}
	
	public void progress(Integer... params) {
		publishProgress(params);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (values[0] == MESSAGE_COUNT) {
			progressDialog.setMax(values[1]);
		} else if (values[0] == MESSAGE_PROGRESS) {
			progressDialog.setProgress(values[1]);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(B result) {
		progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onPreExecute();
	}

}
