DECLARE @Sql NVARCHAR(500)
DECLARE @Cursor CURSOR

SET @Cursor = CURSOR FAST_FORWARD FOR
SELECT DISTINCT sql = 'ALTER TABLE [' + tc2.TABLE_SCHEMA + '].[' +  tc2.TABLE_NAME + '] DROP [' + rc1.CONSTRAINT_NAME + '];'
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc1
    LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc2 ON tc2.CONSTRAINT_NAME =rc1.CONSTRAINT_NAME

OPEN @Cursor
FETCH NEXT FROM @Cursor INTO @Sql

WHILE (@@FETCH_STATUS = 0)
BEGIN
    EXEC sp_executesql @Sql
    FETCH NEXT FROM @Cursor INTO @Sql
END

CLOSE @Cursor
DEALLOCATE @Cursor
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

EXEC sp_MSforeachtable 'DROP TABLE ?'
GO

DECLARE @procName VARCHAR(500)
DECLARE cur CURSOR 

FOR SELECT [name]
FROM sys.objects
WHERE type = 'p'
OPEN cur
FETCH next FROM cur INTO @procName
WHILE @@fetch_status = 0
BEGIN
    EXEC('drop procedure [' + @procName + ']')
    FETCH next FROM cur INTO @procName
END
CLOSE cur
DEALLOCATE cur


CREATE TABLE "User"
(
    user_id INT PRIMARY KEY NOT NULL IDENTITY(1, 1),
    username VARCHAR(50) NOT NULL,
    display_name VARCHAR(50),
    email VARCHAR(100) NOT NULL,
    password VARCHAR(50) NOT NULL,
    reg_date DATE NOT NULL,
    is_verified BIT NOT NULL,
    country_id INT,
    last_track_id INT,
    last_track_stopped_time INT,
    subscription_id INT,
    image_id INT NOT NULL,
    last_login_date DATE,
    last_activity_date DATE,
    last_updated DATE,
    birth_date DATE,
    description VARCHAR(500)
);

CREATE TABLE Country
(
    country_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    country_name VARCHAR(50) NOT NULL
);

CREATE TABLE Follower
(
    user_id INT NOT NULL FOREIGN KEY REFERENCES "User"(user_id),
    follower_id INT NOT NULL FOREIGN KEY REFERENCES "User"(user_id),
    followed_time DATE NOT NULL,
    PRIMARY KEY (user_id, follower_id)
);

CREATE TABLE Playlist
(
    playlist_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    playlist_name VARCHAR(50) NOT NULL,
    author_id INT NOT NULL,
    tracks_num SMALLINT NOT NULL,
    followers_num INT NOT NULL,
    listeners_num INT NOT NULL,
    duration INT NOT NULL,
    rating TINYINT,
    description VARCHAR(100),
    release_date DATE NOT NULL,
    is_private BIT NOT NULL,
    image_id INT NOT NULL,
    last_updated DATE,
);

CREATE TABLE UserPlaylist
(
    user_id INT NOT NULL FOREIGN KEY REFERENCES "User"(user_id),
    playlist_id INT NOT NULL FOREIGN KEY REFERENCES Playlist(playlist_id),
    PRIMARY KEY (user_id, playlist_id)
);

CREATE TABLE Album
(
    album_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    album_name VARCHAR(100) NOT NULL,
    tracks_num SMALLINT NOT NULL,
    followers_num INT NOT NULL,
    listeners_num INT NOT NULL,
    duration SMALLINT NOT NULL,
    rating TINYINT,
    description VARCHAR(100),
    release_date DATE NOT NULL,
    is_private BIT NOT NULL,
    last_updated DATE,
    image_id INT NOT NULL,
    artist_id INT,
)

CREATE TABLE Genre
(
    genre_id SMALLINT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    genre_name VARCHAR(50)
);

CREATE TABLE Track
(
    track_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    track_name VARCHAR(50) NOT NULL,
    listeners_num INT NOT NULL,
    favorites_num INT NOT NULL,
    url VARCHAR(100) NOT NULL,
    image_id INT NOT NULL,
    album_id INT NOT NULL,
    duration SMALLINT NOT NULL,
    release_date DATE NOT NULL,
    genre_id SMALLINT,
    is_explicit BIT NOT NULL,
    is_private BIT NOT NULL,
    lyrics VARCHAR(1000),
    last_updated DATE
);

