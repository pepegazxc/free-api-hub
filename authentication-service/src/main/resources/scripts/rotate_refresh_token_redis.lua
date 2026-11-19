-- Author: pepega
-- Description: Make a script for making a redis do two commands (delete, get and set) in one move

local current = redis.call('GET', KEYS[1])

if current == false then
    return 0
end

if current ~= ARGV[1] then
    redis.call('DEL', KEYS[1])
    return 0
end

redis.call('SET', KEYS[1], ARGV[2], 'PX', ARGV[3])
return 1
