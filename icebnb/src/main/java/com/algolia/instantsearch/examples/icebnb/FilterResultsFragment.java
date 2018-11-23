package com.algolia.instantsearch.examples.icebnb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.core.model.FacetStat;
import com.algolia.instantsearch.core.model.NumericRefinement;

import java.util.ArrayList;
import java.util.List;

public class FilterResultsFragment extends DialogFragment {
    public static final String TAG = "FilterResultsFragment";

    private Searcher searcher;

    private List<FilterDescription> futureFilters = new ArrayList<>();
    private SparseArray<View> filterViews = new SparseArray<>();
    private int filterCount = 0;

    /**
     * Default constructor required when the fragment is restored.
     * You <b>MUST</b> use {@link FilterResultsFragment#get(Searcher)} instead as this fragment requires a Searcher reference.
     *
     * @deprecated you <b>MUST</b> use {@link FilterResultsFragment#get(Searcher)} instead.
     */
    public FilterResultsFragment() {}

    /**
     * Get a FilterResultsFragment instance linked with a given searcher.
     *
     * @param searcher the searcher object to associate with this fragment.
     * @return a fragment ready to use.
     */
    public static FilterResultsFragment get(Searcher searcher) {
        final FilterResultsFragment fragment = new FilterResultsFragment();
        fragment.searcher = searcher; //storing the searcher for method calls before onCreateDialog, like addSeekBar
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        searcher = Searcher.get();
        checkHasSearcher();

        final FragmentActivity activity = getActivity();
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        filterViews.clear();
        for (FilterDescription filter : futureFilters) {
            filter.create();
        }
        for (int i = 0; i < filterViews.size(); i++) {
            View v = filterViews.get(i);
            layout.addView(v);
        }

        ScrollView scrollView = new ScrollView(activity);
        scrollView.addView(layout);
        b.setTitle("Filter results").setView(scrollView)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searcher.search();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "Cancelled filtering.", Toast.LENGTH_SHORT).show();
                    }
                });
        return b.create();
    }

    /**
     * Add a SeekBar to the fragment, automatically fetching min and max values for its attribute.
     * This method <b>MUST</b> be called in the enclosing activity's <code>onCreate</code> to setup the required facets <b>before</b> the fragment is created.
     *
     * @param attribute the attribute this SeekBar will filter on.
     * @param steps     the amount of steps between min and max.
     * @return the fragment to allow chaining calls.
     */
    public FilterResultsFragment addSeekBar(final String attribute, final int steps) {
        return addSeekBar(attribute, attribute, null, null, steps);
    }

    /**
     * Add a SeekBar to the fragment, naming the attribute differently in the UI and automatically fetching min and max values for its attribute.
     * This method <b>MUST</b> be called in the enclosing activity's <code>onCreate</code> to setup the required facets <b>before</b> the fragment is created.
     *
     * @param attribute the attribute this SeekBar will filter on.
     * @param name      the name to use when referring to the refined attribute.
     * @param steps     the amount of steps between min and max.
     * @return the fragment to allow chaining calls.
     */
    public FilterResultsFragment addSeekBar(final String attribute, final String name, final int steps) {
        return addSeekBar(attribute, name, null, null, steps);
    }

    /**
     * Add a SeekBar to the fragment, naming the attribute differently in the UI.
     *
     * @param attribute the attribute this SeekBar will filter on.
     * @param name      the name to use when referring to the refined attribute.
     * @param min       the minimum value that the user can select.
     * @param max       the maximum value that the user can select.
     * @param steps     the amount of steps between min and max.
     * @return the fragment to allow chaining calls.
     */
    public FilterResultsFragment addSeekBar(final String attribute, final String name, final Double min, final Double max, final int steps) {
        futureFilters.add(new SeekBarDescription(attribute, name, min, max, steps, filterCount++));
        searcher.addFacet(attribute);
        return this;
    }

    /**
     * Add a CheckBox to the fragment, naming the attribute differently in the UI.
     *
     * @param attribute     the attribute this SeekBar will filter on.
     * @param name          the name to use when referring to the refined attribute.
     * @param checkedIsTrue if {@code true}, a checked box will refine on attribute:true, else on attribute:false.
     * @return the fragment to allow chaining calls.
     */
    public FilterResultsFragment addCheckBox(final String attribute, final String name, final boolean checkedIsTrue) {
        futureFilters.add(new CheckBoxDescription(attribute, name, checkedIsTrue, filterCount++));
        searcher.addFacet(attribute);
        return this;
    }

    private View getInflatedLayout(int layoutId) {
        Activity activity = getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(layoutId, null);
    }

    private void createFilter(final FilterDescription filter) {
        checkHasSearcher();

        final String attribute = filter.attribute;
        final String name = filter.name;
        boolean isSeekBar = filter instanceof SeekBarDescription;

        final View filterLayout = getInflatedLayout(isSeekBar ? R.layout.layout_seekbar : R.layout.layout_checkbox);

        if (isSeekBar) {
            final double minValue = ((SeekBarDescription) filter).min;
            final double maxValue = ((SeekBarDescription) filter).max;
            final int steps = ((SeekBarDescription) filter).steps;

            final TextView tv = (TextView) filterLayout.findViewById(R.id.dialog_seekbar_text);
            final SeekBar seekBar = (SeekBar) filterLayout.findViewById(R.id.dialog_seekbar_bar);
            final NumericRefinement currentFilter = searcher.getNumericRefinement(attribute, NumericRefinement.OPERATOR_GT);

            if (currentFilter != null && currentFilter.value != 0) {
                final int progressValue = (int) ((currentFilter.value - minValue) * steps / (maxValue - minValue));
                seekBar.setProgress(progressValue);
            }
            seekBar.setMax(steps);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    onUpdate(seekBar);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    onUpdate(seekBar);
                }

                private void onUpdate(final SeekBar seekBar) {
                    final double actualValue = updateSeekBarText(tv, seekBar, name, minValue, maxValue, steps);
                    searcher.addNumericRefinement(new NumericRefinement(attribute, NumericRefinement.OPERATOR_GT, actualValue));
                }
            });

            updateSeekBarText(tv, name, currentFilter != null ? currentFilter.value : minValue, minValue);
        } else {
            final TextView tv = (TextView) filterLayout.findViewById(R.id.dialog_checkbox_text);
            final CheckBox checkBox = (CheckBox) filterLayout.findViewById(R.id.dialog_checkbox_box);
            final Boolean currentFilter = searcher.getBooleanFilter(attribute);
            final FacetStat stats = searcher.getFacetStat(attribute);
            final boolean hasOnlyOneValue = stats != null && stats.min == stats.max;
            final boolean checkedIsTrue = ((CheckBoxDescription) filter).checkedIsTrue;

            if (currentFilter != null) { // If the attribute is currently filtered, show its state
                checkBox.setChecked(currentFilter);
            } else if (hasOnlyOneValue) { // If the attribute is not filtered and has only one value, disable its checkbox
                checkBox.setChecked(stats.min != 0); // If min=max=1, we check the box before disabling
                checkBox.setEnabled(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        searcher.addBooleanFilter(attribute, checkedIsTrue);
                    } else {
                        searcher.removeBooleanFilter(attribute);
                    }
                }
            });
            tv.setText(name != null ? name : attribute);
        }
        filterViews.put(filter.position, filterLayout);
    }

    private double updateSeekBarText(final TextView textView, final SeekBar seekBar, final String name, final double minValue, final double maxValue, int steps) {
        int progress = seekBar.getProgress();
        final double value = minValue + progress * (maxValue - minValue) / steps;
        updateSeekBarText(textView, name, value, minValue);
        return value;
    }

    private void updateSeekBarText(final TextView textView, final String name, final double value, final double minValue) {
        if (value == minValue) {
            textView.setText(String.format("Filter minimum %s", name));
        } else {
            final String html;
            if (value == (long) value) {
                html = String.format("%d $", (long) value);
            } else {
                html = String.format("%.1f $", value);
            }
            textView.setText(Html.fromHtml(html));
        }
    }

    private void checkHasSearcher() {
        if (searcher == null) {
            throw new IllegalStateException("This fragment has no searcher. Did you use FilterResultsFragment.get(Searcher) as required?");
        }
    }

    abstract class FilterDescription {
        final String attribute;
        final String name;
        final int position;

        FilterDescription(String attribute, String name, int position) {
            this.attribute = attribute;
            this.name = name;
            this.position = position;
        }

        protected void create() {
            createFilter(this);
        }
    }

    private class SeekBarDescription extends FilterDescription {
        private Double min;
        private Double max;
        private final int steps;

        SeekBarDescription(String attribute, String name, Double min, Double max, int steps, int position) {
            super(attribute, name, position);
            this.min = min;
            this.max = max;
            this.steps = steps;
        }

        @Override
        protected void create() {
            if (min == null || max == null) {
                FacetStat stats = searcher.getFacetStat(attribute);
                if (stats == null) {
                    throw new RuntimeException("No facet stats were stored for `" + attribute + "`. Did you call addSeekBar(attribute, name, steps) in the activity's onCreate, as required?");
                }
                min = min == null ? stats.min : min;
                max = max == null ? stats.max : max;
            }
            super.create();
        }
    }

    private class CheckBoxDescription extends FilterDescription {
        private final boolean checkedIsTrue;

        CheckBoxDescription(String attribute, String name, boolean checkedIsTrue, int position) {
            super(attribute, name, position);
            this.checkedIsTrue = checkedIsTrue;
        }

    }
}