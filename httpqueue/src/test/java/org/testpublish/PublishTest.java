package org.testpublish;

import org.client.publisher.Publisher;
import org.client.publisher.intf.IPublisher;
import org.client.publisher.util.Config;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by andilyliao on 16-4-2.
 */
public class PublishTest {
    @Test
    public void testpub() throws IOException {
        Config config=new Config("/publisher.properties");
        IPublisher iPublisher=new Publisher(config);

    }
}
