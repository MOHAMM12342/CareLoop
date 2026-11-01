#!/usr/bin/env bash
# CareLoop Qwen 2.5 0.5B Model Asset Downloader
# Downloads the INT4 quantized weights directly into app assets for zero-download offline bundling.

set -e

DEST_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/app/src/main/assets/models"
DEST_FILE="${DEST_DIR}/qwen2.5-0.5b-instruct-int4.bin"

echo "=== CareLoop Model Bundler ==="
echo "Target: ${DEST_FILE}"

mkdir -p "${DEST_DIR}"

# HuggingFace URL for Qwen2.5-0.5B-Instruct-GGUF (Q4_K_M)
MODEL_URL="https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/qwen2.5-0.5b-instruct-q4_k_m.gguf"

if [ -f "${DEST_FILE}" ] && [ $(stat -c%s "${DEST_FILE}") -gt 10000000 ]; then
    echo "Model weights already present and verified ($(stat -c%s "${DEST_FILE}") bytes)."
    exit 0
fi

echo "Downloading Qwen 2.5 0.5B INT4 (~340 MB) from Hugging Face..."
curl -L -o "${DEST_FILE}" "${MODEL_URL}"

echo "Model downloaded successfully into app assets."
ls -lh "${DEST_FILE}"

