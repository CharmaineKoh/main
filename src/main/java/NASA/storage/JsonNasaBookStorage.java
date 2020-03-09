package NASA.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import NASA.commons.core.LogsCenter;
import NASA.commons.exceptions.DataConversionException;
import NASA.commons.exceptions.IllegalValueException;
import NASA.commons.util.FileUtil;
import NASA.commons.util.JsonUtil;
import NASA.model.ReadOnlyNasaBook;
import NASA.storage.NasaBookStorage;
import NASA.storage.JsonSerializableNasaBook;

/**
 * A class to access NasaBook data stored as a json file on the hard disk.
 */
public class JsonNasaBookStorage implements NasaBookStorage {

    private static final Logger logger = LogsCenter.getLogger(NASA.storage.JsonNasaBookStorage.class);

    private Path filePath;

    public JsonNasaBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getNasaBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyNasaBook> readNasaBook() throws DataConversionException {
        return readNasaBook(filePath);
    }

    /**
     * Similar to {@link #readNasaBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyNasaBook> readNasaBook(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableNasaBook> jsonNasaBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableNasaBook.class);
        if (!jsonNasaBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonNasaBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveNasaBook(ReadOnlyNasaBook NasaBook) throws IOException {
        saveNasaBook(NasaBook, filePath);
    }

    /**
     * Similar to {@link #saveNasaBook(ReadOnlyNasaBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveNasaBook(ReadOnlyNasaBook NasaBook, Path filePath) throws IOException {
        requireNonNull(NasaBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableNasaBook(NasaBook), filePath);
    }

}
