package evgenii.newjob.deepclone.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedCollection {
    private List<Collection> list = new ArrayList<>();
    private Map<String, Collection> collectionMap = new HashMap<>();

    public Collection getCollectionList() {
        return list;
    }

    public void setCollectionList(List<Collection> list) {
        this.list = list;
    }

    public Map<String, Collection> getCollectionMap() {
        return collectionMap;
    }

    public void setCollectionMap(Map<String, Collection> collectionMap) {
        this.collectionMap = collectionMap;
    }
}
