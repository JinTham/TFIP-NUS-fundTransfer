package tfip.paf.fundTransfer2.Models;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Transfer implements Serializable{
    
    @NotNull(message = "Mandatory field")
    private String fromAccountId;

    @NotNull(message = "Mandatory field")
    private String toAccountId;

    @NotNull(message = "Mandatory field")
    @Min(value=10,message="Cannot be less than $10")
    private Float amount;
    
    private String comment;
    
    public Transfer() {
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }
    
}
