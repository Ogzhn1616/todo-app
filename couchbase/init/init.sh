#!/bin/bash

USERNAME=admin
PASSWORD=123456
BUCKET=todo_bucket

# Couchbase server'ı arka planda başlat
/entrypoint.sh couchbase-server &

# Server hazır olana kadar bekle
echo "Couchbase başlatılıyor, hazır olması bekleniyor..."
until curl -s http://localhost:8091/pools > /dev/null; do
  echo "Couchbase henüz hazır değil, bekleniyor..."
  sleep 5
done

echo "Couchbase hazır!"

# Cluster init
/opt/couchbase/bin/couchbase-cli cluster-init \
  --cluster-username=$USERNAME \
  --cluster-password=$PASSWORD \
  --cluster-ramsize=512 \
  --services=data,index,query || echo "Cluster zaten init edilmiş olabilir"

# Bucket kontrol
BUCKET_EXISTS=$(curl -s -u $USERNAME:$PASSWORD http://localhost:8091/pools/default/buckets/$BUCKET | grep -c "\"name\":\"$BUCKET\"")

if [ "$BUCKET_EXISTS" -eq 0 ]; then
  /opt/couchbase/bin/couchbase-cli bucket-create \
    --cluster=localhost \
    --username=$USERNAME \
    --password=$PASSWORD \
    --bucket=$BUCKET \
    --bucket-type=couchbase \
    --bucket-ramsize=256 \
    --wait
  echo "Bucket '$BUCKET' oluşturuldu!"
else
  echo "Bucket '$BUCKET' zaten mevcut, oluşturma atlandı."
fi

# Couchbase server process'i foreground'da çalışsın ki container kapanmasın
tail -f /dev/null