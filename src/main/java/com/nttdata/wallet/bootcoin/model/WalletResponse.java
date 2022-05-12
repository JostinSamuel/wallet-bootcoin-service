package com.nttdata.wallet.bootcoin.model;

import java.util.Map;

import com.nttdata.wallet.bootcoin.entity.WalletBootcoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WalletResponse {
	private WalletBootcoin walletBootcoin;
	private Map<String, Object> mensaje;
}
