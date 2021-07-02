package org.senera.manager;

import android.content.Context;
import android.content.Intent;

import org.senera.Constants;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ChartSpectrumListener implements SpectrumListener {

    private Context mContext;

    public ChartSpectrumListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void processSpectrum(Spectrum spectrum) {
        Intent localIntent = new Intent(Constants.LISTENER_ACTION).putExtra(Constants.EXTENDED_DATA_CHART, spectrum.getChannelValues());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(localIntent);
    }
}
