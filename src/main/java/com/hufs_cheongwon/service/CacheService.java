package com.hufs_cheongwon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheManager cacheManager;

    public void saveEmailCode(String email, String code) {
        Cache cache = cacheManager.getCache("MyCache");
        if (cache != null) {
            cache.put(email, code);
        } else {
            throw new IllegalArgumentException("Cache not foud");
        }
    }

    public String getEmailCode(String email) {
        Cache cache = cacheManager.getCache("MyCache");
        if (cache != null) {
            return cache.get(email, String.class);
        }
        return null;
    }

    public void evictEmailCode(String email) {
        Cache cache = cacheManager.getCache("MyCache");
        if (cache != null) {
            cache.evict(email);
        }
    }
}
