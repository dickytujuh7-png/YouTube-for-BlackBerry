#!/data/data/com.termux/files/usr/bin/bash
set -Eeuo pipefail

SERVER_DIR="$HOME/feather-android/server"
START_SCRIPT="$HOME/start-feather-server.sh"

if [[ ! -d "$SERVER_DIR" ]]; then
  echo "Folder server Feather tidak ditemukan: $SERVER_DIR"
  exit 1
fi

if [[ ! -f "$START_SCRIPT" ]]; then
  echo "Skrip start server tidak ditemukan: $START_SCRIPT"
  exit 1
fi

echo "Pastikan server lama sudah dihentikan dengan CTRL+C sebelum menjalankan skrip ini."
read -r -p "Lanjutkan mengganti token dan menjalankan server? [y/N] " answer
case "$answer" in
  y|Y|yes|YES) ;;
  *) echo "Dibatalkan."; exit 0 ;;
esac

rm -f "$SERVER_DIR/token.txt"
exec bash "$START_SCRIPT"
