echo "1. Closing monitoring services..."
docker compose -f docker-compose-monitoring.yml down

echo "2. Closing Kafka services..."
docker compose -f docker-compose-kafka.yml down

echo "3. Closing Postgres services..."
docker compose -f docker-compose-postgres.yml down

echo "4. Closing Redis services..."
docker compose -f docker-compose-redis.yml down

echo "5. Closing application services..."
docker compose down

echo "6. Remove unusing docker volume"
docker volume ls -qf dangling=true | xargs -r docker volume rm