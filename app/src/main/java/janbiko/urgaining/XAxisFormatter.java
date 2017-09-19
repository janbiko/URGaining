package janbiko.urgaining;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Snackya on 19.09.2017.
 */

public class XAxisFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public XAxisFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }
}
