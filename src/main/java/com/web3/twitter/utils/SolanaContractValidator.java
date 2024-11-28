package com.web3.twitter.utils;

import java.util.regex.Pattern;

public class SolanaContractValidator {

    private static final String SOLANA_ADDRESS_REGEX = "^[1-9A-HJ-NP-Za-km-z]{44}$";
    private static final Pattern SOLANA_ADDRESS_PATTERN = Pattern.compile(SOLANA_ADDRESS_REGEX);

    public static boolean isSolanaContract(String address) {
        boolean result = false;
        if (address == null || address.isEmpty()) {
            return result;
        }
        //pump 发射
        if(address.endsWith("pump")){
            result = true;
        } else {
            result = SOLANA_ADDRESS_PATTERN.matcher(address).matches();
        }
        return result;
    }
}
