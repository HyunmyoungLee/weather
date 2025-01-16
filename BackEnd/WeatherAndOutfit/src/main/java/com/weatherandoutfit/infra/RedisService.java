package com.weatherandoutfit.infra;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class RedisService {
	private final Jedis jedis;
	
	@Autowired
	public RedisService(Jedis jedis) {
		this.jedis = jedis;
	}
	
	public void setData(String key, String value) {
		jedis.setex(key, 120, value);
	}
	
	public String getData(String key) {
		return jedis.get(key);
	}
	
	public void delete(String key) {
		jedis.del(key);
	}
}
