CREATE TABLE TaskOption (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    task_id bigint(20) NOT NULL,
    optionText varchar(80) NOT NULL,
    isCorrect boolean NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_TaskOption_Task FOREIGN KEY (task_id) REFERENCES Task(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
