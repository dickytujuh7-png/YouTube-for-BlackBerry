#!/data/data/com.termux/files/usr/bin/bash
set -Eeuo pipefail

if [[ -z "\${PREFIX:-}" || ! -d "$PREFIX" ]]; then
  echo "Jalankan skrip ini dari aplikasi Termux di Android."
  exit 1
fi

echo "== Feather server untuk Android / Termux =="
echo "Memasang repositori dan Chromium..."
pkg install -y tur-repo x11-repo
pkg update -y
pkg install -y git nodejs-lts chromium

CHROME_BIN="$(command -v chromium-browser || command -v chromium || true)"
if [[ -z "$CHROME_BIN" ]]; then
  echo "Chromium tidak ditemukan. Periksa repositori Termux TUR dan X11."
  exit 1
fi
echo "Chromium: $CHROME_BIN"
echo "Node: $(node --version)"

WORK="$HOME/feather-android"
if [[ -d "$WORK/.git" ]]; then
  git -C "$WORK" pull --ff-only
else
  git clone --depth 1 https://github.com/damianolampisti00/Feather.git "$WORK"
fi

cd "$WORK/server"

# sharp is only used for optional frame-delta optimization.
node -e "const fs=require('fs'); const p=require('./package.json'); delete p.dependencies.sharp; fs.writeFileSync('package.json', JSON.stringify(p,null,2)+'\n');"
rm -f package-lock.json

export PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1
npm install --omit=optional --no-audit --no-fund

# Add Android-specific headless Chromium flags once.
node <<'NODE'
const fs = require('fs');
const p = 'server.js';
let s = fs.readFileSync(p, 'utf8');
const anchor = "    args: [\n      '--disable-blink-features=AutomationControlled',";
const replacement = "    args: [\n      '--no-sandbox',\n      '--disable-dev-shm-usage',\n      '--disable-gpu',\n      '--disable-blink-features=AutomationControlled',";
if (s.includes(anchor)) {
  s = s.replace(anchor, replacement);
} else if (!s.includes("'--no-sandbox'")) {
  throw new Error('Tidak dapat menemukan konfigurasi Chromium; hentikan agar file tidak rusak.');
}
fs.writeFileSync(p, s);
NODE

cat > "$HOME/start-feather-server.sh" <<'START'
#!/data/data/com.termux/files/usr/bin/bash
set -Eeuo pipefail
export PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1
export BROWSER_PATH="$(command -v chromium-browser || command -v chromium)"
export HOST=0.0.0.0
cd "$HOME/feather-android/server"
echo "Feather server berjalan pada port 8080."
echo "Di Q10, gunakan URL: http://IP-ANDROID:8080/api"
echo "Token akses ditampilkan saat server pertama kali dijalankan."
exec node server.js --lan
START
chmod +x "$HOME/start-feather-server.sh"

echo
echo "Pemasangan selesai."
echo "Jalankan server dengan: bash ~/start-feather-server.sh"
echo "Biarkan Termux tetap aktif saat Q10 memakai Feather."
