package Visao.ControllerHierarchy;

import javafx.fxml.Initializable;
import javafx.scene.layout.Region;

/**
 * Created by Gustavo Freitas on 12/10/2015.
 */
public abstract class RegionController implements Controller, Initializable {

    protected Region region = null;
    protected RegionController fatherController = null;
    protected RegionController childrenController = null;
    protected WindowController windowController = null;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public RegionController getFatherController() {
        return fatherController;
    }

    public void setFatherController(RegionController fatherController) {
        this.fatherController = fatherController;
        if(this.windowController == null && this.fatherController != null){
            this.windowController = this.fatherController.getWindowController();
        }
    }

    public RegionController getChildrenController() {
        return childrenController;
    }

    public void setChildrenController(RegionController childrenController) {
        this.childrenController = childrenController;
    }

    public WindowController getWindowController() {
        return windowController;
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }
}
