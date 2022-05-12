package com.nttdata.wallet.bootcoin.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.wallet.bootcoin.entity.WalletBootcoin;

@Repository
public interface WalletBootcoinRepository extends ReactiveMongoRepository<WalletBootcoin, Long> {

}