CREATE TABLE TrackArtist
(
    track_id INT NOT NULL FOREIGN KEY REFERENCES Track(track_id),
    artist_id INT NOT NULL FOREIGN KEY REFERENCES "User"(user_id),
    PRIMARY KEY (track_id, artist_id)
);

CREATE TABLE Image
(
    image_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    url VARCHAR(200) NOT NULL
)

CREATE TABLE PlaylistTrack
(
    playlist_id INT NOT NULL FOREIGN KEY REFERENCES Playlist(playlist_id),
    track_id INT NOT NULL FOREIGN KEY REFERENCES Track(track_id),
    PRIMARY KEY (playlist_id, track_id)
);

CREATE TABLE Subscription
(
    subscription_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    payment_id INT NOT NULL,
    expire_date DATE NOT NULL
)

CREATE TABLE Payment
(
    payment_id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    subscription_type_id TINYINT NOT NULL,
    payment_date DATE NOT NULL,
    payment_amount INT NOT NULL,
)

CREATE TABLE SubscriptionType
(
    subscription_type_id TINYINT NOT NULL PRIMARY KEY IDENTITY(1, 1),
    type_name VARCHAR(100) NOT NULL,
    duration SMALLINT NOT NULL,
    payment_amount INT NOT NULL,
)

INSERT INTO "USER"
    (username, display_name, email, password, is_verified, country_id, subscription_id, image_id, reg_date, birth_date)
VALUES
    ('truebeliever', 'Madiyar', 'madiyar@gmail.com', 'hgsoj52p13pk', 0, 1, 1, 1, '11/22/2020', '11/17/2020'),
    ('demenov', 'Tima', 'tima@gmail.com', 'afsd3', 0, 1, 2, 2, '11/22/2020', '11/17/2020'),
    ('abu', 'Abu Said', 'said@gmail.com', 'bfsd8u', 0, 1, NULL, 1, '11/22/2020', '11/17/2020'),
    ('ic3peak', 'IC3PEAK', 'icepeak@gmail.com', 'asdgf890u', 1, 2, 3, 3, '11/22/2020', '11/17/2020'),
    ('linkinpark', 'Linkin Park', 'linkinpark@gmail.com', '431nm2kl3j14n', 1, 3, 4, 4, '11/22/2020', '11/17/2020'),
    ('molchatdoma', 'Molchat Doma', 'molchatdoma@gmail.com', 'lhkgfjs0', 1, 2, 5, 5, '11/22/2020', '11/17/2020'),
    ('twentyonepilots', 'Twenty One Pilots', 'twentyonepilots@gmail.com', 'k67lpk23', 1, 3, 6, 6, '11/22/2020', '11/17/2020'),
    ('imaginedragons', 'Imagine Dragons', 'imaginedragons@gmail.com', 'asdf124', 1, 3, 7, 7, '11/22/2020', '11/17/2020'),
    ('tessaviolet', 'Tessa Violet', 'tessaviolet@gmail.com', 'qewrjkl41', 1, 3, 8, 8, '11/22/2020', '11/17/2020'),
    ('breathe', 'breathe.', 'breathe@gmail.com', 'gsf34121', 1, 4, 9, 9, '11/22/2020', '11/17/2020');


INSERT INTO Country
    (country_name)
VALUES
    ('Kazakhstan'),
    ('Russia'),
    ('United States'),
    ('United Kingdom');

INSERT INTO follower
VALUES
    (1, 2, CURRENT_TIMESTAMP),
    (2, 1, CURRENT_TIMESTAMP),
    (1, 4, CURRENT_TIMESTAMP),
    (1, 5, CURRENT_TIMESTAMP),
    (2, 5, CURRENT_TIMESTAMP),
    (3, 2, CURRENT_TIMESTAMP),
    (3, 1, CURRENT_TIMESTAMP),
    (3, 4, CURRENT_TIMESTAMP),
    (4, 5, CURRENT_TIMESTAMP),
    (4, 3, CURRENT_TIMESTAMP),
    (3, 8, CURRENT_TIMESTAMP),
    (5, 9, CURRENT_TIMESTAMP);


INSERT INTO Playlist
    (playlist_name, author_id, tracks_num, followers_num, listeners_num, duration, release_date, is_private, image_id)
VALUES
    ('Favorites', 1, 5, 1, 1, 40, CURRENT_TIMESTAMP, 1, 9)

INSERT INTO UserPlaylist
VALUES
    (1, 1);

