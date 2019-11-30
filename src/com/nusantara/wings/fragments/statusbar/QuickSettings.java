/*
 * Copyright (C) 2017-2019 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nusantara.wings.fragments.statusbar;

import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.search.BaseSearchIndexProvider;

import com.nusantara.support.preferences.CustomSeekBarPreference;
import com.nusantara.support.preferences.SystemSettingEditTextPreference;

@SearchIndexable
public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String FOOTER_TEXT_STRING = "footer_text_string";

    private CustomSeekBarPreference mQsRowsPort;
    private CustomSeekBarPreference mQsRowsLand;
    private CustomSeekBarPreference mQsColumnsPort;
    private CustomSeekBarPreference mQsColumnsLand;
    private SystemSettingEditTextPreference mFooterString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.nad_quick_settings);

        final ContentResolver resolver = getActivity().getContentResolver();

        int value = Settings.System.getIntForUser(resolver,
                Settings.System.QS_ROWS_PORTRAIT, 3, UserHandle.USER_CURRENT);
        mQsRowsPort = (CustomSeekBarPreference) findPreference("qs_rows_portrait");
        mQsRowsPort.setValue(value);
        mQsRowsPort.setOnPreferenceChangeListener(this);

        value = Settings.System.getIntForUser(resolver,
                Settings.System.QS_ROWS_LANDSCAPE, 2, UserHandle.USER_CURRENT);
        mQsRowsLand = (CustomSeekBarPreference) findPreference("qs_rows_landscape");
        mQsRowsLand.setValue(value);
        mQsRowsLand.setOnPreferenceChangeListener(this);

        value = Settings.System.getIntForUser(resolver,
                Settings.System.QS_COLUMNS_PORTRAIT, 3, UserHandle.USER_CURRENT);
        mQsColumnsPort = (CustomSeekBarPreference) findPreference("qs_columns_portrait");
        mQsColumnsPort.setValue(value);
        mQsColumnsPort.setOnPreferenceChangeListener(this);

        value = Settings.System.getIntForUser(resolver,
                Settings.System.QS_COLUMNS_LANDSCAPE, 4, UserHandle.USER_CURRENT);
        mQsColumnsLand = (CustomSeekBarPreference) findPreference("qs_columns_landscape");
        mQsColumnsLand.setValue(value);
        mQsColumnsLand.setOnPreferenceChangeListener(this);

        mFooterString = (SystemSettingEditTextPreference) findPreference(FOOTER_TEXT_STRING);
        mFooterString.setOnPreferenceChangeListener(this);
        String footerString = Settings.System.getString(getContentResolver(),
                FOOTER_TEXT_STRING);
        if (footerString != null && footerString != "")
            mFooterString.setText(footerString);
        else {
            mFooterString.setText("#Nusantara Project");
            Settings.System.putString(getActivity().getContentResolver(),
                    Settings.System.FOOTER_TEXT_STRING, "#Nusantara Project");
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mQsRowsPort) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_ROWS_PORTRAIT, val, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsRowsLand) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_ROWS_LANDSCAPE, val, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsColumnsPort) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_COLUMNS_PORTRAIT, val, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsColumnsLand) {
            int val = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_COLUMNS_LANDSCAPE, val, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mFooterString) {
            String value = (String) newValue;
            if (value != "" && value != null)
                Settings.System.putString(getActivity().getContentResolver(),
                        Settings.System.FOOTER_TEXT_STRING, value);
            else {
                mFooterString.setText("#Nusantara Project");
                Settings.System.putString(resolver,
                        Settings.System.FOOTER_TEXT_STRING, "#Nusantara Project");
            }
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.NUSANTARA_PRJ;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.nad_quick_settings);
}
