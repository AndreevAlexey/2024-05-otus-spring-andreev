CREATE TABLE ACL_SID (
                         ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         PRINCIPAL BOOLEAN NOT NULL,
                         SID VARCHAR_IGNORECASE(100) NOT NULL,
                         CONSTRAINT UNIQUE_UK_1 UNIQUE(SID,PRINCIPAL)
);

CREATE TABLE ACL_CLASS(
                          ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          CLASS VARCHAR_IGNORECASE(100) NOT NULL,
                          CONSTRAINT UNIQUE_UK_2 UNIQUE(CLASS)
);

CREATE TABLE ACL_OBJECT_IDENTITY(
                                    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    --ID BIGSERIAL PRIMARY KEY,
                                    OBJECT_ID_CLASS BIGINT NOT NULL,
                                    OBJECT_ID_IDENTITY BIGINT NOT NULL,
                                    PARENT_OBJECT BIGINT,
                                    OWNER_SID BIGINT,
                                    ENTRIES_INHERITING BOOLEAN NOT NULL,
                                    CONSTRAINT UNIQUE_UK_3 UNIQUE(OBJECT_ID_CLASS, OBJECT_ID_IDENTITY),
                                    CONSTRAINT FOREIGN_FK_1 FOREIGN KEY(PARENT_OBJECT)REFERENCES ACL_OBJECT_IDENTITY(ID),
                                    CONSTRAINT FOREIGN_FK_2 FOREIGN KEY(OBJECT_ID_CLASS)REFERENCES ACL_CLASS(ID),
                                    CONSTRAINT FOREIGN_FK_3 FOREIGN KEY(OWNER_SID)REFERENCES ACL_SID(ID)
);

CREATE TABLE ACL_ENTRY(
                          ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          ACL_OBJECT_IDENTITY BIGINT NOT NULL,
                          ACE_ORDER INT NOT NULL,
                          SID BIGINT NOT NULL,
                          MASK INTEGER NOT NULL,
                          GRANTING BOOLEAN NOT NULL,
                          AUDIT_SUCCESS BOOLEAN NOT NULL,
                          AUDIT_FAILURE BOOLEAN NOT NULL,
                          CONSTRAINT UNIQUE_UK_4 UNIQUE(ACL_OBJECT_IDENTITY, ACE_ORDER),
                          CONSTRAINT FOREIGN_FK_4 FOREIGN KEY(ACL_OBJECT_IDENTITY) REFERENCES ACL_OBJECT_IDENTITY(ID),
                          CONSTRAINT FOREIGN_FK_5 FOREIGN KEY(SID) REFERENCES ACL_SID(ID)
);

/*CREATE TABLE acl_sid (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    principal tinyint(1) NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_1 (sid,principal)
    );

CREATE TABLE acl_class (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    class varchar(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_2 (class)
    );

CREATE TABLE acl_entry (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    acl_object_identity bigint(20) NOT NULL,
    ace_order int(11) NOT NULL,
    sid bigint(20) NOT NULL,
    mask int(11) NOT NULL,
    granting tinyint(1) NOT NULL,
    audit_success tinyint(1) NOT NULL,
    audit_failure tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_4 (acl_object_identity,ace_order)
    );

CREATE TABLE acl_object_identity (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    object_id_class bigint(20) NOT NULL,
    object_id_identity bigint(20) NOT NULL,
    parent_object bigint(20) DEFAULT NULL,
    owner_sid bigint(20) DEFAULT NULL,
    entries_inheriting tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_3 (object_id_class,object_id_identity)
    );

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);*/