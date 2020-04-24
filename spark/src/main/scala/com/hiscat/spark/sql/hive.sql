create external table if not exists hive_bigints(id bigint) stored as parquet location '';
select * from hive_bigints;