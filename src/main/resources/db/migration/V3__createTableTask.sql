CREATE TABLE Task (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    createdAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statement varchar(255) NOT NULL,
    orderNumber int NOT NULL,
    type enum('OPENTEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    course_id bigint(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Task_Course FOREIGN KEY (course_id) REFERENCES Course(id),
    CONSTRAINT UC_Course_Statement UNIQUE (course_id, statement)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
