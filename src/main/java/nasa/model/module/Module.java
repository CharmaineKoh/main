package nasa.model.module;

import java.util.Iterator;
import java.util.List;

import nasa.model.activity.Activity;
import nasa.model.activity.UniqueActivityList;

/**
 * Abstract class to specify fields with getter and setters for modules.
 */
public class Module {

    private ModuleCode moduleCode;
    private UniqueActivityList activityList;
    private ModuleName moduleName;

    /**
     * Constructs a {@code module} and initialises the activity list.
     * @param moduleCode module code
     */
    public Module(ModuleCode moduleCode, ModuleName moduleName) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.activityList = new UniqueActivityList();
    }

    // to create dummy modules for checking
    public Module(ModuleCode moduleCode) {
        this.moduleCode = moduleCode;
    }

    /**
     * Constructs a {@code module}
     * @param moduleCode module code
     */
    public Module(ModuleCode moduleCode, ModuleName moduleName, UniqueActivityList uniqueActivityList) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.activityList = uniqueActivityList;
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

    public boolean contains(Activity activity) {
        return activityList.contains(activity);
    }

    public void add(Activity toAdd) {
        activityList.add(toAdd);
    }

    public void setActivity(Activity target, Activity editedActivity) {
        activityList.setActivity(target, editedActivity);
    }

    public void remove(Activity toRemove) {
        activityList.remove(toRemove);
    }

    public UniqueActivityList getActivities() {
        return activityList;
    }

    public void setActivities(UniqueActivityList replacement) {
        activityList.setActivities(replacement);
    }

    /**
     * Replaces the contents of this list with {@code activities}
     * {@code activities} must not contain duplicate activities.
     */
    public void setActivities(List<Activity> activities) {
        activityList.setActivities(activities);
    }

    public Iterator<Activity> iterator() {
        return activityList.iterator();
    }

    /**
     * Returns true if both are the same module.
     * This defines a stronger notion of equality between two activities.
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
        return otherModule.getModuleCode().equals(getModuleCode());
    }

    public ModuleName getModuleName() {
        return moduleName;
    }

    public void setModuleName(ModuleName moduleName) {
        this.moduleName = moduleName;
    }
}
