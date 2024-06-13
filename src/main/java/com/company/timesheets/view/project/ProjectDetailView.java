package com.company.timesheets.view.project;

import com.company.timesheets.entity.Project;
import com.company.timesheets.entity.Task;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "projects/:id", layout = MainView.class)
@ViewController("ts_Project.detail")
@ViewDescriptor("project-detail-view.xml")
@EditedEntityContainer("projectDc")
@DialogMode(width = "64em")
public class ProjectDetailView extends StandardDetailView<Project> {

    @ViewComponent
    private VerticalLayout tasksWrapper;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private DataGrid<Task> tasksDataGrid;

    @Subscribe("tasksDataGrid.create")
    public void onTasksDataGridCreate(final ActionPerformedEvent event) {
        Task newTask = dataManager.create(Task.class);
        newTask.setProject(getEditedEntity());

        dialogWindows.detail(tasksDataGrid)
                .newEntity(newTask)
                .withParentDataContext(getViewData().getDataContext())
                .open();
    }

    @Subscribe("tasksDataGrid.edit")
    public void onTasksDataGridEdit(final ActionPerformedEvent event) {
        Task selectedItem = tasksDataGrid.getSingleSelectedItem();
        if (selectedItem == null) {
            return;
        }

        dialogWindows.detail(tasksDataGrid)
                .editEntity(selectedItem)
                .withParentDataContext(getViewData().getDataContext())
                .open();
    }


//    @Subscribe
//    public void onInitEntity(final InitEntityEvent<Project> event) {
//        tasksWrapper.setEnabled(false);
//    }
//
//    @Subscribe
//    public void onAfterSave(final AfterSaveEvent event) {
//        tasksWrapper.setEnabled(true);
//    }

//    @Install(to = "tasksDataGrid.create", subject = "initializer")
//    private void tasksDataGridCreateInitializer(final Task task) {
//        task.setProject(getEditedEntity());
//    }
}