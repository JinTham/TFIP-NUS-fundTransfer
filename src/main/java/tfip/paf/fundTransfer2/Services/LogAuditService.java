package tfip.paf.fundTransfer2.Services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.paf.fundTransfer2.Models.Transfer;
import tfip.paf.fundTransfer2.Repositories.LogAuditRepository;

@Service
public class LogAuditService {

    @Autowired
    private LogAuditRepository logAuditRepo;
    
    public void logTransaction(String txId, Transfer transfer) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date());

        JsonObject json = Json.createObjectBuilder()
                            .add("transactionId",txId)
                            .add("date",dateString)
                            .add("from_account",transfer.getFromAccountId())
                            .add("to_account",transfer.getToAccountId())
                            .add("amount",transfer.getAmount())
                            .build();
        logAuditRepo.saveToRedis(txId, json);
        logAuditRepo.saveToMongo(txId,json);
    }

}
