package programs.java.hashing.common;

/*
 * Open Addressing Hash Table
 *
 * In Open Addressing, all keys are stored directly inside the array.
 * When a collision occurs, Linear Probing is used to find another position.
 *
 * -1 represents an empty position.
 * -2 represents a deleted position.
 * Any value >= 0 represents an occupied position.
 */
public class OpenAddressingHashTable {
    private int[] arr;
    private int cap;
    public int size;

    /*
     * Creates an empty hash table with the given capacity.
     */
    public OpenAddressingHashTable(int capacity) {
        this.cap = capacity;
        size = 0;
        arr = new int[capacity];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = -1; // -1 represents an empty position.
        }
    }

    /*
     * Returns the index for the given key using the division method.
     */
    private int hash(int key) {
        return key % cap;
    }

    /*
     * Inserts a key into the hash table.
     *
     * If the calculated position is already occupied, Linear Probing
     * is used to find the next available position.
     *
     * Both -1 (empty) and -2 (deleted) positions can be reused.
     *
     * If the complete table is checked and no position is available,
     * the method returns without inserting the key.
     */
    public void insert(int key) {
        int index = hash(key);

        int i = index;

        // Keep probing while the current position contains a key.
        while (arr[i] >= 0) {
            i = (i + 1) % cap;

            // We have checked the complete table.
            if (i == index)
                return;
        }

        // Position is either empty (-1) or deleted (-2).
        arr[i] = key;
        size++;
    }

    /*
     * Searches for the given key in the hash table.
     *
     * The same Linear Probing sequence used during insertion
     * is followed during searching.
     *
     * If -1 is found, the search can stop because the key was
     * never inserted beyond this position.
     *
     * If -2 is found, the search continues because the position
     * was previously occupied and may be part of the probe sequence.
     */
    public boolean search(int key) {
        int index = hash(key);

        int i = index;

        // Continue searching until an unused position is found.
        while (arr[i] != -1) {
            if (arr[i] == key)
                return true;

            i = (i + 1) % cap;

            // We have checked the complete table.
            if (i == index)
                return false;
        }

        return false;
    }

    /*
     * Deletes the given key from the hash table.
     *
     * The position is marked as -2 instead of -1.
     *
     * -2 is used because changing the position to -1 can break
     * the probe sequence and cause search operations to stop early.
     */
    public void delete(int key) {
        int index = hash(key);

        int i = index;

        // Continue searching until an unused position is found.
        while (arr[i] != -1) {
            if (arr[i] == key) {
                arr[i] = -2; // Mark the position as deleted.
                size--;
                return;
            }

            i = (i + 1) % cap;

            // We have checked the complete table.
            if (i == index)
                return;
        }
    }
}
