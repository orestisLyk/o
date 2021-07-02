package org.senera;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;

/**
 * This class holds all the data that we want to
 * survive the lifecycle changes of the MainActivity.
 *
 */
public class MainActivityViewModel extends ViewModel {


    /**
     * Keeps track of the number of measurement we have received from the
     * current measurement session.
     */
    public int count = 0;

    /**
     * Indicates whether we have seen the end of a loop.
     * This helps to distinguish between measurement sessions.
     * While this is false each new measurement we receive is
     * added to the previous measurement.
     */
    private boolean loopFinished = false;

    private int[] chartValues;

    private BarData barData;

    private float[] channelEnergy;

    public BarData getBarData() {
        return barData;
    }

    public void setBarData(BarData barData) {
        this.barData = barData;
    }

    private float getChannelEnergy(float epch,float epch2,int c){
        float enPerBar = epch * c + epch2;
        return enPerBar;
    }

    private float getChannelEnergyGraphCal(float en1,float en2,int chan1,int chan2,int c){
        float epch = (en2 -en1)/(chan2 - chan1);
        float epch2 = en1 - epch * chan1;
        float channelEnergy = getChannelEnergy(epch,epch2,c);
        return channelEnergy;
    }
    public void setBarData(int[] chartValues) {
        if (count == 0) {
            this.chartValues = chartValues;
        }

        List<BarEntry> entries = new ArrayList<BarEntry>();
        int len = this.chartValues.length;

        for (int i = 0; i < len; i++) {
            if (count > 0) {

                this.chartValues[i] += chartValues[i];
                //this.chartValues[i] += (chartValues[i] - this.chartValues[i])/(count + 1)
                /** This would make chartValues the average measurement instead of the sum(in case the received measurement is cps).
                 *It requires that int count is raised after calling setBarData(),but the count++ command could
                 *alternatively be included inside this method to avoid mistakes.
                 **/

            }
            entries.add(new BarEntry(i + 1, this.chartValues[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        this.barData = barData;
    }


    public void setBarDataCalibrated(int[] chartValues,float epch,float epch2) {
        if (count == 0) {
            this.chartValues = chartValues;

        }

        List<BarEntry> entries = new ArrayList<BarEntry>();
        int len = this.chartValues.length;

        for (int i = 0; i < len; i++) {
            if (count > 0) {

                this.chartValues[i] += chartValues[i];
                //this.chartValues[i] += (chartValues[i] - this.chartValues[i])/(count + 1)
                /** This would make chartValues the average measurement instead of the sum(in case the received measurement is cps).
                 *It requires that int count is raised after calling setBarData(),but the count++ command could
                 *alternatively be included inside this method to avoid mistakes.
                 **/

            }
            entries.add(new BarEntry(getChannelEnergy(epch,epch2,i + 1), this.chartValues[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        this.barData = barData;
    }
    public void setBarDataGraphCalibrated(int[] chartValues,float en1,float en2,int chan1,int chan2) {
        if (count == 0) {
            this.chartValues = chartValues;

        }

        List<BarEntry> entries = new ArrayList<BarEntry>();
        int len = this.chartValues.length;

        for (int i = 0; i < len; i++) {
            if (count > 0) {

                this.chartValues[i] += chartValues[i];
                //this.chartValues[i] += (chartValues[i] - this.chartValues[i])/(count + 1)
                /** This would make chartValues the average measurement instead of the sum(in case the received measurement is cps).
                 *It requires that int count is raised after calling setBarData(),but the count++ command could
                 *alternatively be included inside this method to avoid mistakes.
                 **/

            }
            entries.add(new BarEntry(getChannelEnergyGraphCal(en1,en2,chan1,chan2,i + 1), this.chartValues[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        this.barData = barData;
    }






    public boolean isLoopFinished() {
        return loopFinished;
    }

    public void setLoopFinished(boolean loopFinished) {
        this.loopFinished = loopFinished;
    }


}
