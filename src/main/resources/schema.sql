CREATE TABLE IF NOT EXISTS run (
    id INT NOT NULL,
    title VARCHAR(250) NOT NULL,
    started_on TIMESTAMP NOT NULL,
    completed_on TIMESTAMP NOT NULL,
    kilometers INT NOT NULL,
    location VARCHAR(10) NOT NULL,
    version INT,
    PRIMARY KEY (id)
);

