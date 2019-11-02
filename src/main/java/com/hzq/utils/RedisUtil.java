package com.hzq.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.hzq.vo.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis cache 工具类
 * 
 */
@Component
public final class RedisUtil {

	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	private static Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
 
	/**
	 * 判断缓存中是否有对应的value
	 * @param key 对应得key
	 * @return true 存在 false 不存在
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 判断缓存中是否存在对应某种规则的key
	 * @param pattern 匹配规则
	 * @return true存在 false不存在
	 */
	public boolean existsKeys(final String pattern) {
		return getKeySize(pattern) > 0;
	}

	/**
	 * 获取所有对应匹配键的数量
	 * @param pattern 匹配规则
	 * @return 数量
	 */
	public Integer getKeySize(final String pattern) {
		return redisTemplate.keys(pattern).size();
	}

	/**
	 * 获取所有对应匹配键的集合
	 * @param pattern 匹配规则
	 * @return 返回集合
	 */
	public Set<String> getKeys(final String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 批量删除key
	 * @param pattern 类似正则的匹配
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 批量删除对应的value
	 * @param keys key的集合
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 删除对应的value
	 * @param key 对应的key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 读取缓存
	 * @param key 对应的key
	 * @return 取得对应的value
	 */
	public Object get(final String key) {
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		return operations.get(key);
	}

	public <T> List<T> getValues(final String pattern) {
		Set<String> set = getKeys(pattern);
		Iterator<String> iterator =  set.iterator();
		List<T> list = new ArrayList<>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			list.add(JsonUtil.getObjFromJson((String) get(key), SendMessage.class));
		}
		return list;
	}
 
	/**
	 * 写入缓存
	 * @param key 对应的key
	 * @param value 对应的value
	 * @return true 成功 false 失败
	 */
	public  boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			LOGGER.debug("存入redis出错");
			e.printStackTrace();
		}
		return result;
	}
 
	/**
	 * 有过期时间的写入缓存
	 * @param key 对应的key
	 * @param value 对应的value
	 * @param expireTime 对应的时间
	 * @return true 成功 false 失败
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			LOGGER.debug("有过期时间存入redis出错");
			e.printStackTrace();
		}
		return false;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}