
-- DROP TABLE IF EXISTS TBL_FLOW_PROCESS_DATA;
  
CREATE TABLE IF NOT EXISTS TBL_FLOW_INPUT_DATA (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  type_name VARCHAR(64) NOT NULL,
  input_text CLOB DEFAULT NULL,
  processed BOOLEAN DEFAULT false,
  sinked BOOLEAN DEFAULT false,
  created_ts TIMESTAMP DEFAULT NOW(),
  updated_ts TIMESTAMP DEFAULT NOW()
);

DROP TABLE IF EXISTS TBL_FLOW_PROCESS_DATA ;
  
CREATE TABLE IF NOT EXISTS TBL_FLOW_PROCESS_DATA (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  input_id BIGINT NOT NULL,
  input_text CLOB DEFAULT NULL,
  metadata CLOB DEFAULT NULL,
  collection VARCHAR(64) DEFAULT 'NONE',
  created_ts TIMESTAMP DEFAULT NOW(),
  updated_ts TIMESTAMP DEFAULT NOW()
);
