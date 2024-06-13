package com.company.timesheets.view.main;

import com.company.timesheets.app.TimeEntryStatusChangedEvent;
import com.company.timesheets.entity.TimeEntry;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

@Route("")
@RouteAlias("home")
@RouteAlias("main")
@ViewController("ts_MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {

    @Autowired
    private Metadata metadata;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private JmixListMenu menu;

    @Subscribe
    public void onInit(final InitEvent event) {
        updateRejectedTimeEntries();
    }

    @EventListener
    private void timeEntryStatusChanged(TimeEntryStatusChangedEvent event) {
        updateRejectedTimeEntries();
    }

    private void updateRejectedTimeEntries() {
        LoadContext<TimeEntry> loadContext =
                new LoadContext<>(metadata.getClass(TimeEntry.class));
        loadContext.setQueryString("select e from ts_TimeEntry e " +
                "where e.user.username = :current_user_username " +
                "and e.status = @enum(com.company.timesheets.entity.TimeEntryStatus.REJECTED)");
        long count = dataManager.getCount(loadContext);

        Span badge = null;
        if (count > 0) {
            badge = new Span("" + count);
            badge.getElement().getThemeList().add("badge error");
        }
        ListMenu.MenuItem menuItem = menu.getMenuItem("ts_TimeEntry.my");
        menuItem.setSuffixComponent(badge);
    }
}
