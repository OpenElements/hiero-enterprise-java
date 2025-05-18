package com.openelements.hiero.base.data;

public class Transfers{
          private final String account;
          private final long amount;
          private final boolean isApproval;

        public Transfers(String account, long amount, boolean isApproval){
            this.account= account;
            this.amount= amount;
            this.isApproval= isApproval;
        }
    }
