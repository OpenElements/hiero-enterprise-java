package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.SubscriptionHandle;
import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.TopicClient;
import com.openelements.hiero.test.HieroTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = HieroTestConfig.class)
public class TopicClientTest {
    @Autowired
    private TopicClient topicClient;

    @Autowired
    private HieroTestUtils hieroTestUtils;

    @Test
    void testCreateTopic() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        Assertions.assertNotNull(topicId);
    }

    @Test
    void testCreatePrivateTopic() throws HieroException {
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createPrivateTopic(submitKey);
        Assertions.assertNotNull(topicId);
    }

    @Test
    void testDeleteTopic() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        Assertions.assertDoesNotThrow(() -> topicClient.deleteTopic(topicId));
    }

    @Test
    void testDeleteTopicThrowExceptionFroInvalidId() {
        final TopicId topicId = TopicId.fromString("1.2.3");
        Assertions.assertThrows(HieroException.class, () -> topicClient.deleteTopic(topicId));
    }

    @Test
    void testDeleteTopicForGivenAdminKey() throws HieroException {
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createTopic(adminKey);
        Assertions.assertDoesNotThrow(() -> topicClient.deleteTopic(topicId, adminKey));
    }

    @Test
    void testDeleteTopicThrowExceptionForDifferentAdminKey() throws HieroException {
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createTopic(adminKey);

        // operatorAccount privateKey will use as adminKey by default
        Assertions.assertThrows(
                HieroException.class,
                () -> topicClient.deleteTopic(topicId)
        );
    }

    @Test
    void testUpdateTopic() throws HieroException {
        final TopicId topicId = topicClient.createTopic("Hello Hiero");
        final String updatedMemo = "Updated Hello Hiero";
        Assertions.assertDoesNotThrow(() -> topicClient.updateTopic(topicId, updatedMemo));
    }

    @Test
    void testUpdateTopicThrowExceptionFroInvalidId() {
        final TopicId topicId = TopicId.fromString("1.2.3");
        final String updatedMemo = "Updated Hello Hiero";
        Assertions.assertThrows(HieroException.class, () -> topicClient.updateTopic(topicId, updatedMemo));
    }

    @Test
    void testUpdateTopicForGivenAdminKey() throws HieroException {
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createTopic(adminKey, "Hello Hiero");
        final String updatedMemo = "Updated Hello Hiero";
        Assertions.assertDoesNotThrow(() -> topicClient.updateTopic(topicId, adminKey, updatedMemo));
    }

    @Test
    void testUpdateTopicThrowExceptionForDifferentAdminKey() throws HieroException {
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createTopic(adminKey, "Hello Hiero");
        final String updatedMemo = "Updated Hello Hiero";

        // operatorAccount privateKey will use as adminKey by default
        Assertions.assertThrows(
                HieroException.class,
                () -> topicClient.updateTopic(topicId, updatedMemo)
        );
    }

    @Test
    void testUpdateAdminKey() throws HieroException {
        final PrivateKey updatedAdminKey = PrivateKey.generateECDSA();

        // operatorAccount privateKey will use as adminKey by default
        final TopicId topicId = topicClient.createTopic();
        Assertions.assertDoesNotThrow(() -> topicClient.updateAdminKey(topicId, updatedAdminKey));

        // to verify if adminKey is updated
        Assertions.assertThrows(HieroException.class, () -> topicClient.deleteTopic(topicId));
        Assertions.assertDoesNotThrow(() -> topicClient.deleteTopic(topicId, updatedAdminKey));
    }

    @Test
    void testUpdateAdminKeyThrowExceptionFroInvalidId() {
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey updatedAdminKey = PrivateKey.generateECDSA();
        Assertions.assertThrows(HieroException.class, () -> topicClient.updateAdminKey(topicId, updatedAdminKey));
    }

    @Test
    void testUpdateSubmitKey() throws HieroException {
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createPrivateTopic(submitKey);

        final PrivateKey updatedSubmitKey = PrivateKey.generateECDSA();
        Assertions.assertDoesNotThrow(() -> topicClient.updateSubmitKey(topicId, updatedSubmitKey));

        // to verify if submitKey is updated
        final byte[] message = "Hello Hiero".getBytes();
        Assertions.assertThrows(HieroException.class, () -> topicClient.submitMessage(topicId, submitKey, message));
        Assertions.assertDoesNotThrow(() -> topicClient.submitMessage(topicId, updatedSubmitKey, message));
    }

    @Test
    void testUpdateSubmitKeyForPublicTopic() throws HieroException {
        final TopicId topicId = topicClient.createTopic();

        final PrivateKey updatedSubmitKey = PrivateKey.generateECDSA();
        Assertions.assertDoesNotThrow(() -> topicClient.updateSubmitKey(topicId, updatedSubmitKey));

        // to verify if submitKey is updated
        final byte[] message = "Hello Hiero".getBytes();
        Assertions.assertThrows(HieroException.class, () -> topicClient.submitMessage(topicId, message));
        Assertions.assertDoesNotThrow(() -> topicClient.submitMessage(topicId, updatedSubmitKey, message));
    }

    @Test
    void testUpdateSubmitKeyThrowExceptionFroInvalidId() {
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        Assertions.assertThrows(HieroException.class, () -> topicClient.updateSubmitKey(topicId, submitKey));
    }

    @Test
    void testSubmitMessage() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        final byte[] message = "Hello Hiero".getBytes();
        Assertions.assertDoesNotThrow(() -> topicClient.submitMessage(topicId, message));
    }

    @Test
    void testSubmitMessageGreaterThanMaxLength() throws HieroException {
        final TopicId topicId = topicClient.createTopic();
        final byte[] message = new byte[1025];
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> topicClient.submitMessage(topicId, message)
        );
    }

    @Test
    void testSubmitMessageToPrivateTopic() throws HieroException {
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createPrivateTopic(submitKey);
        final byte[] message = "Hello Hiero".getBytes();
        Assertions.assertDoesNotThrow(() -> topicClient.submitMessage(topicId, submitKey, message));
    }

    @Test
    void testSubmitMessageToPrivateTopicThrowExceptionIfSubmitKeyMissing() throws HieroException {
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createPrivateTopic(submitKey);
        final byte[] message = "Hello Hiero".getBytes();
        Assertions.assertThrows(HieroException.class, () -> topicClient.submitMessage(topicId, message));
    }

    @Test
    void testSubmitMessageToPrivateTopicThrowExceptionForInvalidSubmitKey() throws HieroException {
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final TopicId topicId = topicClient.createPrivateTopic(submitKey);
        final byte[] message = "Hello Hiero".getBytes();
        Assertions.assertThrows(
                HieroException.class,
                () -> topicClient.submitMessage(topicId, PrivateKey.generateECDSA(), message)
        );
    }

    @Test
    void testSubmitMessageThrowExceptionFroInvalidId() {
        final TopicId topicId = TopicId.fromString("1.2.3");
        final byte[] message = "Hello Hiero".getBytes();
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        Assertions.assertThrows(HieroException.class, () -> topicClient.submitMessage(topicId, submitKey, message));
    }

    @Test
    void testSubscribeTopic() throws HieroException {
        final String msg = "Hello Hiero";
        final List<String> messages = new ArrayList<>();
        final TopicId topicId = topicClient.createTopic();
        hieroTestUtils.waitForMirrorNodeRecords();

        final SubscriptionHandle handler = topicClient.subscribeTopic(topicId, (message) -> {
            messages.add(new String(message.contents));
        });

        topicClient.submitMessage(topicId, msg);
        hieroTestUtils.waitForMirrorNodeRecords();

        Assertions.assertNotNull(handler);
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(msg,messages.getFirst());
    }

    @Test
    void testSubscribeTopicWithLimit() throws HieroException {
        final String msg = "Hello Hiero";
        final long limit = 1;

        final List<String> messages = new ArrayList<>();
        final TopicId topicId = topicClient.createTopic();
        hieroTestUtils.waitForMirrorNodeRecords();

        final SubscriptionHandle handler = topicClient.subscribeTopic(topicId, (message) -> {
            messages.add(new String(message.contents));
        }, limit);

        topicClient.submitMessage(topicId, msg);
        hieroTestUtils.waitForMirrorNodeRecords();

        topicClient.submitMessage(topicId, msg);
        hieroTestUtils.waitForMirrorNodeRecords();

        Assertions.assertNotNull(handler);
        Assertions.assertEquals(limit, messages.size());
    }

    @Test
    void testSubscribeTopicWithInvalidLimit() throws HieroException {
        final String msg = "limit must be greater than 0";
        final long limit = 0;

        final TopicId topicId = topicClient.createTopic();
        hieroTestUtils.waitForMirrorNodeRecords();

        final IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> topicClient.subscribeTopic(
                topicId, (message) -> {/**/}, limit
        ));

        Assertions.assertEquals(msg, e.getMessage());
    }
}
