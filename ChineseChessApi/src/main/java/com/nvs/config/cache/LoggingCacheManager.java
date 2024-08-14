package com.nvs.config.cache;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class LoggingCacheManager implements CacheManager {

  private final CacheManager delegate;

  public LoggingCacheManager(CacheManager delegate) {
    this.delegate = delegate;
  }

  @Override
  public Cache getCache(@NotNull String name) {
    Cache cache = delegate.getCache(name);
    if (cache != null) {
      return new LoggingCache(name, cache);
    }
    return null;
  }

  @NotNull
  @Override
  public Collection<String> getCacheNames() {
    return delegate.getCacheNames();
  }
}
