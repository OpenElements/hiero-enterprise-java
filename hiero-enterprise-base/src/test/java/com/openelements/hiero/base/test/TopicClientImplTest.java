package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.TopicClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TopicCreateRequest;
import com.openelements.hiero.base.protocol.data.TopicCreateResult;
import com.openelements.hiero.base.protocol.data.TopicUpdateRequest;
import com.openelements.hiero.base.protocol.data.TopicUpdateResult;
import com.openelements.hiero.base.protocol.data.TopicDeleteRequest;
import com.openelements.hiero.base.protocol.data.TopicDeleteResult;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageRequest;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TopicClientImplTest {
    ProtocolLayerClient protocolLayerClient;
    Account operationalAccount;

    TopicClientImpl topicClient;

    ArgumentCaptor<TopicCreateRequest> topicCreateCaptor = ArgumentCaptor.forClass(TopicCreateRequest.class);
    ArgumentCaptor<TopicUpdateRequest> topicUpdateCaptor = ArgumentCaptor.forClass(TopicUpdateRequest.class);
    ArgumentCaptor<TopicDeleteRequest> topicDeleteCaptor = ArgumentCaptor.forClass(TopicDeleteRequest.class);
    ArgumentCaptor<TopicSubmitMessageRequest> topicSubmitCaptor = ArgumentCaptor.forClass(TopicSubmitMessageRequest.class);

    @BeforeEach
    void setup() {
        protocolLayerClient = Mockito.mock(ProtocolLayerClient.class);
        operationalAccount = Mockito.mock(Account.class);
        topicClient = new TopicClientImpl(protocolLayerClient, operationalAccount);
    }

    @Test
    void shouldCreateTopicWithOperationalAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicCreateResult topicCreateResult = Mockito.mock(TopicCreateResult.class);
        final TopicId topicId = TopicId.fromString("1.2.3");

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicCreateTransaction(any(TopicCreateRequest.class)))
                .thenReturn(topicCreateResult);
        when(topicCreateResult.topicId()).thenReturn(topicId);
        final TopicId result = topicClient.createTopic();

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicCreateTransaction(topicCreateCaptor.capture());
        verify(topicCreateResult, times(1)).topicId();

        final TopicCreateRequest request = topicCreateCaptor.getValue();
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertNull(request.memo());
        Assertions.assertNull(request.submitKey());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(topicId, result);
    }

    @Test
    void shouldCreateTopicWithMemoAndOperationalAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicCreateResult topicCreateResult = Mockito.mock(TopicCreateResult.class);
        final TopicId topicId = TopicId.fromString("1.2.3");
        final String memo = "Hello Hiero";

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicCreateTransaction(any(TopicCreateRequest.class)))
                .thenReturn(topicCreateResult);
        when(topicCreateResult.topicId()).thenReturn(topicId);
        final TopicId result = topicClient.createTopic(memo);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicCreateTransaction(topicCreateCaptor.capture());
        verify(topicCreateResult, times(1)).topicId();

        final TopicCreateRequest request = topicCreateCaptor.getValue();
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(memo, request.memo());
        Assertions.assertNull(request.submitKey());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(topicId, result);
    }

    @Test
    void shouldCreateTopicForGivenAdminKey() throws HieroException {
        // mock
        final TopicCreateResult topicCreateResult = Mockito.mock(TopicCreateResult.class);
        final TopicId topicId = TopicId.fromString("1.2.3");

        final PrivateKey adminKey = PrivateKey.generateECDSA();
        // when
        when(protocolLayerClient.executeTopicCreateTransaction(any(TopicCreateRequest.class)))
                .thenReturn(topicCreateResult);
        when(topicCreateResult.topicId()).thenReturn(topicId);
        final TopicId result = topicClient.createTopic(adminKey);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicCreateTransaction(topicCreateCaptor.capture());
        verify(topicCreateResult, times(1)).topicId();

        final TopicCreateRequest request = topicCreateCaptor.getValue();
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertNull(request.submitKey());
        Assertions.assertNull(request.memo());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(topicId, result);
    }

    @Test
    void shouldThrowExceptionForNullParamDuringCreateTopic() {
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.createTopic(null, null));
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.createTopic((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.createTopic((PrivateKey) null));
    }

    @Test
    void shouldCreatePrivateTopicWithOperationalAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicCreateResult topicCreateResult = Mockito.mock(TopicCreateResult.class);
        final TopicId topicId = TopicId.fromString("1.2.3");

        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicCreateTransaction(any(TopicCreateRequest.class)))
                .thenReturn(topicCreateResult);
        when(topicCreateResult.topicId()).thenReturn(topicId);
        final TopicId result = topicClient.createPrivateTopic(submitKey);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicCreateTransaction(topicCreateCaptor.capture());
        verify(topicCreateResult, times(1)).topicId();

        final TopicCreateRequest request = topicCreateCaptor.getValue();
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(submitKey, request.submitKey());
        Assertions.assertNull(request.memo());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(topicId, result);
    }

    @Test
    void shouldCreatePrivateTopicWithMemoAndOperationalAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicCreateResult topicCreateResult = Mockito.mock(TopicCreateResult.class);
        final TopicId topicId = TopicId.fromString("1.2.3");
        final String memo = "Hello Hiero";

        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicCreateTransaction(any(TopicCreateRequest.class)))
                .thenReturn(topicCreateResult);
        when(topicCreateResult.topicId()).thenReturn(topicId);
        final TopicId result = topicClient.createPrivateTopic(submitKey, memo);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicCreateTransaction(topicCreateCaptor.capture());
        verify(topicCreateResult, times(1)).topicId();

        final TopicCreateRequest request = topicCreateCaptor.getValue();
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(submitKey, request.submitKey());
        Assertions.assertEquals(memo, request.memo());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(topicId, result);
    }

    @Test
    void shouldCreatePrivateTopicForGivenAdminKey() throws HieroException {
        // mock
        final TopicCreateResult topicCreateResult = Mockito.mock(TopicCreateResult.class);
        final TopicId topicId = TopicId.fromString("1.2.3");

        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        // when
        when(protocolLayerClient.executeTopicCreateTransaction(any(TopicCreateRequest.class)))
                .thenReturn(topicCreateResult);
        when(topicCreateResult.topicId()).thenReturn(topicId);
        final TopicId result = topicClient.createPrivateTopic(adminKey, submitKey);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicCreateTransaction(topicCreateCaptor.capture());
        verify(topicCreateResult, times(1)).topicId();

        final TopicCreateRequest request = topicCreateCaptor.getValue();
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(submitKey, request.submitKey());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(topicId, result);
    }

    @Test
    void shouldThrowExceptionForNullParamDuringCreatePrivateTopic() {
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.createPrivateTopic(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.createPrivateTopic(null, (PrivateKey) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.createPrivateTopic(null, null, null));
    }

    @Test
    void shouldUpdateTopicAllPropertiesAndUseOperatorAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final String memo = "Hello Hiero";
        final PrivateKey updatedAdminKey = PrivateKey.generateECDSA();
        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateTopic(topicId, updatedAdminKey, submitKey, memo);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(updatedAdminKey, request.updatedAdminKey());
        Assertions.assertEquals(submitKey, request.submitKey());
        Assertions.assertEquals(memo, request.memo());
    }

    @Test
    void shouldUpdateTopicMemoAndUseOperatorAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final String memo = "Updated Hello Hiero";

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateTopic(topicId,memo);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(memo, request.memo());
        Assertions.assertNull(request.updatedAdminKey());
        Assertions.assertNull(request.submitKey());
    }

    @Test
    void shouldUpdateTopicAllPropertiesForGivenAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final String memo = "Hello Hiero";
        final PrivateKey updatedAdminKey = PrivateKey.generateECDSA();
        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateTopic(topicId, adminKey, updatedAdminKey, submitKey, memo);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(updatedAdminKey, request.updatedAdminKey());
        Assertions.assertEquals(submitKey, request.submitKey());
        Assertions.assertEquals(memo, request.memo());
    }

    @Test
    void shouldThrowExceptionForNullParamOnUpdateTopic() throws HieroException {
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.updateTopic(null, null, null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.updateTopic(null, null, null));
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.updateTopic(null, null));
    }

    @Test
    void shouldUpdateAdminKeyAndUseOperatorAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey updatedAdminKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateAdminKey(topicId, updatedAdminKey);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(updatedAdminKey, request.updatedAdminKey());
        Assertions.assertNull(request.submitKey());
        Assertions.assertNull(request.memo());
    }

    @Test
    void shouldUpdateAdminKeyForGivenAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey updatedAdminKey = PrivateKey.generateECDSA();

        // when
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateAdminKey(topicId, adminKey, updatedAdminKey);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(updatedAdminKey, request.updatedAdminKey());
        Assertions.assertNull(request.submitKey());
        Assertions.assertNull(request.memo());
    }

    @Test
    void shouldThrowExceptionForNullParamOnUpdateAdminKey() throws HieroException {
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.updateAdminKey(null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.updateAdminKey(null, null, null));
    }

    @Test
    void shouldUpdateSubmitKeyAndUseOperatorAccountPrivateKeyAsAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateSubmitKey(topicId, submitKey);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(submitKey, request.submitKey());
        Assertions.assertNull(request.updatedAdminKey());
        Assertions.assertNull(request.memo());
    }

    @Test
    void shouldUpdateSubmitKeyForGivenAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicUpdateResult topicUpdateResult = Mockito.mock(TopicUpdateResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(protocolLayerClient.executeTopicUpdateTransaction(any(TopicUpdateRequest.class)))
                .thenReturn(topicUpdateResult);
        topicClient.updateSubmitKey(topicId, adminKey, submitKey);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicUpdateTransaction(topicUpdateCaptor.capture());

        final TopicUpdateRequest request = topicUpdateCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
        Assertions.assertEquals(submitKey, request.submitKey());
        Assertions.assertNull(request.updatedAdminKey());
        Assertions.assertNull(request.memo());
    }

    @Test
    void shouldThrowExceptionForNullParamOnUpdateSubmitKey() {
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.updateSubmitKey(null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> topicClient.updateSubmitKey(null, null, null));
    }

    @Test
    void shouldDeleteTopicAndUseDefaultAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicDeleteResult topicDeleteResult = Mockito.mock(TopicDeleteResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(adminKey);
        when(protocolLayerClient.executeTopicDeleteTransaction(any(TopicDeleteRequest.class)))
                .thenReturn(topicDeleteResult);
        topicClient.deleteTopic(topicId);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTopicDeleteTransaction(topicDeleteCaptor.capture());

        final TopicDeleteRequest request = topicDeleteCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
    }

    @Test
    void shouldDeleteTopicAndUseGivenAdminKey() throws HieroException {
        // mock
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicDeleteResult topicDeleteResult = Mockito.mock(TopicDeleteResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();

        // when
        when(protocolLayerClient.executeTopicDeleteTransaction(any(TopicDeleteRequest.class)))
                .thenReturn(topicDeleteResult);
        topicClient.deleteTopic(topicId, adminKey);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicDeleteTransaction(topicDeleteCaptor.capture());

        final TopicDeleteRequest request = topicDeleteCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(adminKey, request.adminKey());
    }

    @Test
    void shouldThrowExceptionForNullParamOnUpdateDeleteTopic() {
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.deleteTopic((TopicId) null));
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.deleteTopic((TopicId) null, null));
    }

    @Test
    void shouldSubmitMessageToTopic() throws HieroException {
        // mock
        final TopicSubmitMessageResult topicSubmitMessageResult = Mockito.mock(TopicSubmitMessageResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final byte[] message = "Hello Hiero".getBytes();

        // when
        when(protocolLayerClient.executeTopicMessageSubmitTransaction(any(TopicSubmitMessageRequest.class)))
                .thenReturn(topicSubmitMessageResult);
        topicClient.submitMessage(topicId, message);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicMessageSubmitTransaction(topicSubmitCaptor.capture());

        final TopicSubmitMessageRequest request = topicSubmitCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(message, request.message());
        Assertions.assertNull(request.submitKey());
    }

    @Test
    void shouldSubmitMessageToPrivateTopic() throws HieroException {
        // mock
        final TopicSubmitMessageResult topicSubmitMessageResult = Mockito.mock(TopicSubmitMessageResult.class);

        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final byte[] message = "Hello Hiero".getBytes();

        // when
        when(protocolLayerClient.executeTopicMessageSubmitTransaction(any(TopicSubmitMessageRequest.class)))
                .thenReturn(topicSubmitMessageResult);
        topicClient.submitMessage(topicId, submitKey, message);

        // then
        verify(protocolLayerClient, times(1))
                .executeTopicMessageSubmitTransaction(topicSubmitCaptor.capture());

        final TopicSubmitMessageRequest request = topicSubmitCaptor.getValue();
        Assertions.assertEquals(topicId, request.topicId());
        Assertions.assertEquals(message, request.message());
        Assertions.assertEquals(submitKey, request.submitKey());
    }

    @Test
    void shouldThrowExceptionIfMessageGreaterThanMaxLenOnSubmitMessage() throws HieroException {
        final String e_message = "Message cannot be longer than 1024 bytes";
        // given
        final TopicId topicId = TopicId.fromString("1.2.3");
        final PrivateKey submitKey = PrivateKey.generateECDSA();
        final byte[] message = new byte[1025];

        // then
        final IllegalArgumentException e1 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> topicClient.submitMessage(topicId, message));
        final IllegalArgumentException e2 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> topicClient.submitMessage(topicId, submitKey, message));

        Assertions.assertEquals(e_message, e1.getMessage());
        Assertions.assertEquals(e_message, e2.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullParamOnSubmitMessage() {
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.submitMessage((TopicId) null, (String)null));
        Assertions.assertThrows(NullPointerException.class, () -> topicClient.submitMessage((TopicId) null, null, (String)null));
    }
}
