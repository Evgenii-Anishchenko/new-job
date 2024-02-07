package evgenii.newjob.deepclone;

import evgenii.newjob.deepclone.model.Man;
import evgenii.newjob.deepclone.model.NestedList;
import evgenii.newjob.deepclone.model.RecursiveNode;

import java.util.ArrayList;
import java.util.Arrays;

public class CopyUtilsDemo {

    public static void main(String[] args) {
        // Demonstrate deep copying a simple object
        Man originalMan = new Man("John Doe", 30, new ArrayList<>(Arrays.asList("Moby Dick", "War and Peace")));
        Man copiedMan = CopyUtils.deepCopy(originalMan);
        originalMan.getFavoriteBooks().add("Pride and Prejudice");
        System.out.println("Original Man's favorite books: " + originalMan.getFavoriteBooks());
        System.out.println("Copied Man's favorite books: " + copiedMan.getFavoriteBooks());

        // Demonstrate deep copying a nested list
        NestedList<String> originalNestedList = new NestedList<>();
        originalNestedList.addList(new ArrayList<>(Arrays.asList("One", "Two", "Three")));
        NestedList<String> copiedNestedList = CopyUtils.deepCopy(originalNestedList);
        originalNestedList.getList().getFirst().add("Four");
        System.out.println("Original Nested List: " + originalNestedList.getList());
        System.out.println("Copied Nested List: " + copiedNestedList.getList());

        // Demonstrate deep copying a recursive structure
        RecursiveNode originalNode = new RecursiveNode("Parent");
        RecursiveNode childNode = new RecursiveNode("Child");
        originalNode.addChild(childNode);
        RecursiveNode copiedNode = CopyUtils.deepCopy(originalNode);
        originalNode.getChildren().getFirst().addChild(new RecursiveNode("Grandchild"));
        System.out.println("Original Node's children: " + originalNode.getChildren().getFirst().getChildren().size());
        System.out.println("Copied Node's children: " + copiedNode.getChildren().getFirst().getChildren().size());
    }
}

