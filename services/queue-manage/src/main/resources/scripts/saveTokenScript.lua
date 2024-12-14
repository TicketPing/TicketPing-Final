-- 변수 선언
local waitingQueueName = KEYS[1]
local workingQueueName = KEYS[2]
local workingQueueTokenKey = KEYS[3]
local maxSlots = tonumber(ARGV[1])
local tokenValue = ARGV[2]
local enterTime = ARGV[3]
local cacheValue = ARGV[4]
local ttl = tonumber(ARGV[5])

-- 작업열 여유 인원 조회
local currentWorkers = tonumber(redis.call('GET', workingQueueName) or 0)
local availableSlots = maxSlots - currentWorkers

-- 작업열 저장
if availableSlots > 0 then
    if redis.call('EXISTS', workingQueueTokenKey) == 0 then
        redis.call('SET', workingQueueTokenKey, cacheValue, 'EX', ttl)
        redis.call('INCR', workingQueueName)
    end
    return 1

-- 대기열 저장
else
    if redis.call('EXISTS', workingQueueTokenKey) == 0 then
        redis.call('ZADD', waitingQueueName, enterTime, tokenValue)
        return 0
    end
    return 1
end