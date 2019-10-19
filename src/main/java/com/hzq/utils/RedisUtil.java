package com.hzq.utils;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.hzq.domain.Apply;
import com.hzq.domain.Content;
import com.hzq.vo.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
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
	 * 批量删除对应的value
	 * @param keys key的集合
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
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
	 * 删除对应的value
	 * @param key 对应的key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}
 
	/**
	 * 判断缓存中是否有对应的value
	 * @param key 对应得key
	 * @return true 存在 false 不存在
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
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


	public  void appendObj(String id, Content content) {
		if (exists(id)) {
			CommonResult result = JsonUtil.getObjFromJson((String) get(id),CommonResult.class);
			List<Content> list = result.getContentList();
			if (list != null) {
				list.add(content);
			} else {
				list = new ArrayList<>();
				list.add(content);
			}
			set(id,JsonUtil.toJson(result));
		} else {
			CommonResult result = new CommonResult();
			List<Content> list = new ArrayList<>();
			list.add(content);
			result.setContentList(list);
			set(id,JsonUtil.toJson(result));
		}
	}


	public void setRedisTemplate(
			RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}