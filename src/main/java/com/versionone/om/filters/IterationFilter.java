package com.versionone.om.filters;

import com.versionone.apiclient.*;
import com.versionone.om.Entity;
import com.versionone.om.Iteration;
import com.versionone.om.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter iterations.
 */
public class IterationFilter extends BaseAssetFilter {
    /**
     * State of the Iteration.
     */
    public enum IterationState {
        /**
         * Not active yet.
         */
        Future,

        /**
         * Active or open.
         */
        Active,

        /**
         * Closed or inactive.
         */
        Closed
    }

    @Override
    public Class<? extends Entity> getEntityType() {
        return Iteration.class;
    }

    /**
     * State of the Iteration (Future, Active, or Closed).
     */
    public List<IterationState> state = newList();

    /**
     * Schedules this item belongs to.
     */
    public List<Schedule> schedule = newList();

    @Override
    boolean hasState() {
        return state.size() != 0;
    }

    @Override
    boolean isActive() {
        return state.contains(IterationState.Active);
    }

    @Override
    boolean isClosed() {
        return state.contains(IterationState.Closed);
    }

    boolean hasFuture() {
        return state.contains(IterationState.Future);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        List<String> states = new ArrayList<String>(3);
        if (hasFuture()) {
            states.add("Future");
        }

        if (isActive()) {
            states.add("Active");
        }

        if (isClosed()) {
            states.add("Closed");
        }

        if ((states.size() > 0) && (states.size() < 3)) {
            builder.root.and(
                new IFilterTerm[]{
                    new TokenTerm("AssetState=" + TextBuilder.join(states, ",",
                    TextBuilder.STRINGIZER_DELEGATE.build(new ValueStringizer(), "stringize")))}
            );
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState!='Dead'")});
        }
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);
        builder.relation("Schedule", schedule);
    }
}
