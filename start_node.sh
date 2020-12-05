#!/bin/bash
besu --network=dev \
     --rpc-http-cors-origins="all" \
     --host-allowlist="*" \
     --rpc-ws-enabled \
     --rpc-http-enabled \
     --data-path=/tmp/tmpDatdir
