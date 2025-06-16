#!/bin/bash

USERNAME=admin
PASSWORD=123456
BUCKET=todo_bucket


/entrypoint.sh couchbase-server &


echo "Couchbase is starting... Waiting for Couchbase to be ready..."
until curl -s http://localhost:8091/pools > /dev/null; do
  echo "Couchbase is not ready yet... Waiting"
  sleep 5
done

echo "Couchbase is ready!"


/opt/couchbase/bin/couchbase-cli cluster-init \
  --cluster-username=$USERNAME \
  --cluster-password=$PASSWORD \
  --cluster-ramsize=512 \
  --services=data,index,query || echo "Cluster might be already initialized"


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
  echo "Bucket '$BUCKET' is created!"
else
  echo "Bucket '$BUCKET' already exists!."
fi


tail -f /dev/null