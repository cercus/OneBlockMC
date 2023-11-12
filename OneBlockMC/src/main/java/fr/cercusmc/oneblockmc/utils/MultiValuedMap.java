package fr.cercusmc.oneblockmc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import com.google.common.base.Objects;

public class MultiValuedMap<K, V> {
	
	private int capacity;
	private Map<K, Collection<V>> map;
	private int size;
	
	public MultiValuedMap(int initialCapacity) {
		this.capacity = initialCapacity;
		this.map = new HashMap<>(initialCapacity);
		this.size = 0;
	}
	
	public MultiValuedMap() {
		this(10);
	}
	
	public Collection<V> get(K k) {
		if(!containsKey(k)) return new ArrayList<>();
		
		return map.get(k);
	}
	
	public Map<K, Collection<V>> asMap() {
		return map;
	}
	
	public boolean containsKey(K k) {
		return map.containsKey(k);
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void put(K k, V v) {
		if(size+1 >= capacity) capacity += 10;
		if(containsKey(k)) {
			map.get(k).add(v);
			map.put(k, map.get(k));
		} else {
			map.put(k, new ArrayList<>(Arrays.asList(v)));
		}
		size++;
	}
	
	public void putAll(Map<? extends K, ? extends V> map) {
		map.forEach(this::put);
	}
	
	public boolean removeKey(K k) {
		if(!containsKey(k)) return false;
		map.remove(k);
		size--;
		return true;
	}
	
	public boolean removeValue(K k, V v) {
		if(!containsKey(k)) return false;
		Collection<V> values = map.get(k);
		Collection<V> valuesSave = new ArrayList<>(values);
		
		for(V v1 : valuesSave) {
			if(Objects.equal(v, v1)) {
				values.remove(v);
				map.replace(k, values);
				if(values.isEmpty()) removeKey(k);
				return true;
			}
		}
		return false;
	}
	
	public boolean replace(K k, Collection<V> v) {
		
		return this.map.replace(k, v) != null;
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(capacity, map, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		MultiValuedMap<?, ?> other = (MultiValuedMap<?, ?>) obj;
		return capacity == other.capacity && java.util.Objects.equals(map, other.map) && size == other.size;
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
	public int size() {
		return size;
	}
	
	public Set<Entry<K, Collection<V>>> entrySet() {
		return map.entrySet();
	}
	
	public void clear() {
		map.clear();
		size  = 0;
		capacity = 10;
	}
	
	public void forEach(BiConsumer<? super K, Collection<? super V>> action) {
		for(Map.Entry<K, Collection<V>> entry : entrySet()) {
			K k;
			Collection<V> v;
			try {
				k = entry.getKey();
				v = entry.getValue();
			} catch(IllegalStateException e) {
				throw new ConcurrentModificationException(e);
			}
			action.accept(k, v);
			
		}
	}
	

}
