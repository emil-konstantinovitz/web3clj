(ns web3.core
  (:import (org.web3j.crypto ECKeyPair)
           (org.web3j.crypto TransactionEncoder)
           (org.web3j.utils Numeric) 
           (org.web3j.protocol Web3j)
           (org.web3j.protocol.http HttpService)
           (org.web3j.protocol.core DefaultBlockParameter)))


;; Add encoding/decoding and crypto functions
;; Hex, RLP, padding, Keccack-256/SHA3 

(defprotocol IWallet 
  (sign [_ tx-hash]))

(defprotocol IEncodable
  (encode [_])
  (decode [_]))


(deftype Wallet [private-key public-key]
  IWallet 
  (sign [this tx-hash]
    (-> (ECKeyPair/create (Numeric/decodeQuantity private-key))
        (.sign tx-hash))))

(def accounts
  [(->Wallet
     "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
     "0xfe3b557e8fb62b89f4916b721be55ceb828dbd73")
   (->Wallet
     "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
     "0x627306090abaB3A6e1400e9345bC60c78a8BEf57")])

(defprotocol ITx
  (encode [tx])
  (sign [tx wallet]))

(deftype Tx [to nonce gas-price gas-limit value data]
  ITx 
  (encode [tx]
    (->))
  
  (sign [tx wallet]
    ()))

(defprotocol ITxPool
  (submit [_ signed-tx] "Submit a tx to the tx-pool"))

(defn TxPool [provider]
  (reify ITxPool
    (submit [_ signed-tx]
      (-> (.ethSendRawTransaction provider signed-tx)
          (.send)))))

(-> (TxPool (Web3j/build (new HttpService)))
    (submit "") 
    (.getResult))

;; Block
;;  - header
;;  - state trie [address : accounts]
;;    - storage trie (code)
;;  - tx trie [rlp encoded tx : tx]
;;  - receipts trie []

;; Build Web3 batch request from the query
(defrecord Block
  [number state-trie receipts tx])

(defrecord Account 
  [nonce balance code-hash storage-hash])


