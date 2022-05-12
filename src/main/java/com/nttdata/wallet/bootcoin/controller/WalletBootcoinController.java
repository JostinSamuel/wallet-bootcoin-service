package com.nttdata.wallet.bootcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.wallet.bootcoin.entity.WalletBootcoin;
import com.nttdata.wallet.bootcoin.model.WalletResponse;
import com.nttdata.wallet.bootcoin.service.WalletBootcoinService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/v1/wallet/bootcoin")
public class WalletBootcoinController {

	@Autowired
	WalletBootcoinService walletBootcoinService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<WalletBootcoin> findAll() {
		return walletBootcoinService.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<WalletBootcoin>> save(@RequestBody WalletBootcoin walletBootcoin) {
		return walletBootcoinService.save(walletBootcoin).map(_wallet -> ResponseEntity.ok().body(_wallet))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}

	@PutMapping
	public Mono<ResponseEntity<WalletBootcoin>> update(@RequestBody WalletBootcoin walletBootcoin) {
		Mono<WalletBootcoin> mono = walletBootcoinService.findById(walletBootcoin.getIdWalletBootcoin())
				.flatMap(objWallet -> {
					return walletBootcoinService.update(walletBootcoin);
				});
		return mono.map(_wallet -> {
			return ResponseEntity.ok().body(_wallet);
		}).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@DeleteMapping("/{idWalletBootcoin}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable(name = "idWalletBootcoin") Long idWalletBootcoin) {
		Mono<WalletBootcoin> _wallet = walletBootcoinService.findById(idWalletBootcoin);
		_wallet.subscribe();
		WalletBootcoin wallet = _wallet.toFuture().join();
		if (wallet != null) {
			return walletBootcoinService.delete(idWalletBootcoin).map(r -> ResponseEntity.ok().<Void>build());
		} else {
			return Mono.just(ResponseEntity.noContent().build());
		}
	}

	@PostMapping("/register")
	public Mono<ResponseEntity<WalletResponse>> registerWallet(@RequestBody WalletBootcoin walletBootcoin) {
		return walletBootcoinService.registerWallet(walletBootcoin).map(_wallet -> ResponseEntity.ok().body(_wallet))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}

}
