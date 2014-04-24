

package com.cbsb.backup;

import java.io.File;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.TextUtils;
import com.cbsb.backup.R;

public class ApplicationPreferencesActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference storageLocationPreference = (EditTextPreference) findPreference(Strings.PREFERENCE_STORAGELOCATION);
		
		storageLocationPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				if (newValue != null) {
					String newDir = newValue.toString();
					
					if (TextUtils.isEmpty(newDir)) {
						newDir = BackupActivity.STANDARD_DIRNAME;
					}
					
					File newDirFile = new File(newDir);
					
					if (!BackupActivity.DIR.equals(newDirFile)) {
						if (newDirFile.isFile()) {
							// show error
							return false;
						} else {
							BackupActivity.DIR = newDirFile;
							BackupActivity.INSTANCE.listAdapter.reset();
							return true;
						}
					}
				}
				return false;
			}
			
		});
	}
	
}
