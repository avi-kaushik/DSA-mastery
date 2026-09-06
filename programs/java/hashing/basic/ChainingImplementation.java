package programs.java.hashing.basic;

import programs.java.hashing.common.ChainedHashTable;

public class ChainingImplementation {

    public static void main(String[] args) {
        ChainedHashTable hash = new ChainedHashTable(7);

        hash.insert(45);
        hash.insert(13);
        hash.insert(28);

        System.out.println("Search 13: " + hash.search(13));

        hash.delete(13);
        System.out.println("Deleted 13");

        System.out.println("Search 13: " + hash.search(13));

        System.out.println("Search 41: " + hash.search(41));

        System.out.println("Search 45: " + hash.search(45));
    }
}
