package util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author helinhong
 */
public class CacheMap {
	private static final Logger log = LoggerFactory.getLogger(CacheMap.class);

	/**
	 * @desction: 使用google guava缓存处理
	 * @author: helinhong
	 * @date: 2019/7/17
	 */
	private static Cache<String,Object> cache;
	static {
		cache = CacheBuilder.newBuilder().maximumSize(10000)
				.expireAfterWrite(24, TimeUnit.HOURS)
				.initialCapacity(10)
				.removalListener(rn -> {
					if(log.isInfoEnabled()){
						log.info("被移除缓存{}:{}",rn.getKey(),rn.getValue());
					}
				}).build();
	}

	/**
	 * @desction: 获取缓存
	 */
	public  static Object get(String key){
		return StringUtils.isNotEmpty(key)?cache.getIfPresent(key):null;
	}
	/**
	 * @desction: 放入缓存
	 */
	public static void put(String key,Object value){
		if(StringUtils.isNotEmpty(key) && value !=null){
			cache.put(key,value);
		}
	}
	/**
	 * @desction: 移除缓存
	 */
	public static void remove(String key){
		if(StringUtils.isNotEmpty(key)){
			cache.invalidate(key);
		}
	}
	/**
	 * @desction: 批量删除缓存
	 */
	public static void remove(List<String> keys){
		if(keys !=null && keys.size() >0){
			cache.invalidateAll(keys);
		}
	}
}
