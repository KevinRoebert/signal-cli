package org.asamk.signal.manager.storage.senderKeys;

import org.asamk.signal.manager.helper.RecipientAddressResolver;
import org.asamk.signal.manager.storage.recipients.RecipientId;
import org.asamk.signal.manager.storage.recipients.RecipientResolver;
import org.signal.libsignal.protocol.SignalProtocolAddress;
import org.signal.libsignal.protocol.groups.state.SenderKeyRecord;
import org.whispersystems.signalservice.api.SignalServiceSenderKeyStore;
import org.whispersystems.signalservice.api.push.DistributionId;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class SenderKeyStore implements SignalServiceSenderKeyStore {

    private final SenderKeyRecordStore senderKeyRecordStore;
    private final SenderKeySharedStore senderKeySharedStore;

    public SenderKeyStore(
            final File file,
            final File senderKeysPath,
            final RecipientAddressResolver addressResolver,
            final RecipientResolver resolver
    ) {
        this.senderKeyRecordStore = new SenderKeyRecordStore(senderKeysPath, resolver);
        this.senderKeySharedStore = SenderKeySharedStore.load(file, addressResolver, resolver);
    }

    @Override
    public void storeSenderKey(
            final SignalProtocolAddress sender, final UUID distributionId, final SenderKeyRecord record
    ) {
        senderKeyRecordStore.storeSenderKey(sender, distributionId, record);
    }

    @Override
    public SenderKeyRecord loadSenderKey(final SignalProtocolAddress sender, final UUID distributionId) {
        return senderKeyRecordStore.loadSenderKey(sender, distributionId);
    }

    @Override
    public Set<SignalProtocolAddress> getSenderKeySharedWith(final DistributionId distributionId) {
        return senderKeySharedStore.getSenderKeySharedWith(distributionId);
    }

    @Override
    public void markSenderKeySharedWith(
            final DistributionId distributionId, final Collection<SignalProtocolAddress> addresses
    ) {
        senderKeySharedStore.markSenderKeySharedWith(distributionId, addresses);
    }

    @Override
    public void clearSenderKeySharedWith(final Collection<SignalProtocolAddress> addresses) {
        senderKeySharedStore.clearSenderKeySharedWith(addresses);
    }

    public void deleteAll() {
        senderKeySharedStore.deleteAll();
        senderKeyRecordStore.deleteAll();
    }

    public void deleteAll(RecipientId recipientId) {
        senderKeySharedStore.deleteAllFor(recipientId);
        senderKeyRecordStore.deleteAllFor(recipientId);
    }

    public void deleteSharedWith(RecipientId recipientId) {
        senderKeySharedStore.deleteAllFor(recipientId);
    }

    public void deleteSharedWith(RecipientId recipientId, int deviceId, DistributionId distributionId) {
        senderKeySharedStore.deleteSharedWith(recipientId, deviceId, distributionId);
    }

    public void deleteOurKey(RecipientId selfRecipientId, DistributionId distributionId) {
        senderKeySharedStore.deleteAllFor(distributionId);
        senderKeyRecordStore.deleteSenderKey(selfRecipientId, distributionId.asUuid());
    }

    public long getCreateTimeForOurKey(RecipientId selfRecipientId, int deviceId, DistributionId distributionId) {
        return senderKeyRecordStore.getCreateTimeForKey(selfRecipientId, deviceId, distributionId.asUuid());
    }

    public void mergeRecipients(RecipientId recipientId, RecipientId toBeMergedRecipientId) {
        senderKeySharedStore.mergeRecipients(recipientId, toBeMergedRecipientId);
        senderKeyRecordStore.mergeRecipients(recipientId, toBeMergedRecipientId);
    }
}
