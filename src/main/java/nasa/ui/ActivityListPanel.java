package nasa.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import java.util.logging.Logger;

import nasa.commons.core.LogsCenter;
import nasa.model.activity.Activity;
import nasa.ui.ActivityCard;

/**
 * Panel containing the list of modules.
 */
public class ActivityListPanel extends UiPart<Region> {
    private static final String FXML = "ActivityListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ActivityListPanel.class);

    @FXML
    private ListView<Activity> activityListView;

    public ActivityListPanel(ObservableList<Activity> activityList) {
        super(FXML);
        activityListView.setItems(activityList);
        activityListView.setCellFactory(listView -> new ActivityListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Module} using a {@code ModuleCard}.
     */
    class ActivityListViewCell extends ListCell<Activity> {
        @Override
        protected void updateItem(Activity activity, boolean empty) {
            super.updateItem(activity, empty);
            //Fit modules to screen
            //prefWidthProperty().bind(activityListView.widthProperty().divide(activityListView.getItems().size()));
            //minWidthProperty().set(200);
            //setMaxWidth(Control.USE_PREF_SIZE);

            if (empty || activity == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ActivityCard(activity, getIndex() + 1).getRoot());
            }
        }
    }

}