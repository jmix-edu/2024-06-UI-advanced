package com.company.timesheets.view.project;

import com.company.timesheets.entity.Client;
import com.company.timesheets.entity.Project;
import com.company.timesheets.entity.Task;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataManager;
import io.jmix.core.MetadataTools;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

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
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private MetadataTools metadataTools;

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

    @Supply(to = "clientField", subject = "renderer")
    private Renderer<Client> clientFieldRenderer() {
        return new ComponentRenderer<>(client -> {
            FlexLayout wrapper = uiComponents.create(FlexLayout.class);
            wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
            wrapper.addClassNames(LumoUtility.Gap.MEDIUM);

            String clientName = metadataTools.getInstanceName(client);

            wrapper.add(
                    createAvatar(clientName, client.getImage(), "var(--lumo-size-xs)"),
                    new Text(clientName)
            );

            return wrapper;
        });
    }

    private Component createAvatar(String name, byte[] data, String size) {
        Avatar avatar = uiComponents.create(Avatar.class);
        avatar.setName(name);

        if (data != null) {
            StreamResource imageResource = new StreamResource("avatar.png",
                    () -> new ByteArrayInputStream(data));
            avatar.setImageResource(imageResource);
        }

        avatar.setWidth(size);
        avatar.setHeight(size);

        return avatar;
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