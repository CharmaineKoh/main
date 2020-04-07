package nasa.ui.event;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import nasa.commons.core.LogsCenter;
import nasa.model.event.Event;
import nasa.model.event.Deadline;
import nasa.model.event.Event;
import nasa.model.event.Lesson;
import nasa.ui.UiPart;

/**
 * Panel containing the list of modules.
 */
public class EventListPanel extends UiPart<Region> {
    private static final String FXML = "EventListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);

    @FXML
    private ListView<Event> eventListView;

    public EventListPanel(ObservableList<Event> eventList) {
        super(FXML);
        eventListView.setItems(eventList);
        eventListView.setCellFactory(listView -> new EventListViewCell());
    }

    public void setWidth(double width) {
        eventListView.setPrefWidth(width);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Module} using a {@code ModuleCard}.
     */
    class EventListViewCell extends ListCell<Event> {
        @Override
        protected void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);
            prefWidthProperty().bind(eventListView.widthProperty().subtract(10));

            if (empty || event == null) {
                setStyle("");
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new EventCard(event, getIndex() + 1).getRoot());
                setStyle("-fx-background-color: #aee4ff;");
            }
        }
    }
}
