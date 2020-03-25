package nasa.logic.commands;

import static nasa.logic.commands.CommandTestUtil.assertCommandFailure;
import static nasa.logic.commands.CommandTestUtil.assertCommandSuccess;
import static nasa.testutil.TypicalIndexes.INDEX_FIRST_ACTIVITY;
import static nasa.testutil.TypicalModules.getTypicalNasaBook;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import nasa.model.Model;
import nasa.model.ModelManager;
import nasa.model.UserPrefs;
import nasa.model.module.Module;
import nasa.model.module.ModuleCode;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteModuleCommand}.
 */
public class DeleteModuleCommandTest {

    private Model model = new ModelManager(getTypicalNasaBook(), new UserPrefs());

    @Test
    public void execute_validModuleUnfilteredList_success() {
        Module moduleToDelete = model.getFilteredModuleList().get(INDEX_FIRST_ACTIVITY.getZeroBased());
        DeleteModuleCommand deleteCommand = new DeleteModuleCommand(moduleToDelete.getModuleCode());

        String expectedMessage = String.format(DeleteModuleCommand.MESSAGE_DELETE_MODULE_SUCCESS,
                moduleToDelete.getModuleCode().toString());

        ModelManager expectedModel = new ModelManager(model.getNasaBook(), new UserPrefs());
        expectedModel.deleteModule(moduleToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidModuleUnfilteredList_throwsCommandException() {
        DeleteModuleCommand deleteCommand = new DeleteModuleCommand(new ModuleCode("DAO2703"));
        assertCommandFailure(deleteCommand, model, DeleteModuleCommand.MESSAGE_FAILURE);
    }
    /*

    @Test
    public void execute_validModuleFilteredList_success() {
        showModuleAtIndex(model, INDEX_FIRST_PERSON);

        Module moduleToDelete = model.getFilteredModuleList().get(new Index(1));
        DeleteModuleCommand deleteCommand = new DeleteModuleCommand(new ModuleCode("CS2103T"));

        String expectedMessage = String.format(DeleteModuleCommand.MESSAGE_DELETE_MODULE_SUCCESS, moduleToDelete);

        Model expectedModel = new ModelManager(model.getNasaBook(), new UserPrefs());
        expectedModel.deleteModule(moduleToDelete);
        showNoModule(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showModuleAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getModuleList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

     */


    @Test
    public void execute_invalidModuleUnfilteredList_failure() {
        // a  module that does not exist
        DeleteModuleCommand deleteModuleCommand = new DeleteModuleCommand(new ModuleCode("MA1521"));
        assertCommandFailure(deleteModuleCommand, model, DeleteModuleCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        DeleteModuleCommand deleteFirstCommand = new DeleteModuleCommand(new ModuleCode("CS2103T"));
        DeleteModuleCommand deleteSecondCommand = new DeleteModuleCommand(new ModuleCode("CS2101"));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteModuleCommand deleteFirstCommandCopy = new DeleteModuleCommand(new ModuleCode("CS2103T"));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoModule(Model model) {
        model.updateFilteredModuleList(p -> false);

        assertTrue(model.getFilteredModuleList().isEmpty());
    }

}
