IF DB_ID(N'webst4') IS NULL
    CREATE DATABASE webst4 COLLATE Vietnamese_CI_AS;
GO

USE webst4;
GO

IF OBJECT_ID(N'dbo.users', N'U') IS NOT NULL DROP TABLE dbo.users;
IF OBJECT_ID(N'dbo.roles', N'U') IS NOT NULL DROP TABLE dbo.roles;
GO

CREATE TABLE dbo.roles (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT UQ_roles_name UNIQUE (name)
);
GO

CREATE TABLE dbo.users (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    email VARCHAR(120) NOT NULL,
    password VARCHAR(150) NOT NULL,
    full_name NVARCHAR(120) NOT NULL,
    enabled BIT NOT NULL CONSTRAINT DF_users_enabled DEFAULT 0,
    created_at DATETIME2(6) NOT NULL CONSTRAINT DF_users_created_at DEFAULT SYSDATETIME(),
    role_id BIGINT NOT NULL,
    CONSTRAINT UQ_users_email UNIQUE (email),
    CONSTRAINT FK_users_roles FOREIGN KEY (role_id) REFERENCES dbo.roles(id)
);
GO