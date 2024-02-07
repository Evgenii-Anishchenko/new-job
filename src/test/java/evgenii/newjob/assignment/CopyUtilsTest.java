package evgenii.newjob.assignment;


import evgenii.newjob.deepclone.CopyUtils;
import evgenii.newjob.deepclone.model.Man;
import evgenii.newjob.deepclone.model.NestedList;
import evgenii.newjob.deepclone.model.RecursiveNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class CopyUtilsTest {

    @Test
    void deepCopy() {
        String expectedName = "John";
        int expectedAge = 25;
        List<String> expectedFavoriteBooks = List.of("1984", "To Kill a Mockingbird");

        Man originalMan = new Man(expectedName, expectedAge, expectedFavoriteBooks);

        Man copiedMan = CopyUtils.deepCopy(originalMan);

        assertNotSame(originalMan, copiedMan);
        assertEquals(originalMan.getName(), copiedMan.getName());
        assertEquals(originalMan.getAge(), copiedMan.getAge());
        assertEquals(originalMan.getFavoriteBooks(), copiedMan.getFavoriteBooks());

        List<String> modifiedFavoriteBooks = List.of("War and Peace", "The Great Gatsby");
        originalMan.setFavoriteBooks(modifiedFavoriteBooks);

        assertNotEquals(originalMan.getFavoriteBooks(), copiedMan.getFavoriteBooks());
    }

    @Test
    void deepCopyRecursiveNode() {
        RecursiveNode originalNode = new RecursiveNode("id1");
        RecursiveNode childNode = new RecursiveNode("id2");
        originalNode.addChild(childNode);

        RecursiveNode copiedNode = CopyUtils.deepCopy(originalNode);

        assertNotSame(originalNode, copiedNode);
        assertFalse(copiedNode.getChildren().isEmpty());
        assertNotSame(originalNode.getChildren().get(0), copiedNode.getChildren().get(0));
        assertEquals(originalNode.getChildren().get(0).getValue(), copiedNode.getChildren().get(0).getValue());
    }

    @Test
    void testDeepCopyNestedList() {
        // Create a new NestedList instance and add some lists
        NestedList originalNestedList = new NestedList();
        ArrayList<Integer> sublist1 = new ArrayList<>();
        sublist1.add(1);
        sublist1.add(2);
        sublist1.add(3);
        originalNestedList.addList(sublist1);

        ArrayList<String> sublist2 = new ArrayList<>();
        sublist2.add("a");
        sublist2.add("b");
        sublist2.add("c");
        originalNestedList.addList(sublist2);

        NestedList copiedNestedList = CopyUtils.deepCopy(originalNestedList);

        // Assert that the original and copied lists are not the same object
        assertNotSame(originalNestedList, copiedNestedList);

        // Assert that the lists inside original and copied list are not same
        for (int i = 0; i < originalNestedList.getList().size(); i++) {
            assertNotSame(originalNestedList.getList().get(i), copiedNestedList.getList().get(i));
        }

        // Assert the data in original and copied lists are same but not the same reference
        assertEquals(originalNestedList.getList(), copiedNestedList.getList());

        // If we modify original, it shouldn't affect the copy
        ((List<Integer>) originalNestedList.getList().getFirst()).add(4);
        assertNotEquals(originalNestedList.getList(), copiedNestedList.getList());
    }
}