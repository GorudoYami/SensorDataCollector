package pl.polsl.sensordatacollector.data.sql

class CreateTablesDDL {
    fun getSql(): Collection<String> {
        return listOf(
            """
                CREATE TABLE `Sensor` (
                  `id` int(11) NOT NULL,
                  `name` varchar(100) DEFAULT NULL,
                  `index_count` int(11) DEFAULT NULL,
                  PRIMARY KEY (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            """.trimIndent(),
            """
                CREATE TABLE `User` (
                  `id` int(11) NOT NULL AUTO_INCREMENT,
                  `first_name` varchar(100) NOT NULL,
                  `last_name` varchar(100) NOT NULL,
                  PRIMARY KEY (`id`)
                ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            """.trimIndent(),
            """.
                CREATE TABLE `Data` (
                  `id` int(11) NOT NULL,
                  `sensor_id` int(11) NOT NULL,
                  `user_id` int(11) NOT NULL,
                  `timestamp` timestamp NOT NULL,
                  `index` int(11) NOT NULL,
                  `value` decimal(10,0) NOT NULL,
                  PRIMARY KEY (`id`),
                  KEY `Data_Sensor_FK` (`sensor_id`),
                  CONSTRAINT `Data_Sensor_FK` FOREIGN KEY (`sensor_id`) REFERENCES `Sensor` (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            """.trimIndent())
    }
}