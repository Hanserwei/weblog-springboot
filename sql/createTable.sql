-- ====================================================================================================================
-- ====================================================================================================================
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
-- ====================================================================================================================
-- ====================================================================================================================

-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_user_role
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(60)              NOT NULL,
    role_name   VARCHAR(60)              NOT NULL, -- 重命名为 role_name 避免关键字冲突
    create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_username ON t_user_role (username);

COMMENT ON COLUMN t_user_role.role_name IS '角色名称';
-- 为 t_user_role 表创建触发器
CREATE TRIGGER set_t_user_role_update_time
    BEFORE UPDATE
    ON t_user_role
    FOR EACH ROW
EXECUTE FUNCTION set_update_time();
-- ====================================================================================================================
-- ====================================================================================================================

-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_category
(
    -- id：对应 MySQL 的 bigint(20) unsigned NOT NULL AUTO_INCREMENT
    id          BIGSERIAL PRIMARY KEY,

    -- 分类名称：VARCHAR(60) NOT NULL DEFAULT ''，同时是 UNIQUE 约束
    "name"      VARCHAR(60)              NOT NULL DEFAULT '',

    -- 创建时间
    create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- 最后一次更新时间
    update_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- 逻辑删除标志位：tinyint(2) NOT NULL DEFAULT '0'，改为 BOOLEAN
    is_deleted  BOOLEAN                  NOT NULL DEFAULT FALSE,

    -- UNIQUE KEY uk_name (`name`)
    CONSTRAINT uk_name UNIQUE ("name")
);

-- 添加非唯一索引（对应 MySQL 的 KEY `idx_create_time`）
CREATE INDEX idx_create_time ON t_category (create_time);

-- 可选：添加注释
COMMENT ON TABLE t_category IS '文章分类表';
COMMENT ON COLUMN t_category.id IS '分类id';
COMMENT ON COLUMN t_category.name IS '分类名称';
COMMENT ON COLUMN t_category.create_time IS '创建时间';
COMMENT ON COLUMN t_category.update_time IS '最后一次更新时间';
COMMENT ON COLUMN t_category.is_deleted IS '逻辑删除标志位：FALSE：未删除 TRUE：已删除';
-- 为 t_category 表创建触发器
CREATE TRIGGER set_t_category_update_time
    BEFORE UPDATE
    ON t_category
    FOR EACH ROW
EXECUTE FUNCTION set_update_time();
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- 1. 创建表结构
CREATE TABLE t_tag
(
    -- id: 使用 BIG SERIAL，自动创建序列，性能优异
    id          BIGSERIAL PRIMARY KEY,

    -- name: 保持 VARCHAR(60)，但在 PG 中 TEXT 和 VARCHAR 性能一样，
    -- 这里为了保留原表 "60字符限制" 的业务逻辑，继续使用 VARCHAR(60)
    name        VARCHAR(60)              NOT NULL DEFAULT '',

    -- create_time: 使用带时区的时间戳，更标准严谨
    create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- update_time: 同上
    update_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- is_deleted: 使用原生 BOOLEAN 类型，存储效率高且语义明确
    is_deleted  BOOLEAN                  NOT NULL DEFAULT FALSE,

    -- 约束：显式命名约束，方便后续维护（如报错时能看到具体约束名）
    CONSTRAINT uk_tag_name UNIQUE (name)
);

-- 2. 创建普通索引
-- 对应 MySQL 的 KEY `idx_create_time`
CREATE INDEX idx_tag_create_time ON t_tag (create_time);

-- 3. 添加注释 (PostgresSQL 标准方式)
COMMENT ON TABLE t_tag IS '文章标签表';
COMMENT ON COLUMN t_tag.id IS '标签id';
COMMENT ON COLUMN t_tag.name IS '标签名称';
COMMENT ON COLUMN t_tag.create_time IS '创建时间';
COMMENT ON COLUMN t_tag.update_time IS '最后一次更新时间';
COMMENT ON COLUMN t_tag.is_deleted IS '逻辑删除标志位：FALSE：未删除 TRUE：已删除';

-- 4. 应用自动更新时间戳触发器 (体现 PostgresSQL 强大的过程语言优势)
-- 前提：您之前已经执行过 CREATE FUNCTION set_update_time() ...
CREATE TRIGGER set_t_tag_update_time
    BEFORE UPDATE
    ON t_tag
    FOR EACH ROW
EXECUTE FUNCTION set_update_time();
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_blog_settings
(
    -- id: 使用 BIG SERIAL 自动管理序列
    id              BIGSERIAL PRIMARY KEY,

    -- logo: 图片路径可能很长，使用 TEXT 替代 VARCHAR(120)，无性能损耗
    logo            TEXT        NOT NULL DEFAULT '',

    -- name: 博客名称通常较短，保留 VARCHAR 限制也是一种合理的业务约束
    name            VARCHAR(60) NOT NULL DEFAULT '',

    -- author: 作者名同上
    author          VARCHAR(20) NOT NULL DEFAULT '',

    -- introduction: 介绍语可能会变长，使用 TEXT 更灵活
    introduction    TEXT        NOT NULL DEFAULT '',

    -- avatar: 头像路径，使用 TEXT
    avatar          TEXT        NOT NULL DEFAULT '',

    -- 下面的主页链接：原 MySQL 定义 varchar(60) 风险很高，
    -- 现在的 URL 很容易超过 60 字符，PG 使用 TEXT 完美解决
    github_homepage TEXT        NOT NULL DEFAULT '',
    csdn_homepage   TEXT        NOT NULL DEFAULT '',
    gitee_homepage  TEXT        NOT NULL DEFAULT '',
    zhihu_homepage  TEXT        NOT NULL DEFAULT ''
);

