package tfip.paf.fundTransfer2.Controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import tfip.paf.fundTransfer2.Models.Account;
import tfip.paf.fundTransfer2.Models.Transfer;
import tfip.paf.fundTransfer2.Services.AccountService;
import tfip.paf.fundTransfer2.Services.TransferException;

@Controller
@RequestMapping
public class FundsTransferController {
    
    @Autowired
    private AccountService accountSvc;

    @GetMapping(path={"/","/index.html"})
    public String landingPage(Model model) {
        Optional<List<Account>> opt = accountSvc.getAllAccounts();
        model.addAttribute("accounts", opt.get());
        model.addAttribute("transfer",new Transfer());
        return "view0";
    }

    @PostMapping(path="/transfer",consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postTransfer(@Valid Transfer transfer, BindingResult result, Model model, HttpSession session) {
        session.setAttribute("transfer",transfer);
        if (result.hasErrors()){
            Optional<List<Account>> opt = accountSvc.getAllAccounts();
            model.addAttribute("accounts", opt.get());
            Transfer lastTransfer = (Transfer)session.getAttribute("transfer");
            model.addAttribute("transfer", lastTransfer);
            return "view0";
        }
        List<String> errorList = new LinkedList<>();
        if (!accountSvc.accountExists(transfer.getFromAccountId())) {
            errorList.add("Account %s does not exists!".formatted(transfer.getFromAccountId()));
		}
        if (!accountSvc.accountExists(transfer.getToAccountId())) {
			errorList.add("Account %s does not exists!".formatted(transfer.getToAccountId()));
		}
        if (transfer.getToAccountId().equalsIgnoreCase(transfer.getFromAccountId())) {
			errorList.add("Cannot transfer between the same account!");
		}
        if (errorList.size()>0){
            Optional<List<Account>> opt = accountSvc.getAllAccounts();
            model.addAttribute("accounts", opt.get());
            Transfer lastTransfer = (Transfer)session.getAttribute("transfer");
            model.addAttribute("transfer", lastTransfer);
            model.addAttribute("errorList", errorList);
            return "view0";
        }
        try {
            String txId = accountSvc.performTransfer(transfer);
            model.addAttribute("transfer",transfer);
            model.addAttribute("txId",txId);
            model.addAttribute("fromAccount",accountSvc.findAccountById(transfer.getFromAccountId()).get());
            model.addAttribute("toAccount",accountSvc.findAccountById(transfer.getToAccountId()).get());
            return "view1";
        } catch (TransferException ex) {
            Optional<List<Account>> opt = accountSvc.getAllAccounts();
            model.addAttribute("accounts", opt.get());
            Transfer lastTransfer = (Transfer)session.getAttribute("transfer");
            model.addAttribute("transfer", lastTransfer);
            errorList.add(ex.getMessage());
            model.addAttribute("errorList", errorList);
            return "view0";
        }

    }

}