INSERT INTO Album
    (album_name, tracks_num, followers_num, listeners_num, duration, release_date, is_private, image_id, artist_id)
VALUES
    ('Etazhi', 1, 2, 2, 122, CURRENT_TIMESTAMP, 0, 10, 6),
    ('Evolve', 1, 3, 5, 142, CURRENT_TIMESTAMP, 0, 11, 8),
    ('Bad Ideas', 1, 4, 6, 164, CURRENT_TIMESTAMP, 0, 12, 9);

INSERT INTO Genre
    (genre_name)
VALUES
    ('Pop'),
    ('Hip-hop'),
    ('Pop Rock');

INSERT INTO Track
    (track_name, listeners_num, favorites_num, url, image_id, album_id, duration, release_date, genre_id, is_explicit, is_private)
VALUES
    ('Sudno', 5, 5, 'https://open.spotify.com/track/6BSDpXlm2gtorEzoH4RNHw?si=rsK_Ai2jS262_OAffJc56w', 10, 1, 122, CURRENT_TIMESTAMP, NULL, 1, 0),
    ('Believer', 2, 4, 'https://open.spotify.com/track/6BSDpXlm2gtorEzoH4RNHw?si=rsK_Ai2jS262_OAffJc56w', 11, 2, 142, CURRENT_TIMESTAMP, NULL, 0, 0),
    ('Crush', 7, 6, 'https://open.spotify.com/track/6BSDpXlm2gtorEzoH4RNHw?si=rsK_Ai2jS262_OAffJc56w', 12, 3, 164, CURRENT_TIMESTAMP, NULL, 0, 0)

INSERT INTO TrackArtist
VALUES
    (1, 6),
    (2, 8),
    (3, 9);

INSERT INTO PlaylistTrack
VALUES
    (1, 1),
    (1, 2),
    (1, 3)

INSERT INTO Image
    (url)
VALUES
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD'),
    ('www.pinterest.com%2Fpin%&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJie5ofTlu0CFQAAAAAdAAAAABAD')


INSERT INTO SubscriptionType
    (type_name, duration, payment_amount)
VALUES
    ('One month', 30, 10),
    ('Six months', 180, 45),
    ('One year', 365, 80);

INSERT INTO Payment
    (subscription_type_id, payment_date, payment_amount)
VALUES
    (1, CURRENT_TIMESTAMP, 10),
    (1, CURRENT_TIMESTAMP, 10),
    (1, CURRENT_TIMESTAMP, 10),
    (2, CURRENT_TIMESTAMP, 45),
    (3, CURRENT_TIMESTAMP, 80),
    (1, CURRENT_TIMESTAMP, 10),
    (1, CURRENT_TIMESTAMP, 10),
    (1, CURRENT_TIMESTAMP, 10),
    (1, CURRENT_TIMESTAMP, 10);

INSERT INTO Subscription
    (payment_id, expire_date)
VALUES
    (1, DATEADD(DAY, 30, CURRENT_TIMESTAMP)),
    (2, DATEADD(DAY, 30, CURRENT_TIMESTAMP)),
    (3, DATEADD(DAY, 30, CURRENT_TIMESTAMP)),
    (4, DATEADD(DAY, 180, CURRENT_TIMESTAMP)),
    (5, DATEADD(DAY, 365, CURRENT_TIMESTAMP)),
    (6, DATEADD(DAY, 30, CURRENT_TIMESTAMP)),
    (7, DATEADD(DAY, 30, CURRENT_TIMESTAMP)),
    (8, DATEADD(DAY, 30, CURRENT_TIMESTAMP)),
    (9, DATEADD(DAY, 30, CURRENT_TIMESTAMP));

ALTER TABLE Subscription ADD
FOREIGN KEY (payment_id) REFERENCES Payment(payment_id);

ALTER TABLE Payment ADD
FOREIGN KEY (subscription_type_id) REFERENCES SubscriptionType(subscription_type_id);

ALTER TABLE "User" ADD 
FOREIGN KEY (country_id) REFERENCES Country(country_id),
FOREIGN KEY (last_track_id) REFERENCES Track(track_id),
FOREIGN KEY (subscription_id) REFERENCES Subscription(subscription_id),
FOREIGN KEY (image_id) REFERENCES Image(image_id);

ALTER TABLE Playlist ADD
FOREIGN KEY (author_id) REFERENCES "User"(user_id),
FOREIGN KEY (image_id) REFERENCES Image(image_id);

