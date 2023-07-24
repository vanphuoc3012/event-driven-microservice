## Run docker compose:

```
docker compose -f kafka-cluster.yml -f common.yml --env-file .env up
```

## Kafkacat

```
docker run -it \
--network=host \
confluentinc/cp-kafkacat \
kafkacat -b localhost:19092 -L
```
