package tfip.paf.fundTransfer2.Repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import tfip.paf.fundTransfer2.Models.Account;
import tfip.paf.fundTransfer2.Services.TransferException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Repository
public class AccountRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GETALLACCOUNTS = "select * from bank_account";
    private static final String SQL_UPDATEBALANCE = "update bank_account set balance = balance + ? where id = ? ";
    private static final String SQL_GETACCOUNT = "select * from bank_account where id = ?";

    public Optional<List<Account>> getAllAccounts() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GETALLACCOUNTS);
        List<Account> accounts = new LinkedList<>();
        if (rs==null)
			return Optional.empty();
        while (rs.next()) {
            Account account = new Account();
            account.setAccountId(rs.getString("id"));
            account.setName(rs.getString("acct_name"));
            account.setBalance(rs.getFloat("balance"));
            accounts.add(account);
        }
        return Optional.of(accounts);
    }

    public void updateBalance(String accountId, Float amount) throws TransferException {
        if (jdbcTemplate.update(SQL_UPDATEBALANCE,amount,accountId) <= 0){
            throw new TransferException("cannot update %s".formatted(accountId));
        }
    }

    public Optional<Account> findAccountById(String accountId) {
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GETACCOUNT,accountId);
        if (!rs.next()){
            return Optional.empty();
        }
		Account account = new Account();
        account.setAccountId(rs.getString("id"));
        account.setName(rs.getString("acct_name"));
        account.setBalance(rs.getFloat("balance"));
        return Optional.of(account);
	}

}
