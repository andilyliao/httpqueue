package org.httpqueue.util.leveldb;

import org.fusesource.leveldbjni.JniDBFactory;
import org.httpqueue.util.PropertiesStr;
import org.iq80.leveldb.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by andilyliao on 16-4-13.
 */
public class StoreLevelDB {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StoreLevelDB.class);
    public static DBFactory factory =null;
    public static Options options=null;
    public static void initLevelDB() {
        JniDBFactory.pushMemoryPool(PropertiesStr.pushMemoryPool);
        factory= JniDBFactory.factory;
        Logger logger = new Logger() {
            public void log(String message) {
                log.debug(message);
            }
        };
        options = new Options();
        options.createIfMissing(true);
        options.logger(logger);
        options.compressionType(CompressionType.NONE);
        options.cacheSize(PropertiesStr.cacheSize);
    }
    public DB openDB(String queName) throws IOException {
        DB db = factory.open(new File(PropertiesStr.storePath+queName), options);
        return db;
    }
    public void saveData(DB db,String key,String value){
        db.put(JniDBFactory.bytes(key), JniDBFactory.bytes(value));
    }
    public String getData(DB db,String key){
        return JniDBFactory.asString(db.get(JniDBFactory
                .bytes(key)));
    }
}
