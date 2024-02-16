package org.jellyfin.androidtvc.ui.browsing;

import org.jellyfin.androidtvc.ui.livetv.TvManager;
import org.jellyfin.androidtvc.ui.presentation.CardPresenter;
import org.jellyfin.androidtvc.util.apiclient.LifecycleAwareResponse;
import org.jellyfin.apiclient.model.livetv.TimerQuery;

public class BrowseScheduleFragment extends EnhancedBrowseFragment {

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupQueries(final RowLoader rowLoader) {
        TvManager.getScheduleRowsAsync(requireContext(), new TimerQuery(), new CardPresenter(true), mRowsAdapter, new LifecycleAwareResponse<Integer>(getLifecycle()) {
        });
    }
}
