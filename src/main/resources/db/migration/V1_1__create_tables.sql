
CREATE TABLE [dbo].[authorities](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](256) NULL,
	[authority] [varchar](256) NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[oauth_access_token](
	[token_id] [varchar](256) NULL,
	[token] [varbinary](max) NULL,
	[authentication_id] [varchar](256) NULL,
	[user_name] [varchar](256) NULL,
	[client_id] [varchar](256) NULL,
	[authentication] [varbinary](max) NULL,
	[refresh_token] [varchar](256) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]


CREATE TABLE [dbo].[oauth_client_details](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[client_id] [varchar](256) NOT NULL,
	[resource_ids] [varchar](256) NULL,
	[client_secret] [varchar](256) NULL,
	[scope] [varchar](256) NULL,
	[authorized_grant_types] [varchar](256) NULL,
	[web_server_redirect_uri] [varchar](256) NULL,
	[authorities] [varchar](256) NULL,
	[access_token_validity] [int] NULL,
	[refresh_token_validity] [int] NULL,
	[additional_information] [varchar](4096) NULL,
	[autoapprove] [varchar](256) NULL,
PRIMARY KEY CLUSTERED 
(
	[client_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

CREATE TABLE [dbo].[oauth_client_token](
	[token_id] [varchar](256) NULL,
	[token] [varbinary](MAX) NULL,
	[authentication_id] [varchar](256) NULL,
	[user_name] [varchar](256) NULL,
	[client_id] [varchar](256) NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[oauth_code](
	[code] [varchar](256) NULL,
	[authentication] [varbinary](MAX) NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[oauth_refresh_token](
	[token_id] [varchar](256) NULL,
	[token] [varbinary](MAX) NULL,
	[authentication] [varbinary](MAX) NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[users](
    [id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](256) NULL,
	[password] [varchar](256) NULL,
	[email] [varchar](256) NULL,
	[firstname] [varchar](256) NULL,
	[lastname] [varchar](256) NULL,
	[enabled] [bit] NULL,
	[createdon] [datetime2](7) NULL,
	[lastlogin] [datetime2](7) NULL

) ON [PRIMARY]



CREATE TABLE [dbo].[user_audit_log](
	[username] [varchar](256) NULL,
	[eventtime] [datetime2](7) NULL,
	[eventtype] [varchar](50) NULL,
	[ipaddress] [varchar](50) NULL,
	[operatingsystem] [varchar](256) NULL,
	[device_name] [varchar](256) NULL,
	[uri] [varchar](600) NULL,
	[browser_fingerprint] [varchar](200) NULL
) ON [PRIMARY]


