package com.weizilla.transit.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Filterable;

/**
 * calls the filter on an adapter when text changes
 *
 * @author wei
 *         Date: 11/26/13
 *         Time: 3:32 PM
 */
public class FilterTextWatcher implements TextWatcher
{
    private Filterable filterable;

    public FilterTextWatcher(Filterable filterable)
    {
        this.filterable = filterable;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // do nothing
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        filterable.getFilter().filter(s.toString());
    }
}
