package com.daofu.commons.redis;

import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.MultiKeyJedisClusterCommands;

public abstract class RedisClient implements JedisCommands,MultiKeyCommands,MultiKeyJedisClusterCommands,RedisInterface{


}
