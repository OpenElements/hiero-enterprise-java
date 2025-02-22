package com.openelements.hiero.base.mirrornode;

import com.openelements.hiero.base.data.ContractLog;
import com.openelements.hiero.base.data.Page;

public interface ContractRepository {

    Page<ContractLog> findLogsByContract(String contractId);
}
