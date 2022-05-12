package com.nttdata.wallet.bootcoin.service;

import com.nttdata.wallet.bootcoin.entity.WalletBootcoin;
import com.nttdata.wallet.bootcoin.model.WalletResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletBootcoinService {

	/** Retorna todos los WalletBootcoin registrados */
	Flux<WalletBootcoin> findAll();

	/** Busqueda de un WalletBootcoin por idWalletBootcoin */
	Mono<WalletBootcoin> findById(Long idWalletBootcoin);

	/** Registra un nuevo WalletBootcoin */
	Mono<WalletBootcoin> save(WalletBootcoin walletBootcoin);

	/** Actualiza un WalletBootcoin */
	Mono<WalletBootcoin> update(WalletBootcoin walletBootcoin);

	/** Eliminacion fisica de un WalletBootcoin */
	Mono<Void> delete(Long idWalletBootcoin);

	/** Metodo que se encarga de registrar un monedero */
	Mono<WalletResponse> registerWallet(WalletBootcoin walletBootcoin);

	/** Methodo que se encarga de asociar una cuenta bancaria al monedero */
	//Mono<CardResponse> associateYourWallet(CardWallet cardWallet);

	/** Methodo que se encarga de iniciar una transferencia */
	//Mono<MovementWalletResponse> walletTransaction(MovementWalletResponse movementWalletResponse);
}
