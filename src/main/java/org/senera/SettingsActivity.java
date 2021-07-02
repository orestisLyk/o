package org.senera;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import org.senera.senera.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || SensorPreferenceFragment.class.getName().equals(fragmentName)
                || ServerPreferenceFragment.class.getName().equals(fragmentName)
                || SeneraPreferenceFragment.class.getName().equals(fragmentName)
                || AnseenPreferenceFragment.class.getName().equals(fragmentName)
                || RitecPreferenceFragment.class.getName().equals(fragmentName)
                || CalibrationPreferenceFragment.class.getName().equals(fragmentName)
;
    }


    /**
     * This fragment shows sensor preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SensorPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sensor);
            setHasOptionsMenu(true);

            // Bind the summaries of sensor preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("pref_key_sensor_id"));
            bindPreferenceSummaryToValue(findPreference("pref_key_sensor_type"));
            bindPreferenceSummaryToValue(findPreference("pref_key_sensor_control_port"));
            bindPreferenceSummaryToValue(findPreference("pref_key_device_serial_number"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * This fragment shows fusion server preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ServerPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_server);
            setHasOptionsMenu(true);

            // Bind the summaries of server preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("pref_key_fusion_server_address"));
            bindPreferenceSummaryToValue(findPreference("pref_key_fusion_server_port"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CalibrationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_calibration);
            setHasOptionsMenu(true);

            // Bind the summaries of server preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("pref_key_Calibration_energy_1"));
            bindPreferenceSummaryToValue(findPreference("pref_key_Calibration_energy_2"));
            bindPreferenceSummaryToValue(findPreference("pref_key_Calibration_channel_for_energy_1"));
            bindPreferenceSummaryToValue(findPreference("pref_key_Calibration_channel_for_energy_2"));
            bindPreferenceSummaryToValue(findPreference("pref_key_calibration_type"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }



    /**
     * This fragment shows SENERA preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SeneraPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_senera);
            setHasOptionsMenu(true);

            // Bind the summaries of preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("pref_key_use_static_location"));
            bindPreferenceSummaryToValue(findPreference("pref_key_static_loc_lon"));
            bindPreferenceSummaryToValue(findPreference("pref_key_static_loc_lat"));
            bindPreferenceSummaryToValue(findPreference("pref_key_static_loc_alt"));
            //bindPreferenceSummaryToValue(findPreference("pref_key_use_console_spectrum_listener"));
            //bindPreferenceSummaryToValue(findPreference("pref_key_use_file_spectrum_listener"));
            bindPreferenceSummaryToValue(findPreference("pref_key_file_spectrum_listener_filename"));
            //bindPreferenceSummaryToValue(findPreference("pref_key_use_csv_spectrum_listener"));
            bindPreferenceSummaryToValue(findPreference("pref_key_file_csv_listener_filename"));
            //bindPreferenceSummaryToValue(findPreference("pref_key_use_ws_spectrum_listener"));
            bindPreferenceSummaryToValue(findPreference("pref_key_jd2xx_port_timeout"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * This fragment shows ANSEEN sensor preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AnseenPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_anseen);
            setHasOptionsMenu(true);

            // Bind the summaries of ANSeeN sensor preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("pref_key_anseen_gain"));
            bindPreferenceSummaryToValue(findPreference("pref_key_anseen_lld1"));
            bindPreferenceSummaryToValue(findPreference("pref_key_anseen_uld1"));
            bindPreferenceSummaryToValue(findPreference("pref_key_anseen_hv_enabled"));
            bindPreferenceSummaryToValue(findPreference("pref_key_anseen_hv_value"));
            bindPreferenceSummaryToValue(findPreference("pref_key_anseen_offset"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * This fragment shows RITEC sensor preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class RitecPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_ritec);
            setHasOptionsMenu(true);

            // Bind the summaries of RITEC sensor preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec_channels"));
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec_lld"));
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec_uld"));
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec_high_voltage"));
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec__hv_inhibit"));
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec_energy_per_channel_a"));
            bindPreferenceSummaryToValue(findPreference("pref_key_ritec_energy_per_channel_b"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
