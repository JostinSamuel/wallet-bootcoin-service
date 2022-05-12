package com.nttdata.wallet.bootcoin.service.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.nttdata.wallet.bootcoin.entity.WalletBootcoin;
import com.nttdata.wallet.bootcoin.model.CustomerWalletBootcoin;
import com.nttdata.wallet.bootcoin.model.WalletResponse;
import com.nttdata.wallet.bootcoin.repository.WalletBootcoinRepository;
import com.nttdata.wallet.bootcoin.service.WalletBootcoinService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class WalletBootcoinServiceImpl implements WalletBootcoinService {
	
	@Value("${api.kafka-uri.customer-topic-bootcoin}")
	String customerTopic;

	@Autowired
	WalletBootcoinRepository bootcoinRepository;
	
	@Autowired
	KafkaTemplate<String, CustomerWalletBootcoin> kafkaTemplate;

	@Override
	public Flux<WalletBootcoin> findAll() {
		return bootcoinRepository.findAll();
	}

	@Override
	public Mono<WalletBootcoin> findById(Long idWalletBootcoin) {
		return bootcoinRepository.findById(idWalletBootcoin);
	}

	@Override
	public Mono<WalletBootcoin> save(WalletBootcoin walletBootcoin) {
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idwalletBootcoin;
		if (count != null) {
			if (count <= 0) {
				idwalletBootcoin = Long.valueOf(0);
			} else {
				idwalletBootcoin = this.findAll()
						.collect(Collectors.maxBy(Comparator.comparing(WalletBootcoin::getIdWalletBootcoin)))
						.blockOptional().get().get().getIdWalletBootcoin();
			}

		} else {
			idwalletBootcoin = Long.valueOf(0);

		}
		walletBootcoin.setCreationDate(Calendar.getInstance().getTime());
		walletBootcoin.setIdWalletBootcoin(idwalletBootcoin + 1);
		return bootcoinRepository.insert(walletBootcoin);
	}

	@Override
	public Mono<WalletBootcoin> update(WalletBootcoin walletBootcoin) {
		// TODO Auto-generated method stub
		walletBootcoin.setDateModified(Calendar.getInstance().getTime());
		return bootcoinRepository.save(walletBootcoin);
	}

	@Override
	public Mono<Void> delete(Long idWalletBootcoin) {
		// TODO Auto-generated method stub
		return bootcoinRepository.deleteById(idWalletBootcoin);
	}

	@Override
	public Mono<WalletResponse> registerWallet(WalletBootcoin walletBootcoin) {

		WalletResponse response = new WalletResponse();
		response.setMensaje(new HashMap<String, Object>());
		WalletBootcoin bootcoin = new WalletBootcoin();
		bootcoin.setPhoneNumber(walletBootcoin.getPhoneNumber());
		/*verificar si ya existe un monedero bootcoin*/
		bootcoin = this.bootcoinRepository.findOne(Example.of(bootcoin)).blockOptional().orElse(null);
		if (bootcoin == null) {
			return this.save(walletBootcoin).map( e -> {
				CustomerWalletBootcoin customerWalletBootcoin = new CustomerWalletBootcoin();
				customerWalletBootcoin.setTypeDocument(walletBootcoin.getTypeDocument());
				customerWalletBootcoin.setDocumentNumber(walletBootcoin.getDocumentNumber());
				customerWalletBootcoin.setEmail(walletBootcoin.getEmail());
				customerWalletBootcoin.setFirstname(walletBootcoin.getFirstname());
				customerWalletBootcoin.setLastname(walletBootcoin.getLastname());
				customerWalletBootcoin.setPhoneNumber(walletBootcoin.getPhoneNumber());
				customerWalletBootcoin.setIdWalletBootcoin(e.getIdWalletBootcoin());
				log.info("Send kafka:" + customerTopic + " -->" + customerWalletBootcoin);
				this.kafkaTemplate.send(customerTopic, customerWalletBootcoin);
				response.setWalletBootcoin(e);
				response.getMensaje().put("status", "success");
				return response;
			});
		}else {
			response.setWalletBootcoin(bootcoin);
			response.getMensaje().put("status", "error");
			response.getMensaje().put("mensaje", "El usuario ya encuentra registrado");
			return Mono.just(response);
		}
	}
}
