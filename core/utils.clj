(ns web3.utils
  (:import org.web3j.rlp.RlpEncoder
           org.web3j.rlp.RlpString
           org.web3j.rlp.RlpList
           org.web3j.utils.Numeric))

;; BigInteger
;; HEX
;; RLP
;; Keccak-256/SHA3
;; Accounts has 42 character hexadecimal addresses
;; Check-out HEX value encoding SPEC at https://eth.wiki/json-rpc/API
;; Address 20-byte
(comment "-> RLP keccack signing ")

(comment
  "https://eth.wiki/en/fundamentals/rlp"
  "https://github.com/pelle/clothereum/blob/master/src/clothereum/rlp.clj")

(defmulti ->RLP class)

(defmethod ->RLP java.lang.String 
  [value]
  (-> (RlpString/create value)
      RlpEncoder/encode))

(defmethod ->RLP java.lang.Number
  [value]
  (-> (RlpString/create value)
      RlpEncoder/encode))

;; Alternative implementation
(extend java.lang.Number
  ->RLP
  {:->RLP 
   (fn [value] 
     (-> (RlpString/create value)
         RlpEncoder/encode))})

;; TODO: implement ->RLP for seqs

(comment
 (->RLP 10000)
 (->RLP "heysan")
 (->RLP ["heysan" 2 3]))

(defn tx->bytes 
  [{:keys [nonce recipient gas-price gas-limit value data chain-id]}]
  (-> (map ->RLP )) 
  (-> (new RlpList nonce
               gas-price
               gas-limit
               recipient
               value
               data
               chain-id
               "0x"
               "0x")
      RlpEncoder/encodeList))

(tx->bytes {:nonce 1 
     :recipient "0xfe3b557e8fb62b89f4916b721be55ceb828dbd73"
     :gas-price 1
     :gas-limit 10
     :value 10
     :data ""
     :chain-id 0})



