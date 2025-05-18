package com.openelements.hiero.base.data;

import org.jspecify.annotations.Nullable;

public class NftTransfers {
         private final  boolean isApproval;
         private final String receiverAccountId;
         private final @Nullable  String senderAccountId;
         private final int serialNumber;
         private final String tokenId;

       public NftTransfers(boolean isApproval, String receiverAccountId, String senderAccountId, int serialNumber, String tokenId ){
           this.isApproval=isApproval;
           this.receiverAccountId=receiverAccountId;
           this.senderAccountId=senderAccountId;
           this.serialNumber=serialNumber;
           this.tokenId=tokenId;
        }
    }
