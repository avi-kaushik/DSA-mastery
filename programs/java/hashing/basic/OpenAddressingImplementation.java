package programs.java.hashing.basic;

import programs.java.hashing.common.OpenAddressingHashTable;

public class OpenAddressingImplementation {
    public static void main(String[] args) {
        OpenAddressingHashTable hash = new OpenAddressingHashTable(7);

        hash.insert(49);
        hash.insert(56);
        hash.insert(72);

        System.out.println("56 available: " + hash.search(56));

        hash.delete(56);
        
        System.out.println("56 available: " + hash.search(56));
    }
}
