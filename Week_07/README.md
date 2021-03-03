####学习笔记 学号:G20210579020387 2021-03-03

class13

作业:按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
思路:
本质:减少SQL语句解析的次数，减少网络传输的IO
1.批量提交 insert into values(),(),();
2.批量操作使用同一个事务,减少事务锁表次数,增加性能.
3.增加mysql单次执行SQL的语句长度,环境变量或者修改my.ini
<pre>
show variables like '%max_allowed_packet%';
SET @@global.max_allowed_packet=8388608;
SET @@global.bulk_insert_buffer_size=125829120;
SET @@global.net_buffer_length = 8192;
事务需要控制大小，事务太大可能会影响执行的效率
innodb_log_buffer_size default:8388608(8M) modify:16777216(16M)
</pre>