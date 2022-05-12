package com.nttdata.wallet.bootcoin.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.wallet.bootcoin.model.TypeDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(collection = "wallet-bootcoin")
public class WalletBootcoin {

	@Id
	private Long idWalletBootcoin;
	private Long idCustomer;
	private String firstname;
	private String lastname;
	private TypeDocument typeDocument;
	private String documentNumber;
	private String email;
	private String phoneNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date creationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dateModified;
	
}
