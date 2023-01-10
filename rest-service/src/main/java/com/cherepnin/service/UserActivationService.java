package com.cherepnin.service;

import com.cherepnin.utils.CryptoTool;

public interface UserActivationService {
    boolean activation(String cryptoUserId);
}
