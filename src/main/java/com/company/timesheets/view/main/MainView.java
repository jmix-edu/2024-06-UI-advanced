package com.company.timesheets.view.main;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route("")
@RouteAlias("home")
@RouteAlias("main")
@ViewController("ts_MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {
}
