package com.nttdata.wallet.bootcoin.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.nttdata.wallet.bootcoin.entity.WalletBootcoin;
import com.nttdata.wallet.bootcoin.model.CustomerWalletBootcoin;
import com.nttdata.wallet.bootcoin.service.WalletBootcoinService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomerConsumer {
	
	@Value("${api.kafka-uri.customer-topic-respose}")
	String customerTopicSave;
	
	@Autowired
	WalletBootcoinService walletBootcoinService;
	
	@KafkaListener(topics = "${api.kafka-uri.customer-topic-respose}", groupId = "group_id")
	public void customerConsumer(CustomerWalletBootcoin customerWalletBootcoin) {
		log.info("customerConsumer["+customerTopicSave+"]:" + customerWalletBootcoin.toString());		
		WalletBootcoin walletBootcoin=this.walletBootcoinService.findById(customerWalletBootcoin.getIdCustomer()).blockOptional().get();
		walletBootcoin.setIdCustomer(customerWalletBootcoin.getIdCustomer());
		this.walletBootcoinService.update(walletBootcoin).subscribe();
		log.info("customerConsumer[Save]:" + walletBootcoin);
	}
}
