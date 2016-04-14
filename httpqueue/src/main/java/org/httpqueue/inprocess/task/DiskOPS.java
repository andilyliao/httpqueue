package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IDiskOPS;
import org.httpqueue.util.CommonConst;
import org.httpqueue.util.leveldb.StoreLevelDB;
import org.iq80.leveldb.DB;

/**
 * Created by andilyliao on 16-3-31.
 */
public class DiskOPS implements IDiskOPS {
    @Override
    public void inputDirectMessage(String queName, String body,long pubset, int seq, int totleseq) throws Exception {
        StoreLevelDB storeLevelDB=new StoreLevelDB();
        DB db=storeLevelDB.openDB(queName);
        String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
        storeLevelDB.saveData(db,key,body+ CommonConst.splitor+seq+ CommonConst.splitor+totleseq);
    }

    @Override
    public void inputFanoutMessage(String queName, String body,long pubset, int seq, int totleseq) throws Exception {
        StoreLevelDB storeLevelDB=new StoreLevelDB();
        DB db=storeLevelDB.openDB(queName);
        String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
        storeLevelDB.saveData(db,key,body+ CommonConst.splitor+seq+ CommonConst.splitor+totleseq);
    }
}
