-- 변수 선언
local queueName = KEYS[1]
local tokenValue = ARGV[1]

-- 대기열 토큰 순위 조회
local rank = redis.call('ZRANK', queueName, tokenValue)
if rank == false then
    return nil
end

-- 대기열 전체 크기 조회
local size = redis.call('ZCARD', queueName)
return {rank, size}
