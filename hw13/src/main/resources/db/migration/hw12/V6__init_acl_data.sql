INSERT INTO ACL_SID (ID, PRINCIPAL, SID)
VALUES
    (1, true, 'user'),
    (2, true, 'admin'),
    (101, false, 'ROLE_USER'),
    (102, false, 'ROLE_ADMIN');


INSERT INTO ACL_CLASS (ID, CLASS)
VALUES
    (1, 'ru.otus.hw.models.Author'),
    (2, 'ru.otus.hw.models.Genre'),
    (3, 'ru.otus.hw.models.Book'),
    (4, 'ru.otus.hw.models.Comment');


INSERT INTO ACL_OBJECT_IDENTITY (OBJECT_ID_CLASS, OBJECT_ID_IDENTITY, PARENT_OBJECT, OWNER_SID, ENTRIES_INHERITING)
VALUES
    -- author
    (1, 1, NULL, 102, true),
    (1, 2, NULL, 102, true),
    (1, 3, NULL, 102, true),
    -- genre
    (2, 1, NULL, 102, true),
    (2, 2, NULL, 102, true),
    (2, 3, NULL, 102, true),
    -- book
    (3, 1, NULL, 102, true),
    (3, 2, NULL, 102, true),
    (3, 3, NULL, 102, true),
    -- comment
    (4, 1, NULL, 102, true),
    (4, 2, NULL, 102, true),
    (4, 3, NULL, 102, true),
    (4, 4, NULL, 102, true),
    (4, 5, NULL, 102, true),
    (4, 6, NULL, 102, true),

    -- for insert
    (3, 0, NULL, 102, true),
    (4, 0, NULL, 102, true);


INSERT INTO ACL_ENTRY (ACL_OBJECT_IDENTITY, ACE_ORDER, SID, MASK, GRANTING, AUDIT_SUCCESS, AUDIT_FAILURE)
VALUES
    -- author
    (1, 1, 101, 1, true, false, false),--read
    (2, 1, 101, 1, true, false, false),--read
    (3, 1, 101, 1, true, false, false),--read
    -- genre
    (4, 1, 101, 1, true, false, false),--read
    (5, 1, 101, 1, true, false, false),--read
    (6, 1, 101, 1, true, false, false),--read
    -- book
    (7, 1, 101, 1, true, false, false),--read
    (8, 1, 101, 1, true, false, false),--read
    (9, 1, 101, 1, true, false, false),--read
    -- comment
    (10, 1, 101, 1, true, false, false),--read
    (11, 1, 101, 1, true, false, false),--read
    (12, 1, 101, 1, true, false, false),--read
    (13, 1, 101, 1, true, false, false),--read
    (14, 1, 101, 1, true, false, false),--read
    (15, 1, 101, 1, true, false, false),--read
    -- for insert
    (16, 1, 102, 4, true, false, false),--create
    (17, 1, 101, 4, true, false, false),--create
    (17, 2, 102, 4, true, false, false);--create