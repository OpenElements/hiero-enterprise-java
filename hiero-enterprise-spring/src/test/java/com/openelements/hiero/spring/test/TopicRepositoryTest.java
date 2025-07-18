package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.TopicClient;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Topic;
import com.openelements.hiero.base.data.TopicMessage;
import com.openelements.hiero.base.mirrornode.TopicRepository;
import com.openelements.hiero.test.HieroTestUtils;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class TopicRepositoryTest {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private HieroTestUtils hieroTestUtils;

    @Autowired
    private TopicClient topicClient;

    @Test
    void testNullParam() {
        Assertions.assertThrows(NullPointerException.class, () -> topicRepository.findTopicById((TopicId) null));
        Assertions.assertThrows(NullPointerException.class, () -> topicRepository.getMessages((TopicId) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> topicRepository.getMessageBySequenceNumber((TopicId) null, 1)
        );
    }

    @Test
    void testFindTopicById() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        hieroTestUtils.waitForMirrorNodeRecords();

        final Optional<Topic> result = topicRepository.findTopicById(topicId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void testFindTopicByIdReturnsEmptyOptional() throws HieroException {
        final TopicId topicId = TopicId.fromString("0.0.0");
        final Optional<Topic> result = topicRepository.findTopicById(topicId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetMessagesByTopicId() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        topicClient.submitMessage(topicId, "Hello Hiero");
        hieroTestUtils.waitForMirrorNodeRecords();

        final Page<TopicMessage> result = topicRepository.getMessages(topicId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getData().size());
    }

    @Test
    void testGetMessagesByTopicIdReturnsEmptyList() throws HieroException {
        final TopicId topicId = TopicId.fromString("1.2.3");
        final Page<TopicMessage> result = topicRepository.getMessages(topicId);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getData().isEmpty());
    }

    @Test
    void testGetMessagesByTopicIdAndSequenceNumber() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        topicClient.submitMessage(topicId, "Hello Hiero 1");
        topicClient.submitMessage(topicId, "Hello Hiero 2");
        hieroTestUtils.waitForMirrorNodeRecords();

        final Optional<TopicMessage> result = topicRepository.getMessageBySequenceNumber(topicId, 1);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Hello Hiero 1", result.get().message());
    }

    @Test
    void testGetMessagesByTopicIdAndSequenceNumberReturnEmptyOptional() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        topicClient.submitMessage(topicId, "Hello Hiero 1");
        hieroTestUtils.waitForMirrorNodeRecords();

        final Optional<TopicMessage> result = topicRepository.getMessageBySequenceNumber(topicId, 2);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetMessagesByTopicIdAndSequenceNumberThrowsException() throws HieroException {
        final TopicId topicId = TopicId.fromString("0.0.0");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> topicRepository.getMessageBySequenceNumber(topicId, 0));
    }
}
