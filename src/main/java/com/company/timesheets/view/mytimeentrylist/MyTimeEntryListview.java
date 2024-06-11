package com.company.timesheets.view.mytimeentrylist;


import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.entity.User;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
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

    @Subscribe("timeEntriesDataGrid.copy")
    public void onTimeEntriesDataGridCopy(final ActionPerformedEvent event) {
        TimeEntry selectedItem = timeEntriesDataGrid.getSingleSelectedItem();
        if (selectedItem == null) {
            return;
        }

        TimeEntry copiedEntity = timeEntrySupport.copy(selectedItem);
    }

    @Subscribe
    public void onInitEntity(final StandardDetailView.InitEntityEvent<TimeEntry> event) {
        User user = (User) currentUserSubstitution.getEffectiveUser();

        timeEntriesDl.setParameter("username_obtain_in_controller", user.getUsername());

        timeEntriesDl.load();
    }

}