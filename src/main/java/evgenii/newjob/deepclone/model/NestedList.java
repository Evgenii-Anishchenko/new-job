package evgenii.newjob.deepclone.model;

import java.util.ArrayList;
import java.util.List;

public class NestedList <T> {
    List<List<T>> list = new ArrayList<>();
    public void addList(List<T> arrayList) {
        list.add(arrayList);
    }

    public List<List<T>> getList() {
        return list;
    }
}
