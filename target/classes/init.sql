DROP TABLE IF EXISTS question_answers;
DROP TABLE IF EXISTS right_answer;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS question;

CREATE TABLE question (
    id INT NOT NULL AUTO_INCREMENT,
    question_text VARCHAR(255) NOT NULL,
    right_answer INT,
    PRIMARY KEY (id)
);

CREATE TABLE answer (
    id INT NOT NULL AUTO_INCREMENT,
    question_id INT NOT NULL,
    answer_text VARCHAR(255) NOT NULL,
    answer_index INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

CREATE TABLE question_answers (
    id INT NOT NULL AUTO_INCREMENT,
    question_id INT NOT NULL,
    answer_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY (answer_id) REFERENCES answer(id) ON DELETE CASCADE
);

CREATE TABLE right_answer (
    id INT NOT NULL AUTO_INCREMENT,
    question_id INT NOT NULL,
    right_answer_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY (right_answer_id) REFERENCES answer(id) ON DELETE CASCADE
);
