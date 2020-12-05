(ns web3.core
  (:import (org.web3j.protocol Web3j)
           (org.web3j.protocol.http HttpService)
           (org.web3j.protocol.core DefaultBlockParameter)))


;; Generate tx
;; Sign tx
;; Submit tx

;; Recursive Length Prefix of signed transaction
(defprotocol IBlock 
  (tx-count-hash [_])
  (tx-count-number [_]))

(defn BlockParameter [value]
  (reify DefaultBlockParameter
    (getValue [_] value)))

(def DefaultBlocks
  {:latest (BlockParameter "latest")})

(defprotocol IAdress
  (nonce [_])
  (balance [_])
  (code-hash [_])
  (storage-hash [_])
  (storage-at [_ storage-pos block]))


(defn JAddress [address provider] 
  (reify IAdress
    (balance [this]
      (-> (.ethGetBalance provider address (:latest DefaultBlocks))
          (.send)
          (.getBalance)))))


(-> (JAddress "0xfe3b557e8fb62b89f4916b721be55ceb828dbd73"
             (Web3j/build (new HttpService)))
    balance
    println)

(defprotocol Transaction
  (recipient [_])
  (signature [_])
  (value [_])
  (data [_])
  (gas-limit [_])
  (gas-price [_]))


;; Stupid demo to read ERC20 balances
(defprotocol ERC20
  (token-name [_])
  (token-symbol [_])
  (decimals [_])
  (total-supply [_])
  (balance-of [address]))

;; The DAI contract