ALTER TABLE Track ADD
FOREIGN KEY (album_id) REFERENCES Album(album_id),
FOREIGN KEY (image_id) REFERENCES Image(image_id),
FOREIGN KEY (genre_id) REFERENCES Genre(genre_id);

ALTER TABLE Album ADD
FOREIGN KEY (artist_id) REFERENCES "User"(user_id),
FOREIGN KEY (image_id) REFERENCES Image(image_id);
GO


CREATE PROCEDURE RegisterUser(
    @username VARCHAR(20),
    @display_name VARCHAR(50),
    @email VARCHAR(100),
    @password VARCHAR(50))
AS
INSERT INTO "User"
    (username, display_name, email, password, reg_date, is_verified, image_id)
VALUES
    (@username, @display_name, @email, @password, CURRENT_TIMESTAMP, 0, 1);

SELECT user_id
FROM "User"
WHERE username = @username AND password = @password AND email = @email;
GO


EXEC RegisterUser @username = 'dunno', @display_name = 'nothing', @email = 'dunno@gmail.com', @password = 'av908s';
GO


CREATE PROCEDURE CreateAlbum(
    @album_name VARCHAR(50),
    @artist_id INT,
    @image_id INT,
    @description VARCHAR(100),
    @is_private BIT
)
AS
IF NULLIF(@image_id, '') IS NULL
BEGIN
    SET @image_id = 1;
END
INSERT INTO Album
    (album_name, tracks_num, followers_num, listeners_num, duration, description, release_date, is_private, last_updated, image_id, artist_id)
VALUES
    (@album_name, 0, 0, 0, 0, @description, CURRENT_TIMESTAMP, @is_private, CURRENT_TIMESTAMP, @image_id, @artist_id);
GO

EXEC CreateAlbum @album_name = 'Neon Blood', @artist_id = 9, @image_id = 1, @description = NULL, @is_private=0;
GO


CREATE PROCEDURE UploadTrack(
    @track_name VARCHAR(50),
    @user_id INT,
    @url VARCHAR(100),
    @image_id INT,
    @album_id INT,
    @duration SMALLINT,
    @genre_id SMALLINT,
    @is_explicit BIT,
    @is_private BIT,
    @lyrics VARCHAR(1000))
AS
IF NULLIF(@image_id, '') IS NULL
BEGIN
    SET @image_id = 1;
END
INSERT INTO Track
    (track_name, listeners_num, favorites_num, url, image_id, album_id, duration, release_date, genre_id, is_explicit, is_private, lyrics, last_updated)
VALUES
    (@track_name, 0, 0, @url, @image_id, @album_id, @duration, CURRENT_TIMESTAMP, @genre_id, @is_explicit, @is_private, @lyrics, CURRENT_TIMESTAMP);

INSERT INTO TrackArtist
VALUES
    (SCOPE_IDENTITY(), @user_id);
GO


EXEC UploadTrack @track_name = 'Games', @user_id = 9, @url = 'https://uploaded.com/1fj-825@2812hd1', @image_id = NULL, @album_id = 3, @duration
= 133, @genre_id = 2, @is_explicit = 0, @is_private = 0, @lyrics = NULL;


-- SELECT *
-- FROM Track
--     JOIN TrackArtist ON TrackArtist.track_id = Track.track_id
--     JOIN "User" ON "User".user_id = TrackArtist.artist_id;
GO


CREATE PROCEDURE FindUser(
    @username VARCHAR(50),
    @email VARCHAR(100))
AS
SELECT user_id
FROM "User"
WHERE username = @username OR email = @email;
RETURN;


EXEC FindUser @username='demenov', @email='madiyar@gmail.com';
GO


CREATE PROCEDURE SignIn(
    @username VARCHAR(50),
    @password VARCHAR(50)
)
AS
SELECT user_id
FROM "User"
WHERE username = @username AND password = @password;
IF @@ROWCOUNT > 0
    UPDATE "User"
    SET last_login_date = CURRENT_TIMESTAMP, last_activity_date = CURRENT_TIMESTAMP
    WHERE username = @username AND password = @password;
RETURN;

EXEC SignIn @username='demenov', @password='afsd3';
GO


CREATE PROCEDURE FindUserById(
    @user_id INT
)
AS
SELECT *
FROM "USER"
WHERE user_id = @user_id;
RETURN;

EXEC FindUserById @user_id = 12;