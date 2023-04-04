package tfip.paf.fundTransfer2.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tfip.paf.fundTransfer2.Models.Account;
import tfip.paf.fundTransfer2.Models.Transfer;
import tfip.paf.fundTransfer2.Repositories.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private LogAuditService logAuditSvc;

    public Optional<List<Account>> getAllAccounts() {
        return accountRepo.getAllAccounts();
    }

    @Transactional(rollbackOn = TransferException.class)
    public String performTransfer(Transfer transfer) throws TransferException {
        String txId = UUID.randomUUID().toString().substring(0, 8);
        accountRepo.updateBalance(transfer.getFromAccountId(), -transfer.getAmount());
        accountRepo.updateBalance(transfer.getToAccountId(), transfer.getAmount());
        logAuditSvc.logTransaction(txId,transfer);
        return txId;
    }

    public boolean accountExists(String accountId) {
		return accountRepo.findAccountById(accountId).isPresent();
	}

    public Optional<Account> findAccountById(String accountId) {
		return accountRepo.findAccountById(accountId);
	}

}
