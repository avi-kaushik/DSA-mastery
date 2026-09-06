package programs.java.hashing.common;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Hash table implementation using chaining for collision handling.
 *
 * Chaining stores multiple keys that map to the same bucket
 * inside a LinkedList.
 *
 * Average time complexity:
 * Insert: O(1)
 * Search: O(1)
 * Delete: O(1)
 *
 * Worst case:
 * Insert: O(n)
 * Search: O(n)
 * Delete: O(n)
 */
public class ChainedHashTable {

    // Number of buckets in the hash table.
    private int bucket;

    // Each index represents a bucket.
    // Multiple keys can exist in the same bucket using a LinkedList.
    private ArrayList<LinkedList<Integer>> items;

    /**
     * Creates a hash table with the given number of buckets.
     *
     * @param bucket number of buckets
     */
    public ChainedHashTable(int bucket) {
        this.bucket = bucket;
        items = new ArrayList<>();

        // Create an empty LinkedList for each bucket.
        for (int index = 0; index < bucket; index++) {
            items.add(new LinkedList<>());
        }
    }

    /**
     * Calculates the bucket index for a given key.
     *
     * @param key key whose bucket index needs to be calculated
     * @return bucket index
     */
    private int hashIndex(int key) {
        return key % bucket;
    }

    /**
     * Inserts a key into the hash table.
     *
     * If the key already exists, it will not be inserted again.
     *
     * @param key key to insert
     */
    public void insert(int key) {
        LinkedList<Integer> chain = items.get(hashIndex(key));

        if (!chain.contains(key)) {
            chain.add(key);
        }
    }

    /**
     * Searches for a key in the hash table.
     *
     * @param key key to search
     * @return true if key exists, otherwise false
     */
    public boolean search(int key) {
        return items.get(hashIndex(key)).contains(key);
    }

    /**
     * Deletes a key from the hash table.
     *
     * If the key does not exist, nothing happens.
     *
     * @param key key to delete
     */
    public void delete(int key) {
        items.get(hashIndex(key)).remove((Integer) key);
    }
}
