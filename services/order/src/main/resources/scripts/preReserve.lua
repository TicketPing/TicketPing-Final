local seatData = redis.call("GET", KEYS[1])
if not seatData then
    return "SEAT_CACHE_NOT_FOUND"
end

local seatObj = cjson.decode(seatData)

if seatObj.seatStatus ~= "AVAILABLE" then
    return "SEAT_ALREADY_TAKEN"
end

seatObj.seatStatus = "HELD"
redis.call("SET", KEYS[1], cjson.encode(seatObj))

local newKey = KEYS[2]
local ttl = tonumber(ARGV[1])
redis.call("SET", newKey, "HELD")
redis.call("EXPIRE", newKey, ttl)

return "SUCCESS"