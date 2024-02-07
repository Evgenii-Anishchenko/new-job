package evgenii.newjob.deepclone;

import evgenii.newjob.deepclone.model.Man;
import evgenii.newjob.deepclone.model.NestedCollection;
import evgenii.newjob.deepclone.model.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class CopyUtilsDemo {

    public static void main(String[] args) {
        System.out.println(" \n\n ******* DEEP COPY SIMPLE CLASS \n\n ");
        simpleObjectDemo();
        System.out.println(" \n\n ******* DEEP COPY OF CYCLIC DEPENDENCY \n\n ");
        cyclicDependencyDemo();
        System.out.println(" \n\n ******* DEEP COPY OF NESTED COLLECTIONS \n\n ");
        nestedCollectionDemo();
        System.out.println(" \n\n ******* THE END OF THE DEMO ******* \n\n ");
    }

    private static void simpleObjectDemo() {
        Man original = new Man("John Doe", 30, new ArrayList<>(Arrays.asList("Moby Dick", "War and Peace")));
        System.out.println("Original Man created: " + original.getName() + ", Age: " + original.getAge() + ", Favorite Books: " + original.getFavoriteBooks());

        Man copied = CopyUtils.deepCopy(original);
        System.out.println("Deep copy of Man performed.");

        // Displaying the copied Man details
        System.out.println("Copied Man details: " + copied.getName() + ", Age: " + copied.getAge() + ", Favorite Books: " + copied.getFavoriteBooks());

        // Modifying the original Man's favorite books
        original.getFavoriteBooks().add("Pride and Prejudice");
        System.out.println("After modifying the original Man's favorite books.");

        // Showing that the copied Man's favorite books list remains unchanged
        System.out.println("Original Man's favorite books: " + original.getFavoriteBooks());
        System.out.println("Copied Man's favorite books (unchanged): " + copied.getFavoriteBooks());
    }

    private static void cyclicDependencyDemo() {
        System.out.println("Creating a TreeNode structure with cyclic dependencies...");

        // Create the root node
        TreeNode root = new TreeNode("root");
        System.out.println("Created root node with value: " + root.getValue());

        // Create a child node
        TreeNode child = new TreeNode("child");
        System.out.println("Created child node with value: " + child.getValue());

        // Add child to root
        root.addChild(child);
        System.out.println("Added child to root.");

        // Create cyclic dependency: child node has a reference back to the root
        child.addChild(root);
        System.out.println("Added root as a child to child to create a cyclic dependency.");

        // Perform deep copy
        System.out.println("Performing deep copy of the root node...");
        TreeNode copiedRoot = CopyUtils.deepCopy(root);

        // Verifying and displaying the results
        System.out.println("Verifying and displaying the results:");
        System.out.println("Root node value: " + root.getValue() + " | Copied root node value: " + copiedRoot.getValue());
        System.out.println("Child node value: " + root.getChildren().getFirst().getValue() + " | Copied child node value: " + copiedRoot.getChildren().getFirst().getValue());

        // Verify that the cyclic dependency is preserved in the copy
        boolean cyclicDependencyPreserved = copiedRoot == copiedRoot.getChildren().getFirst().getChildren().getFirst();
        System.out.println("Cyclic dependency preserved in the copy: " + cyclicDependencyPreserved);

        // Ensure the copied structure does not reference the original structure
        boolean isDeepCopy = copiedRoot != root && copiedRoot.getChildren().getFirst() != root.getChildren().getFirst();
        System.out.println("The copied structure is independent of the original (deep copy): " + isDeepCopy);
    }

    public static void nestedCollectionDemo() {
        NestedCollection original = new NestedCollection();
        original.getCollectionList().add(Arrays.asList("One", "Two", "Three"));
        original.getCollectionList().add(new HashSet<>(Arrays.asList(1, 2, 3)));
        original.getCollectionMap().put("List", new ArrayList<>(Arrays.asList("A", "B", "C")));
        original.getCollectionMap().put("Set", new HashSet<>(Arrays.asList(4, 5, 6)));

        System.out.println("Performing deep copy of NestedCollection...");

        NestedCollection copied = CopyUtils.deepCopy(original);

        // Verifying and logging the deep copy results
        System.out.println("Verification:");
        System.out.println("Original and copied objects are distinct: " + (original != copied));
        System.out.println("Original and copied lists are distinct: " + (original.getCollectionList() != copied.getCollectionList()));
        System.out.println("Original and copied maps are distinct: " + (original.getCollectionMap() != copied.getCollectionMap()));

        System.out.println("Original list content equals copied list content: " + original.getCollectionList().equals(copied.getCollectionList()));
        System.out.println("Original map content equals copied map content: " + original.getCollectionMap().equals(copied.getCollectionMap()));

        // Modifying the original to show independence of the copied object
        original.getCollectionList().add(Arrays.asList("New Entry"));
        original.getCollectionMap().put("NewList", new ArrayList<>(Arrays.asList("X", "Y", "Z")));

        System.out.println("After modifying the original:");
        System.out.println("Original list size: " + original.getCollectionList().size());
        System.out.println("Copied list size (should be unchanged): " + copied.getCollectionList().size());
        System.out.println("Original map size: " + original.getCollectionMap().size());
        System.out.println("Copied map size (should be unchanged): " + copied.getCollectionMap().size());
    }


}