-- 添加注释
COMMENT ON TABLE t_blog_settings IS '博客设置表';
COMMENT ON COLUMN t_blog_settings.id IS 'id';
COMMENT ON COLUMN t_blog_settings.logo IS '博客Logo';
COMMENT ON COLUMN t_blog_settings.name IS '博客名称';
COMMENT ON COLUMN t_blog_settings.author IS '作者名';
COMMENT ON COLUMN t_blog_settings.introduction IS '介绍语';
COMMENT ON COLUMN t_blog_settings.avatar IS '作者头像';
COMMENT ON COLUMN t_blog_settings.github_homepage IS 'GitHub 主页访问地址';
COMMENT ON COLUMN t_blog_settings.csdn_homepage IS 'CSDN 主页访问地址';
COMMENT ON COLUMN t_blog_settings.gitee_homepage IS 'Gitee 主页访问地址';
COMMENT ON COLUMN t_blog_settings.zhihu_homepage IS '知乎主页访问地址';
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_article
(
    id          BIGSERIAL PRIMARY KEY,

    -- 标题：保持限制，适合作为索引或列表显示
    title       VARCHAR(120)             NOT NULL DEFAULT '',

    -- 封面：使用 TEXT，不再担心 URL 超长
    cover       TEXT                     NOT NULL DEFAULT '',

    -- 摘要：使用 TEXT，不再受 160 字限制，前端截取即可
    summary     TEXT                              DEFAULT '',

    -- 阅读量：使用 INTEGER 配合 CHECK 约束模拟 unsigned
    read_num    INTEGER                  NOT NULL DEFAULT 1 CHECK (read_num >= 0),

    -- 时间与逻辑删除
    create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted  BOOLEAN                  NOT NULL DEFAULT FALSE
);

-- 索引
CREATE INDEX idx_article_create_time ON t_article (create_time);

-- 自动更新时间戳触发器
CREATE TRIGGER set_t_article_update_time
    BEFORE UPDATE
    ON t_article
    FOR EACH ROW
EXECUTE FUNCTION set_update_time();

-- 注释
COMMENT ON TABLE t_article IS '文章表';
COMMENT ON COLUMN t_article.read_num IS '被阅读次数 (>=0)';
COMMENT ON COLUMN t_article.cover IS '文章封面';
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_article_content
(
    id         BIGSERIAL PRIMARY KEY,

    -- 外键关联字段
    article_id BIGINT NOT NULL,

    -- 正文：PG 的 TEXT 能够容纳海量文字
    content    TEXT,

    -- 显式外键约束：确保 article_id 必须存在于 t_article 表中
    -- ON DELETE CASCADE: 如果物理删除了 t_article，对应的内容也会被自动删除
    CONSTRAINT fk_article_content_article_id
        FOREIGN KEY (article_id)
            REFERENCES t_article (id)
            ON DELETE CASCADE
);

-- 索引
CREATE INDEX idx_article_content_article_id ON t_article_content (article_id);

-- 注释
COMMENT ON TABLE t_article_content IS '文章内容表';
COMMENT ON COLUMN t_article_content.content IS '教程正文';
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_article_category_rel
(
    id          BIGSERIAL PRIMARY KEY,

    -- 文章 ID：添加外键，删除文章时自动删除此关联
    article_id  BIGINT NOT NULL,

    -- 分类 ID：添加外键，删除分类时... (通常分类不轻易删，或者策略不同，这里设为级联删除)
    category_id BIGINT NOT NULL,

    -- 约束：保证 article_id 唯一 (对应 MySQL 的 UNIQUE KEY)
    CONSTRAINT uni_article_category_rel_article_id UNIQUE (article_id),

    -- 外键定义
    CONSTRAINT fk_rel_cat_article
        FOREIGN KEY (article_id)
            REFERENCES t_article (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_rel_cat_category
        FOREIGN KEY (category_id)
            REFERENCES t_category (id)
            ON DELETE CASCADE
);

-- 索引：category_id 需要索引以便反向查询（查询某分类下有哪些文章）
CREATE INDEX idx_rel_cat_category_id ON t_article_category_rel (category_id);

-- 注释
COMMENT ON TABLE t_article_category_rel IS '文章所属分类关联表';
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
-- ====================================================================================================================
CREATE TABLE t_article_tag_rel
(
    id         BIGSERIAL PRIMARY KEY,

    article_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,

    -- 外键定义：级联删除
    CONSTRAINT fk_rel_tag_article
        FOREIGN KEY (article_id)
            REFERENCES t_article (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_rel_tag_tag
        FOREIGN KEY (tag_id)
            REFERENCES t_tag (id)
            ON DELETE CASCADE,

    -- ⚡ 优化：防止同一篇文章被打上重复的标签
    CONSTRAINT uk_article_tag_rel_unique_pair UNIQUE (article_id, tag_id)
);

-- 索引
CREATE INDEX idx_rel_tag_article_id ON t_article_tag_rel (article_id);
CREATE INDEX idx_rel_tag_tag_id ON t_article_tag_rel (tag_id);

-- 注释
COMMENT ON TABLE t_article_tag_rel IS '文章对应标签关联表';
-- ====================================================================================================================
-- ====================================================================================================================