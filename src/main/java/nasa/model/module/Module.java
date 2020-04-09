package nasa.model.module;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import nasa.commons.core.index.Index;
import nasa.model.activity.Activity;
import nasa.model.activity.Deadline;
import nasa.model.activity.Event;
import nasa.model.activity.UniqueDeadlineList;
import nasa.model.activity.UniqueEventList;

/**
 * Abstract class to specify fields with getter and setters for modules.
 */
public class Module {

    private ModuleCode moduleCode;
    private UniqueEventList eventList;
    private FilteredList<Event> filteredEvent;
    private UniqueDeadlineList deadlineList;
    private FilteredList<Deadline> filteredDeadline;
    private ModuleName moduleName;

    /**
     * Constructs a {@code module}
     * @param moduleCode ModuleCode
     * @param moduleName ModuleName
     */
    public Module(ModuleCode moduleCode, ModuleName moduleName) {
        this.moduleCode = moduleCode;
        this.eventList = new UniqueEventList();
        this.deadlineList = new UniqueDeadlineList();
        this.filteredEvent = new FilteredList<>(eventList.getActivityList());
        this.filteredDeadline = new FilteredList<>(deadlineList.getActivityList());
        this.moduleName = moduleName;
    }

    /**
     * Retrieve the moduleCode of the module.
     * @return String moduleCode
     */
    public ModuleCode getModuleCode() {
        return moduleCode;
    }

    /**
     * Sets the module moduleCode to a new moduleCode.
     * Used for editing module code.
     * @param moduleCode of the module
     */
    public void setModuleCode(ModuleCode moduleCode) {
        this.moduleCode = moduleCode;
    }

    public void addDeadline(Deadline deadline) {
        deadlineList.add(deadline);
    }

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public ModuleName getModuleName() {
        return moduleName;
    }

    public void removeDeadline(Deadline toRemove) {
        deadlineList.remove(toRemove);
    }

    public void removeEvent(Event toRemove) {
        eventList.remove(toRemove);
    }

    public UniqueDeadlineList getDeadlineList() {
        return deadlineList;
    }

    public UniqueEventList getEventList() {
        return eventList;
    }

    public void setDeadlines(List<Deadline> deadlines) {
        deadlineList.setActivities(deadlines);
    }

    public void setDeadlines(UniqueDeadlineList replacement) {
        deadlineList.setActivities(replacement);
    }

    public void setEvents(UniqueEventList replacement) {
        eventList.setActivities(replacement);
    }

    public void setEvents(List<Event> events) {
        eventList.setActivities(events);
    }

    public void setDeadlineSchedule(Index index, Index type) {
        deadlineList.setSchedule(index, type);
        updateFilteredActivityList(x -> true);
    }

    public void setEventSchedule(Index index, Index type) {
        eventList.setSchedule(index, type);
        updateFilteredActivityList(x -> true);
    }

    public ObservableList<Deadline> getFilteredDeadlineList() {
        return filteredDeadline;
    }

    public ObservableList<Event> getFilteredEventList() {
        return filteredEvent;
    }

    public ObservableList<Activity> getDeepCopyDeadlineList() {
        return deadlineList.getDeepCopyList();
    }

    public ObservableList<Deadline> getModifiableDeadlineList() {
        return deadlineList.getActivityList();
    }

    public ObservableList<Activity> getDeepCopyEventList() {
        return eventList.getDeepCopyList();
    }

    public ObservableList<Event> getModifiableEventList() {
        return eventList.getActivityList();
    }

    public Module getDeepCopyModule() {
        Module newModule = new Module(getModuleCode(), getModuleName());
        ObservableList<Activity> deadlines = deadlineList.getDeepCopyList();
        ObservableList<Deadline> deadlinesCopy = FXCollections.observableArrayList();
        for (Activity activity : deadlines) {
            deadlinesCopy.add((Deadline) activity);
        }
        newModule.setDeadlines(deadlinesCopy);
        ObservableList<Activity> events = eventList.getDeepCopyList();
        ObservableList<Event> eventsCopy = FXCollections.observableArrayList();
        for (Activity activity : events) {
            eventsCopy.add((Event) activity);
        }
        newModule.setEvents(eventsCopy);
        return newModule;
    }

    public Iterator<Deadline> deadlineIterator() {
        return deadlineList.iterator();
    }

    public Iterator<Event>eventIterator() {
        return eventList.iterator();
    }

    /**
     * Update filtered activity list.
     * @param predicate Predicate
     */
    public void updateFilteredActivityList(Predicate<Activity> predicate) {
        filteredDeadline.setPredicate(predicate);
        filteredEvent.setPredicate(predicate);
    }

    /**
     * Sorts module's deadline list by the specified {@code sortMethod}.
     * @param sortMethod Method of sorting the activities in the module deadline list.
     */
    public void sortDeadlineList(SortMethod sortMethod) {
        Comparator<Deadline> comparator = sortMethod.getComparator();
        this.deadlineList.getActivityList().sort(comparator);
    }

    /**
     * Returns true if both are the same module with same module name and module code.
     * This defines a stronger notion of equality between two modules.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Module)) {
            return false;
        }

        Module otherModule = (Module) other;
        return otherModule.getModuleCode().equals(getModuleCode()) && otherModule.getModuleName()
                .equals(getModuleName());
    }

    @Override
    public String toString() {
        return String.format("%s %s", moduleCode, moduleName);
    }

    /**
     * Check whether module has the deadline.
     * @param deadline Deadline
     * @return boolean
     */
    public boolean hasDeadline(Deadline deadline) {
        for (Deadline currentDeadline : deadlineList.getActivityList()) {
            if (currentDeadline == deadline) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether module has the event.
     * @param event Event
     * @return boolean
     */
    public boolean hasEvent(Event event) {
        for (Event currentEvent : eventList.getActivityList()) {
            if (currentEvent == event) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether module has activity.
     * @param activity Activity
     * @return boolean
     */
    public boolean hasActivity(Activity activity) {
        if (activity instanceof Deadline) {
            return hasDeadline((Deadline) activity);
        } else {
            return hasEvent((Event) activity);
        }
    }
}
