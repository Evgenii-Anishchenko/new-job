package evgenii.newjob.assignment;


import evgenii.newjob.deepclone.CopyUtils;
import evgenii.newjob.deepclone.model.Man;
import evgenii.newjob.deepclone.model.NestedCollection;
import evgenii.newjob.deepclone.model.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class CopyUtilsTest {

    @Test
    void testDeepCopyMan() {
        Man original = new Man("John Doe", 30, new ArrayList<>(Arrays.asList("Moby Dick", "War and Peace")));
        Man copied = CopyUtils.deepCopy(original);

        // Assertions to verify the deep copy
        assertNotSame(original, copied, "The copied object should not be the same as the original object.");
        assertEquals(original.getName(), copied.getName(), "Names should be equal.");
        assertEquals(original.getAge(), copied.getAge(), "Ages should be equal.");
        assertNotSame(original.getFavoriteBooks(), copied.getFavoriteBooks(), "The list of favorite books should not be the same object.");
        assertEquals(original.getFavoriteBooks(), copied.getFavoriteBooks(), "The contents of the list of favorite books should be equal.");

        // Modify original object's list
        original.getFavoriteBooks().add("Pride and Prejudice");

        // Verify copied object's list remains unchanged
        assertFalse(copied.getFavoriteBooks().contains("Pride and Prejudice"), "The copied object's list should not reflect changes to the original object's list.");
    }

    @Test
    void testDeepCopyWithCyclicDependency() {
        // Create the root node
        TreeNode root = new TreeNode("root");

        // Create a child node
        TreeNode child = new TreeNode("child");

        // Add child to root
        root.addChild(child);

        // Create cyclic dependency: child node has a reference back to the root
        child.addChild(root);

        // Perform deep copy
        TreeNode copiedRoot = CopyUtils.deepCopy(root);

        // Assertions to verify the deep copy
        assertNotNull(copiedRoot);
        assertEquals(root.getValue(), copiedRoot.getValue());
        assertEquals(root.getChildren().size(), copiedRoot.getChildren().size());
        assertEquals(root.getChildren().getFirst().getValue(), copiedRoot.getChildren().getFirst().getValue());

        // Verify that the cyclic dependency is preserved in the copy
        TreeNode copiedChild = copiedRoot.getChildren().getFirst();
        assertNotSame(root, copiedRoot); // Ensure root is a deep copy
        assertNotSame(child, copiedChild); // Ensure child is a deep copy

        // Verify the copied structure does not reference the original structure
        assertNotSame(root.getChildren().getFirst(), copiedRoot.getChildren().getFirst());
        // Verify cyclic dependency in the copied structure
        assertSame(copiedRoot, copiedRoot.getChildren().getFirst().getChildren().getFirst());
    }

    @Test
    void testDeepCopyNestedCollection() {
        NestedCollection original = new NestedCollection();
        original.getCollectionList().add(Arrays.asList("One", "Two", "Three"));
        original.getCollectionList().add(new HashSet<>(Arrays.asList(1, 2, 3)));
        original.getCollectionMap().put("List", new ArrayList<>(Arrays.asList("A", "B", "C")));
        original.getCollectionMap().put("Set", new HashSet<>(Arrays.asList(4, 5, 6)));

        NestedCollection copied = CopyUtils.deepCopy(original);

        // Assertions
        assertNotSame(original, copied);
        assertNotSame(original.getCollectionList(), copied.getCollectionList());
        assertNotSame(original.getCollectionMap(), copied.getCollectionMap());
        assertEquals(original.getCollectionList(), copied.getCollectionList());
        assertEquals(original.getCollectionMap(), copied.getCollectionMap());
    }
}