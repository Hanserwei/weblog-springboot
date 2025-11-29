-- 1. 创建一个函数，用于在数据更新时自动修改 update_time 字段
CREATE OR REPLACE FUNCTION set_update_time()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.update_time = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 2. 创建表（使用 BOOLEAN 替代 SMALLINT for is_deleted）
CREATE TABLE t_user
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(60)              NOT NULL UNIQUE,
    password    VARCHAR(60)              NOT NULL,
    create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    -- WITH TIME ZONE 是更严谨的选择
    update_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    -- 使用 BOOLEAN 逻辑删除，DEFAULT FALSE 对应 '0：未删除'
    is_deleted  BOOLEAN                  NOT NULL DEFAULT FALSE
);

-- 3. 创建触发器，在每次 UPDATE 操作前调用函数
CREATE TRIGGER set_t_user_update_time
    BEFORE UPDATE
    ON t_user
    FOR EACH ROW
EXECUTE FUNCTION set_update_time();

-- 添加注释
COMMENT ON TABLE t_user IS '用户表（优化版）';
COMMENT ON COLUMN t_user.is_deleted IS '逻辑删除：FALSE：未删除 TRUE：已删除';