package com.nvs.config.cache;

import jakarta.validation.constraints.NotNull;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

@Slf4j
@RequiredArgsConstructor
public class LoggingCache implements Cache {

  private final String name;
  private final Cache delegate;

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @NotNull
  @Override
  public Object getNativeCache() {
    return delegate.getNativeCache();
  }

  @Override
  public ValueWrapper get(@NotNull Object key) {
    ValueWrapper value = delegate.get(key);
    if (value != null) {
      log.info("Cache hit in '{}' for key: {}", name, key);
    } else {
      log.info("Cache miss in '{}' for key: {}", name, key);
    }
    return value;
  }

  @Override
  public <T> T get(@NotNull Object key, Class<T> type) {
    return delegate.get(key, type);
  }

  @Override
  public <T> T get(@NotNull Object key, @NotNull Callable<T> valueLoader) {
    return delegate.get(key, valueLoader);
  }

  @Override
  public void put(@NotNull Object key, Object value) {
    log.info("Putting value in cache '{}' for key: {}", name, key);
    delegate.put(key, value);
  }

  @Override
  public ValueWrapper putIfAbsent(@NotNull Object key, Object value) {
    log.info("Putting value in cache '{}' if absent for key: {}", name, key);
    return delegate.putIfAbsent(key, value);
  }

  @Override
  public void evict(@NotNull Object key) {
    log.info("Evicting cache entry in '{}' for key: {}", name, key);
    delegate.evict(key);
  }

  @Override
  public void clear() {
    log.info("Clearing all cache entries in '{}'", name);
    delegate.clear();
  }
}
