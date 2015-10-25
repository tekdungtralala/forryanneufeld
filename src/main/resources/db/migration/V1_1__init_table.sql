CREATE TABLE customer (
  customer_pkid int(11) NOT NULL AUTO_INCREMENT,
  customer_id varchar(100) NOT NULL,
  token varchar(100) NOT NULL,
  PRIMARY KEY (customer_pkid),
  UNIQUE KEY customer_id (customer_id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'customer'
--

INSERT INTO customer VALUES(1, 'customer0001', '123456');
INSERT INTO customer VALUES(2, 'customer0002', '111222333');

-- --------------------------------------------------------

--
-- Table structure for table 'sensors'
--

CREATE TABLE sensors (
  sensor_id int(11) NOT NULL AUTO_INCREMENT,
  customer_id int(50) NOT NULL,
  sensor_uuid varchar(50) NOT NULL,
  format varchar(50) NOT NULL,
  length varchar(50) NOT NULL,
  PRIMARY KEY (sensor_id),
  UNIQUE KEY sensor_uuid (sensor_uuid),
  KEY customer_id (customer_id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'sensors'
--

INSERT INTO sensors VALUES(1, 1, 'sensor_uuid1', 'format', '12');
INSERT INTO sensors VALUES(2, 2, 'sensor_uuid2', 'format', '12');

-- --------------------------------------------------------

--
-- Table structure for table 'sensor_data'
--

CREATE TABLE sensor_data (
  data_id int(11) NOT NULL AUTO_INCREMENT,
  sensor_id int(11) NOT NULL,
  data_time datetime NOT NULL,
  data_value varchar(50) NOT NULL,
  PRIMARY KEY (data_id),
  KEY sensor_id (sensor_id)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'sensor_data'
--

INSERT INTO sensor_data VALUES(1, 1, '2015-10-01 00:00:00', '1');
INSERT INTO sensor_data VALUES(2, 1, '2015-10-02 00:00:00', '2');
INSERT INTO sensor_data VALUES(3, 1, '2015-10-03 00:00:00', '3');
INSERT INTO sensor_data VALUES(4, 2, '2015-10-08 00:00:00', '21');
INSERT INTO sensor_data VALUES(5, 2, '2015-10-09 00:00:00', '22');

--
-- Constraints for dumped tables
--

--
-- Constraints for table sensors
--
ALTER TABLE sensors
  ADD CONSTRAINT sensors_ibfk_1 FOREIGN KEY (customer_id) REFERENCES customer (customer_pkid) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table sensor_data
--
ALTER TABLE sensor_data
  ADD CONSTRAINT sensor_data_ibfk_1 FOREIGN KEY (sensor_id) REFERENCES sensors (sensor_id) ON DELETE CASCADE ON UPDATE CASCADE;
