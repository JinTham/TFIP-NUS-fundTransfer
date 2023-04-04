package tfip.paf.fundTransfer2.Services;

import tfip.paf.fundTransfer2.Models.Transfer;

public class TransferException extends Exception {
    
    private Transfer transfer;

    public TransferException() {
		super();
	}

	public TransferException(String msg) {
		super(msg);
	}

	public void setTransferInfo(Transfer transferInfo) { this.transfer = transfer; }
	public Transfer getTransferInfo() { return this.transfer; }

}
