package nasa.logic;

import static nasa.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static nasa.logic.commands.CommandTestUtil.MODULE_CODE_DESC_CS2030;
import static nasa.logic.commands.CommandTestUtil.MODULE_NAME_DESC_CS2030;
import static nasa.logic.commands.CommandTestUtil.VALID_MODULE_CODE_CS2030;
import static nasa.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import nasa.logic.commands.AddModuleCommand;
import nasa.logic.commands.CommandResult;
import nasa.logic.commands.ListCommand;
import nasa.logic.commands.exceptions.CommandException;
import nasa.logic.parser.exceptions.ParseException;
import nasa.model.Model;
import nasa.model.ModelManager;
import nasa.model.ReadOnlyNasaBook;
import nasa.model.UserPrefs;
import nasa.model.module.Module;
import nasa.storage.JsonNasaBookStorage;
import nasa.storage.JsonUserPrefsStorage;
import nasa.storage.StorageManager;
import nasa.testutil.ModuleBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonNasaBookStorage nasaBookStorage =
                new JsonNasaBookStorage(temporaryFolder.resolve("nasaBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(nasaBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    /*
    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
    */

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonNasaBookIoExceptionThrowingStub
        JsonNasaBookStorage nasaBookStorage =
                new JsonNasaBookIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionNasaBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(nasaBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add command

        String addCommand = AddModuleCommand.COMMAND_WORD + MODULE_CODE_DESC_CS2030 + MODULE_NAME_DESC_CS2030;
        Module expectedModule = new ModuleBuilder().withCode(VALID_MODULE_CODE_CS2030).build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addModule(expectedModule);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredModuleList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredModuleList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getNasaBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonNasaBookIoExceptionThrowingStub extends JsonNasaBookStorage {
        private JsonNasaBookIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveNasaBook(ReadOnlyNasaBook nasaBook, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
