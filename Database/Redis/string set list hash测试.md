Redis 是一个高性能的键值存储系统，支持多种数据类型，包括 `String`、`List`、`Set`、`Hash` 等。以下是针对这四种数据类型的常用指令测试练习，帮助你熟悉 Redis 的操作。

---

### **1. String 类型测试**
`String` 是 Redis 中最基本的数据类型，可以存储字符串或数字。

#### 测试指令：
```bash
# 设置键值对
SET mykey "Hello Redis"

# 获取键值
GET mykey

# 设置过期时间（5秒后过期）
SET mykey "Hello Redis" EX 5

# 自增操作（适用于数值）
SET counter 10
INCR counter
INCRBY counter 5

# 自减操作
DECR counter
DECRBY counter 3

# 查看键是否存在
EXISTS mykey

# 删除键
DEL mykey
```

---

### **2. List 类型测试**
`List` 是一个有序的字符串列表，支持从两端插入和删除元素。

#### 测试指令：
```bash
# 从左侧插入元素
LPUSH mylist "first"
LPUSH mylist "second"

# 从右侧插入元素
RPUSH mylist "third"
RPUSH mylist "fourth"

# 查看列表中的所有元素
LRANGE mylist 0 -1

# 从左侧弹出元素
LPOP mylist

# 从右侧弹出元素
RPOP mylist

# 获取列表长度
LLEN mylist

# 修改指定索引处的值
LSET mylist 0 "new_first"

# 删除指定值的元素（最多删除 2 次）
LREM mylist 2 "third"

# 删除整个列表
DEL mylist
```

---

### **3. Set 类型测试**
`Set` 是一个无序集合，集合内的元素是唯一的。

#### 测试指令：
```bash
# 添加元素到集合
SADD myset "apple"
SADD myset "banana"
SADD myset "orange"

# 查看集合中的所有元素
SMEMBERS myset

# 判断某个元素是否在集合中
SISMEMBER myset "apple"

# 获取集合的元素数量
SCARD myset

# 从集合中随机移除一个元素
SPOP myset

# 移除指定元素
SREM myset "banana"

# 求两个集合的交集
SADD set1 "a" "b" "c"
SADD set2 "b" "c" "d"
SINTER set1 set2

# 求两个集合的并集
SUNION set1 set2

# 删除集合
DEL myset
```

---

### **4. Hash 类型测试**
`Hash` 是一个键值对集合，适合存储对象。

#### 测试指令：
```bash
# 设置哈希字段值
HSET user:1 name "Alice"
HSET user:1 age 25
HSET user:1 city "New York"

# 获取单个字段值
HGET user:1 name

# 获取所有字段和值
HGETALL user:1

# 获取所有字段名
HKEYS user:1

# 获取所有字段值
HVALS user:1

# 判断字段是否存在
HEXISTS user:1 age

# 删除字段
HDEL user:1 city

# 增加字段值（适用于数值）
HINCRBY user:1 age 1

# 删除整个哈希
DEL user:1
```

---

### **总结**
通过上述测试指令，你可以熟悉 Redis 的基本操作。以下是一些注意事项：

1. **数据类型选择**：根据实际需求选择合适的数据类型。例如，`String` 适合存储简单的键值对，`List` 适合存储有序数据，`Set` 适合去重操作，`Hash` 适合存储对象。
2. **性能优化**：Redis 是单线程的，因此在设计时要注意避免耗时操作，如扫描大量数据。
3. **过期时间**：使用 `EXPIRE` 或 `SET ... EX` 设置键的过期时间，避免内存占用过高。

如果你需要更复杂的场景测试，或者有其他问题，请随时告诉我！