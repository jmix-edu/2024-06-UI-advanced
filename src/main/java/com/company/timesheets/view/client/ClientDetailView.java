package com.company.timesheets.view.client;

import com.company.timesheets.component.contactInformation.ContactInformationFragment;
import com.company.timesheets.entity.Client;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;

@Route(value = "clients/:id", layout = MainView.class)
@ViewController("ts_Client.detail")
@ViewDescriptor("client-detail-view.xml")
@EditedEntityContainer("clientDc")
public class ClientDetailView extends StandardDetailView<Client> {

    @ViewComponent
    private HorizontalLayout uploadWrapper;
    @ViewComponent
    private FileUploadField imageUpload;
    @ViewComponent
    private JmixImage<byte[]> image;
    @ViewComponent
    private ContactInformationFragment containerInformation;



    @Subscribe
    public void onReady(final ReadyEvent event) {
        updateImage(getEditedEntity().getImage());
        applySecurityPermissions();
    }

    

    private void applySecurityPermissions() {
        uploadWrapper.setVisible(!imageUpload.isReadOnly());
    }



    private void updateImage(byte[] value) {
        if (value == null) {
            image.setSrc("images/add-image-placeholder.png");
        }
    }

    @Subscribe(id = "clientDc", target = Target.DATA_CONTAINER)
    public void onClientDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<Client> event) {
        if ("image".equals(event.getProperty())) {
            updateImage(getEditedEntity().getImage());
        }
    }

    @Subscribe(id = "uploadClearBtn", subject = "clickListener")
    public void onUploadClearBtnClick(final ClickEvent<JmixButton> event) {
        getEditedEntity().setImage(null);
    }

}