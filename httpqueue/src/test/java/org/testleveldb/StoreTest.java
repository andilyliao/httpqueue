package org.testleveldb;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by andilyliao on 16-4-14.
 */
public class StoreTest {
    @Test
    public void testSave(){
        JniDBFactory.pushMemoryPool(1024 *1024 *  512);
        DBFactory factory = JniDBFactory.factory;
        Logger logger = new Logger() {
            public void log(String message) {
                System.out.println(message);
            }
        };
        DBComparator comparator = new DBComparator() {
            public int compare(byte[] key1, byte[] key2) {
                return new String(key1).compareTo(new String(key2));
            }
            public String name() {
                return "simple";
            }
            public byte[] findShortestSeparator(byte[] start, byte[] limit) {
                return start;
            }
            public byte[] findShortSuccessor(byte[] key) {
                return key;
            }
        };
        Options options = new Options();
        options.createIfMissing(true);
        options.logger(logger);
        options.comparator(comparator);
        options.compressionType(CompressionType.NONE);
        options.cacheSize(100 * 1048576);
        DB db = null;
        try {
            db = factory.open(new File("/opt/xxx/example.db"), options);
            String value = JniDBFactory.asString(db.get(JniDBFactory
                    .bytes("Tampa")));
            System.out.println("=================：" + value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
