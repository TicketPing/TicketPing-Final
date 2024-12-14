-- 변수 선언
local queueName = KEYS[1]

-- 대기열 첫 번째 토큰 조회
local firstMember = redis.call('ZRANGE', queueName, 0, 0)
if #firstMember == 0 then
    return nil
end

-- 대기열 첫 번째 토큰 삭제
redis.call('ZREM', queueName, firstMember[1])
return firstMember[1]
