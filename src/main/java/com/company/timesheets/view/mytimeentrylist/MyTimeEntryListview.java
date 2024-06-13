package com.company.timesheets.view.mytimeentrylist;


import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.entity.User;
import com.company.timesheets.view.main.MainView;
import com.company.timesheets.view.timeentry.TimeEntryDetailView;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-time-entries", layout = MainView.class)
@ViewController("ts_TimeEntry.my")
@ViewDescriptor("my-time-entry-listview.xml")
public class MyTimeEntryListview extends StandardView {

    @ViewComponent
    private DataGrid<TimeEntry> timeEntriesDataGrid;
    @Autowired
    private TimeEntrySupport timeEntrySupport;
    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;
    @ViewComponent
    private CollectionLoader<TimeEntry> timeEntriesDl;
    @Autowired
    private DialogWindows dialogWindows;

    @Subscribe("timeEntriesDataGrid.copy")
    public void onTimeEntriesDataGridCopy(final ActionPerformedEvent event) {
        TimeEntry selectedItem = timeEntriesDataGrid.getSingleSelectedItem();
        if (selectedItem == null) {
            return;
        }

        TimeEntry copiedEntity = timeEntrySupport.copy(selectedItem);

        DialogWindow<TimeEntryDetailView> window = dialogWindows.detail(timeEntriesDataGrid)
                .withViewClass(TimeEntryDetailView.class)
                .newEntity(copiedEntity)
                .build();

        window.getView().setOwnTimeEntry(true);
        window.open();
    }

    @Install(to = "timeEntriesDataGrid.create", subject = "queryParametersProvider")
    private QueryParameters timeEntriesDataGridCreateQueryParametersProvider() {
        return QueryParameters.of(TimeEntryDetailView.PARAMETER_OWN_TIME_ENTRY, "");
    }

    @Install(to = "timeEntriesDataGrid.edit", subject = "queryParametersProvider")
    private QueryParameters timeEntriesDataGridEditQueryParametersProvider() {
        return QueryParameters.of(TimeEntryDetailView.PARAMETER_OWN_TIME_ENTRY, "");
    }


}