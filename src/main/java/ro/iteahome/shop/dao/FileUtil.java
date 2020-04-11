package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.model.WritableToDatabase;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles the concrete read/write operations to databases (.txt files). It provides various methods for
 * extracting general and specific information from said databases.
 */
class FileUtil<Entry extends WritableToDatabase> {

    /**
     * {@code getAllEntries()} returns an ArrayList of all the entries (as objects) of a specified database. For
     * implementation, it requires:
     * <p>
     * - a concrete database path ({@code String});
     * - a method to construct entries from the database lines (more specifically, from the array of line tokens
     * representing the entry properties).
     */
    ArrayList<Entry> getAllEntries(String databasePath, Function<String[], Entry> constructEntry) throws ShopEntryNotFoundException {
        ArrayList<Entry> fileEntries = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(databasePath))) {

            while (fileScanner.hasNextLine()) {
                String entryLine = fileScanner.nextLine();
                String[] entryProperties = entryLine.split("\\|");
                if (entryLine.length() != 0) {
                    Entry entry = constructEntry.apply(entryProperties);
                    fileEntries.add(entry);
                }
            }

        } catch (FileNotFoundException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
        if (fileEntries.isEmpty()) {
            throw new ShopEntryNotFoundException();
        } else {
            return fileEntries;
        }
    }

    /**
     * {@code findFirstEntryByProperty()} returns an Entry object by checking for a value within a given property for
     * every database line. More specifically, it returns the first object it finds. If the the targeted property is a
     * unique identifier for that database's Entries, this method returns a very specific object.
     * e.g. ProductDAO's {@code findProductByID()} method.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - the index ({@code int}) of the targeted property within the array of line properties;
     * - the value ({@code String}) to be compared to the property;
     * - a method to construct Entry objects from the database text lines (more specifically, from the array of line
     * tokens representing the entry properties).
     */
    Entry findFirstEntryByProperty(String databasePath, int propertyIndex, String value, Function<String[], Entry> constructEntry) throws ShopEntryNotFoundException {
        Entry entry = null;
        try (Scanner fileScanner = new Scanner(new File(databasePath))) {

            while (fileScanner.hasNextLine()) {
                String entryLine = fileScanner.nextLine();
                String[] entryProperties = entryLine.split("\\|");
                String targetProperty = entryProperties[propertyIndex];
                if (entryLine.length() != 0 && targetProperty.matches(value)) {
                    entry = constructEntry.apply(entryProperties);
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
        if (entry == null) {
            throw new ShopEntryNotFoundException();
        } else {
            return entry;
        }
    }

    /**
     * {@code findEntriesByProperty()} returns an ArrayList of Entry objects by checking for a value within a given
     * property for every database line. More specifically, it returns all the objects that have the same value for
     * said property.
     * e.g. ProductDAO's {@code findProductsByCategory()} method.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - the index ({@code int}) of the targeted property within the array of line properties;
     * - the value ({@code String}) to be compared to the property;
     * - a method to construct Entry objects from the database text lines (more specifically, from the array of line
     * tokens representing the entry properties).
     */
    ArrayList<Entry> findEntriesByProperty(String databasePath, int propertyIndex, String value, Function<String[], Entry> arrayToEntry) throws ShopEntryNotFoundException {
        ArrayList<Entry> entries = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(databasePath))) {

            while (fileScanner.hasNextLine()) {
                String entryLine = fileScanner.nextLine();
                String[] entryProperties = entryLine.split("\\|");
                String targetProperty = entryProperties[propertyIndex];
                if (entryLine.length() != 0 && targetProperty.matches(value)) {
                    entries.add(arrayToEntry.apply(entryProperties));
                }
            }

        } catch (FileNotFoundException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
        if (entries.isEmpty()) {
            throw new ShopEntryNotFoundException();
        } else {
            return entries;
        }
    }

    /**
     * {@code findPossibleEntries()} returns an ArrayList of Entry objects by checking for a pattern within a given
     * property for every database line. More specifically, it returns all the objects that contain the pattern within
     * their value of said property.
     * e.g. ProductDAO's {@code findPossibleProductsByCategory()} method, which returns all Entries of a category if the
     * pattern is found within the category name.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - the index ({@code int}) of the targeted property within the array of line properties;
     * - the pattern ({@code String}) to be searched for within the property;
     * - a method to construct Entry objects from the database text lines (more specifically, from the array of line
     * tokens representing the entry properties).
     */
    ArrayList<Entry> findPossibleEntries(String databasePath, int propertyIndex, String pattern, Function<String[], Entry> arrayToEntry) throws ShopEntryNotFoundException {
        ArrayList<Entry> targetEntries = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(databasePath))) {

            while (fileScanner.hasNextLine()) {
                String entryLine = fileScanner.nextLine();
                String[] entryProperties = entryLine.split("\\|");
                String targetProperty = entryProperties[propertyIndex].toLowerCase();

                Pattern searchPattern = Pattern.compile(pattern.toLowerCase());
                Matcher matcher = searchPattern.matcher(targetProperty);

                if (matcher.find()) {
                    Entry entry = arrayToEntry.apply(entryProperties);
                    targetEntries.add(entry);
                }
            }

        } catch (FileNotFoundException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
        if (targetEntries.isEmpty()) {
            throw new ShopEntryNotFoundException();
        } else {
            return targetEntries;
        }
    }

    /**
     * {@code addNewEntry()} adds a new line of text to a given database. The line of text is comprised of the given
     * Entry object's properties, written as tokens separated by a delimiter. This conversion is handled by the
     * {@code WritableToDatabase} class' {@code toDatabaseString()} method. Before writing the entry to the database,
     * its ID is set as the increment of the corresponding ID sequence.
     * After writing the line, the database's corresponding ID sequence file is updated.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - a database sequence path ({@code String});
     * - an Entry object, to be converted into a string that will be written to the database.
     */
    void addNewEntry(String databasePath, String databaseSequencePath, Entry entry) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(databasePath, true))) {

            fileWriter.append(entry.toDatabaseString());
            fileWriter.newLine();
            updateSequence(databaseSequencePath, entry.getID());

        } catch (IOException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
    }

    /**
     * {@code updateSequence()} replaces the value found in a specified ID sequence file with its incremented value.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database sequence path ({@code String});
     */
    private void updateSequence(String databaseSequencePath, int newID) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(databaseSequencePath, false))) {

            fileWriter.append(String.valueOf(newID));

        } catch (IOException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
    }

    /**
     * {@code getSequence()} returns the value found in a specified ID sequence file.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database sequence path ({@code String});
     */
    int getSequence(String databaseSequencePath) throws ShopEntryNotFoundException {
        int sequence = -1;
        try (Scanner fileScanner = new Scanner(new File(databaseSequencePath))) {

            sequence = fileScanner.nextInt();

        } catch (FileNotFoundException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
        if (sequence == -1) {
            throw new ShopEntryNotFoundException("DATABASE SEQUENCE NOT ACCESSIBLE.");
        } else {
            return sequence;
        }
    }

    /**
     * {@code updateEntry()} replaces a line of text within a given database. It does not alter the ID sequence.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - a target Entry object, to identify the corresponding line within the database;
     * - a new Entry object, to be converted into a string that will be written to the database.
     * - a method to construct Entry objects from the database text lines (more specifically, from the array of line
     * tokens representing the entry properties).
     */
    void updateEntry(String databasePath, Entry targetEntry, Entry newEntry, Function<String[], Entry> arrayToEntry) throws ShopEntryNotFoundException {
        ArrayList<Entry> fileEntries = getAllEntries(databasePath, arrayToEntry);
        boolean entryFound = false;
        for (Entry entry : fileEntries) {
            if (entry.getID() == targetEntry.getID()) {
                fileEntries.set(fileEntries.indexOf(entry), newEntry);
                overwriteDatabase(databasePath, fileEntries);
                entryFound = true;
                break;
            }
        }
        if (!entryFound) {
            throw new ShopEntryNotFoundException();
        }
    }

    /**
     * {@code removeEntry()} removes a specific line of text from a given database. The line of text is found by
     * matching an identifying property of the targeted entry to the corresponding token of the database line. This
     * method does not leave empty spaces within the database, nor does it alter the ID sequence.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - an Entry object, to be converted into a string that will be written to tha database.
     * - a method to construct Entry objects from the database text lines (more specifically, from the array of line
     * tokens representing the entry properties).
     */
    void removeEntry(String databasePath, Entry targetEntry, Function<String[], Entry> arrayToEntry) throws ShopEntryNotFoundException {
        ArrayList<Entry> fileEntries = getAllEntries(databasePath, arrayToEntry);
        boolean entryFound = false;
        for (Entry entry : fileEntries) {
            if (entry.getID() == targetEntry.getID()) {
                fileEntries.remove(entry);
                overwriteDatabase(databasePath, fileEntries);
                entryFound = true;
                break;
            }
        }
        if (!entryFound) {
            throw new ShopEntryNotFoundException();
        }
    }

    /**
     * {@code getSortedPropertyValues()} returns an ArrayList of all values of a given property, ignoring duplicates.
     * e.g. ProductDAO's {@code getProductCategories()} method.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - the index ({@code int}) of the targeted property within the array of line properties;
     */
    ArrayList<String> getSortedPropertyValues(String databasePath, int propertyIndex) throws ShopEntryNotFoundException {
        ArrayList<String> values = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(databasePath))) {

            HashSet<String> valueSet = new HashSet<>();
            while (fileScanner.hasNextLine()) {
                String entryLine = fileScanner.nextLine();
                String[] entryProperties = entryLine.split("\\|");
                String targetProperty = entryProperties[propertyIndex];
                valueSet.add(targetProperty);
            }
            values.addAll(valueSet);
            Collections.sort(values);

        } catch (FileNotFoundException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
        if (values.isEmpty()) {
            throw new ShopEntryNotFoundException();
        } else {
            return values;
        }
    }

    /**
     * {@code overwriteDatabase()} overwrites the content of a given database with that of an ArrayList of Entry
     * objects.
     * <p>
     * For implementation, it requires:
     * <p>
     * - a database path ({@code String});
     * - an ArrayList of Entries to become the new content.
     */
    private void overwriteDatabase(String databasePath, ArrayList<Entry> entries) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(databasePath, false))) {

            StringBuilder entriesBuilder = new StringBuilder();
            for (Entry entry : entries) {
                entriesBuilder.append(entry.toDatabaseString()).append(System.lineSeparator());
            }
            fileWriter.write(entriesBuilder.toString());

        } catch (IOException e) {
            OutputFrame.printAlert("DATABASE NOT ACCESSIBLE.");
        }
    }
}