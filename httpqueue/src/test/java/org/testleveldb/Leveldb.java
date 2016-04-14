package org.testleveldb;

import java.io.File;
import java.io.IOException;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBComparator;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Logger;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.WriteBatch;
import org.junit.Test;

public class Leveldb {

	private void test(){
		JniDBFactory.pushMemoryPool(1024 *1024 *  512);
		DBFactory factory = JniDBFactory.factory;
		Logger logger = new Logger() {
			public void log(String message) {
				System.out.println(message);
			}
		};
//		DBComparator comparator = new DBComparator() {
//			public int compare(byte[] key1, byte[] key2) {
//				return new String(key1).compareTo(new String(key2));
//			}
//
//			public String name() {
//				return "simple";
//			}
//
//			public byte[] findShortestSeparator(byte[] start, byte[] limit) {
//				return start;
//			}
//
//			public byte[] findShortSuccessor(byte[] key) {
//				return key;
//			}
//		};
		Options options = new Options();
		options.createIfMissing(true);
		options.logger(logger);
//		options.comparator(comparator);
		options.compressionType(CompressionType.NONE);
		options.cacheSize(100 * 1048576); // 100MB
		DB db = null;

		try {
			db = factory.open(new File("/opt/xxx/aaa.db"), options);
//			String stats = db.getProperty("leveldb.stats");
//			System.out.println(stats);

			db.put(JniDBFactory.bytes("Tampa"), JniDBFactory.bytes("rocks"));
			String value = JniDBFactory.asString(db.get(JniDBFactory
					.bytes("Tampa")));
			System.out.println("=================：" + value);
//			db.delete(JniDBFactory.bytes("Tampa"));

//			WriteBatch batch = db.createWriteBatch();
//			batch.delete(JniDBFactory.bytes("Denver"));
//			batch.put(JniDBFactory.bytes("Tampa"), JniDBFactory.bytes("green"));
//			batch.put(JniDBFactory.bytes("London"), JniDBFactory.bytes("red"));
//			db.write(batch);
//			batch.close();
//
//			DBIterator iterator = db.iterator();
//			System.out.println("=================");
//			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
//				String key = JniDBFactory
//						.asString(iterator.peekNext().getKey());
//				String val = JniDBFactory.asString(iterator.peekNext()
//						.getValue());
//				System.out.println(key + " = " + val);
//			}
//			System.out.println("=================");
//			iterator.close();
//
//			ReadOptions ro = new ReadOptions();
//			ro.snapshot(db.getSnapshot());
//			DBIterator iterator1 = db.iterator(ro);
//			System.out.println("=================");
//			for (iterator1.seekToFirst(); iterator1.hasNext(); iterator1.next()) {
//				String key = JniDBFactory.asString(iterator1.peekNext()
//						.getKey());
//				String val = JniDBFactory.asString(iterator1.peekNext()
//						.getValue());
//				System.out.println(key + " = " + val);
//			}
//			System.out.println("=================");
//
//			String v = JniDBFactory.asString(db.get(
//					JniDBFactory.bytes("Tampa"), ro));
//			System.out.println("=================：" + v);
//
//			long[] sizes = db.getApproximateSizes(new Range(JniDBFactory.bytes("a"), JniDBFactory.bytes("k")), new Range(JniDBFactory.bytes("k"), JniDBFactory.bytes("z")));
//			System.out.println("Size: "+sizes[0]+", "+sizes[1]);
//			System.out.println("-------------------------------------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		try {
//			factory.repair(new File("example"), options);
//			factory.destroy(new File("example"), options);
//			JniDBFactory.popMemoryPool();
//			System.out.println("-------------------------------------------------------------------------------");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	@Test
	public void testlevelDB()throws Exception {
		Leveldb l=new Leveldb();
		l.test();
	}

}
