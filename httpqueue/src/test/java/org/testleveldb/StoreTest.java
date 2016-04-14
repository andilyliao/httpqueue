package org.testleveldb;

import org.fusesource.leveldbjni.JniDBFactory;
import org.httpqueue.util.leveldb.StoreLevelDB;
import org.iq80.leveldb.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by andilyliao on 16-4-14.
 */
public class StoreTest {
    @Test
    public void testSave() throws IOException {
        StoreLevelDB.initLevelDB();
        StoreLevelDB storeLevelDB=new StoreLevelDB();
        DB db=storeLevelDB.openDB("ooo.db");
        storeLevelDB.saveData(db,"aaa","bbb");
        System.out.println(storeLevelDB.getData(db,"aaa"));
    }
}
